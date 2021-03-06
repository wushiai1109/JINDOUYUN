package com.jindouyun.admin.service;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jindouyun.admin.model.vo.*;
import com.jindouyun.common.constant.MergeOrderConstant;
import com.jindouyun.core.notify.NotifyService;
import com.jindouyun.core.notify.NotifyType;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.service.*;
import com.jindouyun.db.util.OrderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.jindouyun.admin.util.AdminResponseCode.*;
import static com.jindouyun.common.constant.MergeOrderConstant.MERGE_ORDER_DELIVER;
import static com.jindouyun.common.constant.MergeOrderConstant.MERGE_ORDER_MERCHANT_RECEIVE;
import static com.jindouyun.db.util.OrderUtil.*;

@Service

public class AdminOrderService {
    private final Log logger = LogFactory.getLog(AdminOrderService.class);

    @Autowired
    private JindouyunOrderGoodsService orderGoodsService;
    @Autowired
    private JindouyunOrderService orderService;
    @Autowired
    private JindouyunExpressOrderService expressOrderService;
    @Autowired
    private JindouyunGoodsProductService productService;
    @Autowired
    private JindouyunUserService userService;
    @Autowired
    private JindouyunBrandService brandService;
    @Autowired
    private JindouyunOrderSplitService splitService;
    @Autowired
    private JindouyunMergeOrderService mergeOrderService;
    @Autowired
    private JindouyunGrabOrderService grabOrderService;
    @Autowired
    private JindouyunBrandOrderService brandOrderService;
    @Autowired
    private JindouyunCommentService commentService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private LogHelper logHelper;

    /**
     * 查询快递订单详情
     * @param orderId
     * @return
     */
    public Object queryExpressOrderDetail(Integer orderId){
        if(orderId == null){
            return ResponseUtil.badArgument();
        }
        JindouyunExpressOrder expressOrder = expressOrderService.queryById(orderId);
        return ResponseUtil.ok(expressOrder);
    }

    /**
     * 根据分单号 查询订单详情
     * @param splitOrderId
     * @return
     */
    public Object queryDetailBySplitId(Integer splitOrderId){
        if(splitOrderId == null){
            System.err.println("据分单号 查询订单详情 - splitOrderId为 null");
            return ResponseUtil.badArgument();
        }
        OrderSplitVO splitVO = splitService.queryOrderSplitVO(splitOrderId);
        if(splitVO == null){
            System.err.println("据分单号 查询订单详情 - splitOrder为 null");
            return ResponseUtil.badArgument();
        }

        return ResponseUtil.ok(splitVO);
    }

    /**
     * 条件查询 userId orderSn orderStatusArray
     * @param userId
     * @param orderSn
     * @param orderStatusArray
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public Object listExpressOrder(Integer userId, String orderSn, List<Short> orderStatusArray,
                                   Integer page, Integer limit, String sort, String order){
        List<JindouyunExpressOrder> orderList = expressOrderService.queryCommonOrderSelective(userId,null,orderSn,null,null,null,null,null,null,orderStatusArray,page,limit,sort,order);
        return ResponseUtil.okList(orderList);
    }
    /**
     * 条件查询 userId orderSn orderStatusArray
     * @param userId
     * @param orderSn
     * @param orderStatusArray
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public Object list(Integer userId, String orderSn, List<Short> orderStatusArray,
                       Integer page, Integer limit, String sort, String order) {
        List<JindouyunOrder> orderList = orderService.querySelective(userId, orderSn, orderStatusArray, page, limit,
                sort, order);
        return ResponseUtil.okList(orderList);
    }

    /**
     * 查询用户订单详情
     * @param id
     * @return
     */
    public Object detail(Integer id) {
        JindouyunOrder order = orderService.findById(id);
        List<JindouyunOrderSplit> orderSplits = splitService.queryByOid(id);
        List<OrderSplitVO> splitOrderVOs = new ArrayList<>();
        for (JindouyunOrderSplit orderSplit:orderSplits) {
            BrandVo brandVo = brandService.findBrandVoById(orderSplit.getBrandId());
            List<JindouyunOrderGoods> orderGoodsList = orderGoodsService.queryBySplitOrderId(orderSplit.getOrderId());
            OrderSplitVO splitVO = new OrderSplitVO(brandVo,orderSplit,orderGoodsList);
            splitOrderVOs.add(splitVO);
        }
//        System.err.println(order.getUserId());
        UserVo user = userService.findUserVoById(order.getUserId());
        Map<String, Object> data = new HashMap<>();
        data.put("order", order);
        data.put("splitOrders", splitOrderVOs);
        data.put("user", user);

        return ResponseUtil.ok(data);
    }

    /**
     * 订单退款
     * <p>
     * 1. 检测当前订单是否能够退款;
     * 2. 微信退款操作;
     * 3. 设置订单退款确认状态；
     * 4. 订单商品库存回库。
     * <p>
     * TODO
     * 虽然接入了微信退款API，但是从安全角度考虑，建议开发者删除这里微信退款代码，采用以下两步走步骤：
     * 1. 管理员登录微信官方支付平台点击退款操作进行退款
     * 2. 管理员登录Jindouyun管理后台点击退款操作进行订单状态修改和商品库存回库
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    @Transactional
    public Object refund(String body) {
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        String refundMoney = JacksonUtil.parseString(body, "refundMoney");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }
        if (StringUtils.isEmpty(refundMoney)) {
            return ResponseUtil.badArgument();
        }

        JindouyunOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }

        if (order.getActualPrice().compareTo(new BigDecimal(refundMoney)) != 0) {
            return ResponseUtil.badArgumentValue();
        }

        // 如果订单不是退款状态，则不能退款
        if (!order.getOrderStatus().equals(OrderUtil.STATUS_REFUND)) {
            return ResponseUtil.fail(ORDER_CONFIRM_NOT_ALLOWED, "订单不能确认收货");
        }

        // 微信退款
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        wxPayRefundRequest.setOutTradeNo(order.getOrderSn());
        wxPayRefundRequest.setOutRefundNo("refund_" + order.getOrderSn());
        // 元转成分
        Integer totalFee = order.getActualPrice().multiply(new BigDecimal(100)).intValue();
        wxPayRefundRequest.setTotalFee(totalFee);
        wxPayRefundRequest.setRefundFee(totalFee);

        WxPayRefundResult wxPayRefundResult;
        try {
            wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
        } catch (WxPayException e) {
            logger.error(e.getMessage(), e);
            return ResponseUtil.fail(ORDER_REFUND_FAILED, "订单退款失败");
        }
        if (!wxPayRefundResult.getReturnCode().equals("SUCCESS")) {
            logger.warn("refund fail: " + wxPayRefundResult.getReturnMsg());
            return ResponseUtil.fail(ORDER_REFUND_FAILED, "订单退款失败");
        }
        if (!wxPayRefundResult.getResultCode().equals("SUCCESS")) {
            logger.warn("refund fail: " + wxPayRefundResult.getReturnMsg());
            return ResponseUtil.fail(ORDER_REFUND_FAILED, "订单退款失败");
        }

        LocalDateTime now = LocalDateTime.now();
        // 设置订单取消状态
        order.setOrderStatus(OrderUtil.STATUS_REFUND_CONFIRM);
        order.setEndTime(now);
        // 记录订单退款相关信息
        order.setRefundAmount(order.getActualPrice());
        order.setRefundType("微信退款接口");
        order.setRefundContent(wxPayRefundResult.getRefundId());
        order.setRefundTime(now);
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            throw new RuntimeException("更新数据已失效");
        }

        // 商品货品数量增加
        List<JindouyunOrderGoods> orderGoodsList = orderGoodsService.queryByOid(orderId);
        for (JindouyunOrderGoods orderGoods : orderGoodsList) {
            Integer productId = orderGoods.getProductId();
            Short number = orderGoods.getNumber();
            if (productService.addStock(productId, number) == 0) {
                throw new RuntimeException("商品货品库存增加失败");
            }
        }

        //TODO 发送邮件和短信通知，这里采用异步发送
        // 退款成功通知用户, 例如“您申请的订单退款 [ 单号:{1} ] 已成功，请耐心等待到账。”
        // 注意订单号只发后6位
        notifyService.notifySmsTemplate(order.getMobile(), NotifyType.REFUND,
                new String[]{order.getOrderSn().substring(8, 14)});

        logHelper.logOrderSucceed("退款", "订单编号 " + orderId);
        return ResponseUtil.ok();
    }

//    /**
//     * 发货
//     * 1. 检测当前订单是否能够发货
//     * 2. 设置订单发货状态
//     *
//     * @param body 订单信息，{ orderId：xxx, shipSn: xxx, shipChannel: xxx }
//     * @return 订单操作结果
//     * 成功则 { errno: 0, errmsg: '成功' }
//     * 失败则 { errno: XXX, errmsg: XXX }
//     */
//    public Object ship(String body) {
//        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
//        String shipSn = JacksonUtil.parseString(body, "shipSn");
//        String shipChannel = JacksonUtil.parseString(body, "shipChannel");
//        if (orderId == null || shipSn == null || shipChannel == null) {
//            return ResponseUtil.badArgument();
//        }
//
//        JindouyunOrder order = orderService.findById(orderId);
//        if (order == null) {
//            return ResponseUtil.badArgument();
//        }
//
//        // 如果订单不是已付款状态，则不能发货
//        if (!order.getOrderStatus().equals(OrderUtil.STATUS_PAY)) {
//            return ResponseUtil.fail(ORDER_CONFIRM_NOT_ALLOWED, "订单不能确认收货");
//        }
//
//        order.setOrderStatus(OrderUtil.STATUS_SHIP);
//        order.setShipSn(shipSn);
//        order.setShipChannel(shipChannel);
//        order.setShipTime(LocalDateTime.now());
//        if (orderService.updateWithOptimisticLocker(order) == 0) {
//            return ResponseUtil.updatedDateExpired();
//        }
//
//        //TODO 发送邮件和短信通知，这里采用异步发送
//        // 发货会发送通知短信给用户:          *
//        // "您的订单已经发货，快递公司 {1}，快递单 {2} ，请注意查收"
//        notifyService.notifySmsTemplate(order.getMobile(), NotifyType.SHIP, new String[]{shipChannel, shipSn});
//
//        logHelper.logOrderSucceed("发货", "订单编号 " + orderId);
//        return ResponseUtil.ok();
//    }


//    /**
//     * 回复订单商品
//     *
//     * @param body 订单信息，{ orderId：xxx }
//     * @return 订单操作结果
//     * 成功则 { errno: 0, errmsg: '成功' }
//     * 失败则 { errno: XXX, errmsg: XXX }
//     */
//    public Object reply(String body) {
//        Integer commentId = JacksonUtil.parseInteger(body, "commentId");
//        if (commentId == null || commentId == 0) {
//            return ResponseUtil.badArgument();
//        }
//        // 目前只支持回复一次
//        if (commentService.findById(commentId) != null) {
//            return ResponseUtil.fail(ORDER_REPLY_EXIST, "订单商品已回复！");
//        }
//        String content = JacksonUtil.parseString(body, "content");
//        if (StringUtils.isEmpty(content)) {
//            return ResponseUtil.badArgument();
//        }
//        // 创建评价回复
//        JindouyunComment comment = new JindouyunComment();
//        comment.setType((byte) 2);
//        comment.setValueId(commentId);
//        comment.setContent(content);
//        comment.setUserId(0);                 // 评价回复没有用
//        comment.setStar((short) 0);           // 评价回复没有用
//        comment.setHasPicture(false);        // 评价回复没有用
//        comment.setPicUrls(new String[]{});  // 评价回复没有用
//        commentService.save(comment);
//
//        return ResponseUtil.ok();
//    }


    public Object select(Byte type, Integer userId, Integer mergeId, String orderSn, Integer brandId,
                                            String name, String mobile, Short building, String address, LocalDateTime startTime,LocalDateTime endTime,
                                            List<Short> orderStatusArray, Integer page, Integer limit, String sort, String order){

        List<Integer> orderIds;
        Object reslut = null;
        //查找符合条件的orderId列表
        //商品（0） 外卖（0）
        if( type == 0 || type == 1){
            //查询所有符合条件的 商品订单
            List<JindouyunOrder> orders = orderService.queryGoodsOrderIdsSelective(
                    userId,orderSn,name,mobile,building,address,startTime,endTime,null,null,null,null,null);
            orderIds = new ArrayList<>();
            for (JindouyunOrder jindouyunOrder:orders) {
                orderIds.add(jindouyunOrder.getId());
            }
            //查询所有订单下 的split_order,商品或者是外卖订单在此处区分
            List<JindouyunOrderSplit> orderSplits = splitService.querySelective(type, mergeId,brandId,orderStatusArray,orderIds,page,limit,sort,order);

            //组织以合并订单，和未合并订单
            MergeVO mergeVO = new MergeVO();
            List<JindouyunOrderSplit> unMergeList = new ArrayList<>();
            Map<Integer, MergeInfo> mergeMap = new HashMap<>();

            for (JindouyunOrderSplit splitOrder:orderSplits) {
                //未合并
                if(splitOrder.getMergeId() == null || splitOrder.getMergeId()== -1){
                    unMergeList.add(splitOrder);
                //已合并
                }else{
                    Integer id = splitOrder.getMergeId();
                    if(!mergeMap.containsKey(id)){
                        MergeInfo mergeInfo = mergeOrderService.queryMergeInfoById(id);
                        mergeMap.put(id,mergeInfo);
                    }
                }
            }
            mergeVO.setType(type);
            mergeVO.setUnMergeList(unMergeList);
            mergeVO.setMergeList(new ArrayList<>(mergeMap.values()));
            reslut = mergeVO;
        }else if(type == 2){
            //查询所有符合条件的 快递订单
            List<JindouyunExpressOrder> orders = expressOrderService.queryCommonOrderSelective(userId,mergeId,orderSn,name,mobile,building,address,
                    startTime,endTime,orderStatusArray,page,limit,sort,order);
            //组织以合并订单，和未合并订单
            MergeExpressVO mergeExpressVO = new MergeExpressVO();
            List<JindouyunExpressOrder> unMergeList = new ArrayList<>();
            Map<Integer, MergeExpressInfo> mergeMap = new HashMap<>();

            for (JindouyunExpressOrder expressOrder:orders) {
                //未合并
                if(expressOrder.getMergeId() == null || expressOrder.getMergeId()== -1){
                    unMergeList.add(expressOrder);
                    //已合并
                }else{
                    Integer id = expressOrder.getMergeId();
                    if(!mergeMap.containsKey(id)){
                        MergeExpressInfo mergeExpressInfo = mergeOrderService.queryMergeExpressInfoById(id);
                        mergeMap.put(id,mergeExpressInfo);
                    }

                }
            }
            mergeExpressVO.setType(type);
            mergeExpressVO.setUnMergeList(unMergeList);
            mergeExpressVO.setMergeList(new ArrayList<>(mergeMap.values()));
            reslut = mergeExpressVO;
        }else {
            ResponseUtil.badArgument();
        }

        return ResponseUtil.ok(reslut);
    }

    /**
     *  合并订单
     * @param type
     * @param message
     * @param release
     * @param orderIds
     * @return
     */
    @Transactional
    public Object merge(Byte type, String message, Byte release, List<Integer> orderIds) {

        Subject currentUser = SecurityUtils.getSubject();
        JindouyunAdmin admin = (JindouyunAdmin) currentUser.getPrincipal();

        if (type != 0 && type != 1 && type != 2) {
            System.err.println("订单合并 - type：" + type);
            return ResponseUtil.badArgument();
        }
        JindouyunMergeOrder mergeOrder = new JindouyunMergeOrder();

        String orderSn = mergeOrderService.generateOrderSn(admin.getId());
        mergeOrder.setAdminId(admin.getId());
        mergeOrder.setOrderSn(orderSn);
        mergeOrder.setMessage(message);
        mergeOrder.setType(type);
        mergeOrder.setRelease(release);
        //商家已发货 31
        //直接提取快递 31
        if (type == 0 || type == 2){
            mergeOrder.setStatus(MERGE_ORDER_DELIVER);
        }else if (type == 1){
            //用户已付款 商家未发货 21
            mergeOrder.setStatus(MergeOrderConstant.MERGE_ORDER_UNDELIVER);
        }

        if (release == 1) {
            mergeOrder.setReleaseTime(LocalDateTime.now().plusSeconds(30));
        }

        if (mergeOrderService.add(mergeOrder) == 0) {
            System.err.println("订单合并 - 订单添加失败");
            return ResponseUtil.fail();
        }
        mergeOrder = mergeOrderService.queryByAdminIdAndOrderSn(admin.getId(), orderSn);
        if (mergeOrder == null) {
            System.err.println("订单合并 - 订单添加后，未查询到该计量");
            return ResponseUtil.fail();
        }
        Short num = 0;
        BigDecimal allPrice = new BigDecimal(0);

        // 合并订单
        if (type == 0 || type == 1) {
            for (Integer orderSplitId : orderIds) {
                JindouyunOrderSplit orderSplit = splitService.queryById(orderSplitId);
                if (orderSplit.getMergeId() == null || orderSplit.getMergeId()== -1) {
                    if (splitService.updateMergeId(orderSplitId, mergeOrder.getId()) != 0) {
                        allPrice.add(orderSplit.getGoodsPrice());
                        num++;
                    }
                }else{

                }
            }
        } else if (type == 2) {
            for (Integer orderSplitId : orderIds) {
                JindouyunExpressOrder expressOrder = expressOrderService.queryById(orderSplitId);
                if (expressOrder.getMergeId() == null || expressOrder.getMergeId() == -1) {
                    if (expressOrderService.updateMergeId(orderSplitId, mergeOrder.getId()) != 0) {
                        allPrice.add(expressOrder.getActualPrice());
                        num++;
                    }
                }
            }
        }
        //
        mergeOrder.setNum(num);
        mergeOrder.setAllPrice(allPrice);

        //当release=1的时候发布订单到骑手端 ， 商家端
        if(release == 1){
            //发布到骑手端
            sendMergeOrderToDeliveryBrand(mergeOrder);
        }

        if(mergeOrderService.updateAllPriceAndNum(mergeOrder.getId(), allPrice, num) ==0 ){
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(mergeOrder);
    }


    /**
     * 发布
     * @param mergeId
     * @return
     */
    @Transactional
    public Object release(Integer mergeId){
        Subject currentUser = SecurityUtils.getSubject();
        JindouyunAdmin admin = (JindouyunAdmin) currentUser.getPrincipal();

        JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(mergeId);
        if(mergeOrder == null){
            System.err.println("发布mergeOrder - mergeOrder不存在");
            return ResponseUtil.fail(MERGE_ORDER_NUEXIST,"mergeId不存在");
        }

        //更新状态
        Byte type = mergeOrder.getType();
        if ( type== 0 || type==2){
            mergeOrder.setType(MERGE_ORDER_DELIVER);
        }else if (type == 1){
            mergeOrder.setType(MERGE_ORDER_MERCHANT_RECEIVE);
        }
        mergeOrder.setAdminId(admin.getId());
        mergeOrder.setRelease((byte) 1);
        mergeOrder.setUpdateTime(LocalDateTime.now());
        mergeOrder.setReleaseTime(LocalDateTime.now());
        if(mergeOrderService.updateOrderStatus(mergeOrder) == 0){
            return ResponseUtil.fail();
        }
        sendMergeOrderToDeliveryBrand(mergeOrder);
        return ResponseUtil.ok();
    }

    /**
     *
     * @param mergeId
     * @return
     */
    @Transactional
    public Object delete(Byte type, Integer mergeId){
        if(type == 0|| type == 1){
            if(splitService.setMergeIdNull(mergeId)==0){
                return ResponseUtil.fail(ORDER_NOT_DELETED,"配送员已接单，无法删除");
            }
        }else if(type == 2){
            if(expressOrderService.setMergeIdNull(mergeId)==0){
                return ResponseUtil.fail(ORDER_NOT_DELETED,"配送员已接单，无法删除");
            }
        }else{
            return ResponseUtil.badArgument();
        }
        if(mergeOrderService.delete(mergeId)==0){
            return ResponseUtil.badArgument();
        }
        return ResponseUtil.ok();
    }

    public void sendMergeOrderToDeliveryBrand(JindouyunMergeOrder mergeOrder){

        Byte type = mergeOrder.getType();
        if( type == 0 || type == 2){
            //发布到骑手端
            if(grabOrderService.queryByMergeId(mergeOrder.getId()) == null){
                JindouyunGrabOrder grabOrder = new JindouyunGrabOrder();
                grabOrder.setDeliveryId(-1);
                grabOrder.setUserId(-1);
                grabOrder.setOrderId(mergeOrder.getId());
                grabOrder.setAdminId(mergeOrder.getAdminId());
                grabOrder.setForce(false);
                grabOrderService.add(grabOrder);
            }
        }else if(type == 1){
            //发布到商家端
            if(brandOrderService.queryByMergeId(mergeOrder.getId()) == null){
                List<JindouyunOrderSplit> orderSplits = splitService.queryByMergeId(mergeOrder.getId());
                if(orderSplits==null || orderSplits.size() != mergeOrder.getNum()){
                    throw new RuntimeException("合单子订单数量与orderSplit数量不同");
                }
                JindouyunBrandOrder brandOrder = new JindouyunBrandOrder();
                brandOrder.setOrderId(mergeOrder.getId());
                brandOrder.setBrandId(orderSplits.get(0).getBrandId());
                brandOrder.setStatus((short)mergeOrder.getStatus());
                brandOrder.setUserId(-1);
                brandOrder.setDeliveryId(-1);
                brandOrderService.add(brandOrder);
            }
        }
        //更新子订单的状态
        if( type == 0) {
            //201
            splitService.updateStatusByMergeId(mergeOrder.getId(), STATUS_PAY);
        }else if( type == 1){
            //301
            splitService.updateStatusByMergeId(mergeOrder.getId(), STATUS_SHIP);
        }else if(type == 2){
            //301
            expressOrderService.updateStatusByMergeId(mergeOrder.getId(),STATUS_SHIP);
        }
    }
}
