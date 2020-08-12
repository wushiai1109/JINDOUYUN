package com.jindouyun.admin.service;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jindouyun.admin.model.dto.MergeExpressInfo;
import com.jindouyun.admin.model.dto.MergeExpressVO;
import com.jindouyun.admin.model.dto.MergeInfo;
import com.jindouyun.admin.model.dto.MergeVO;
import com.jindouyun.core.notify.NotifyService;
import com.jindouyun.core.notify.NotifyType;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.service.*;
import com.jindouyun.db.util.OrderUtil;
import io.swagger.models.auth.In;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jindouyun.admin.util.AdminResponseCode.*;

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
    private JindouyunOrderSplitService splitService;
    @Autowired
    private JindouyunMergeOrderService mergeOrderService;
    @Autowired
    private JindouyunCommentService commentService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private LogHelper logHelper;

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
     * 查询订单详情
     * @param id
     * @return
     */
    public Object detail(Integer id) {
        JindouyunOrder order = orderService.findById(id);
        List<JindouyunOrderGoods> orderGoods = orderGoodsService.queryByOid(id);
        UserVo user = userService.findUserVoById(order.getUserId());
        Map<String, Object> data = new HashMap<>();
        data.put("order", order);
        data.put("orderGoods", orderGoods);
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
            Map<Integer,MergeInfo> mergeMap = new HashMap<>();

            for (JindouyunOrderSplit splitOrder:orderSplits) {
                //未合并
                if(splitOrder.getMergeId() == -1 || splitOrder.getMergeId()==null){
                    unMergeList.add(splitOrder);
                //已合并
                }else{
                    Integer id = splitOrder.getMergeId();
                    JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(id);
                    if(mergeOrder != null){
                        if(!mergeMap.containsKey(id)){
                            MergeInfo mergeInfo = new MergeInfo();
                            mergeInfo.setId(id);
                            mergeInfo.setOrderSn(mergeOrder.getOrderSn());
                            mergeInfo.setMessage(mergeOrder.getMessage());
                            mergeInfo.setAllPrice(mergeOrder.getAllPrice());
                            mergeInfo.setSplitOrder(new ArrayList<>());
                        }
                        MergeInfo mergeInfo = mergeMap.get(id);
                        mergeInfo.getSplitOrder().add(splitOrder);
                    }
                }
            }
            mergeVO.setType(type);
            mergeVO.setUnMergeList(unMergeList);
            mergeVO.setMergeList((List<MergeInfo>)mergeMap.values());
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
                if(expressOrder.getMergeId() == -1 || expressOrder.getMergeId()==null){
                    unMergeList.add(expressOrder);
                    //已合并
                }else{
                    Integer id = expressOrder.getMergeId();
                    JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(id);
                    if(mergeOrder != null){
                        if(!mergeMap.containsKey(id)){
                            MergeExpressInfo mergeExpressInfo = new MergeExpressInfo();
                            mergeExpressInfo.setId(id);
                            mergeExpressInfo.setOrderSn(mergeOrder.getOrderSn());
                            mergeExpressInfo.setMessage(mergeOrder.getMessage());
                            mergeExpressInfo.setAllPrice(mergeOrder.getAllPrice());
                            mergeExpressInfo.setSplitOrder(new ArrayList<>());
                        }
                        MergeExpressInfo mergeExpressInfo = mergeMap.get(id);
                        mergeExpressInfo.getSplitOrder().add(expressOrder);
                    }
                }
            }
            mergeExpressVO.setType(type);
            mergeExpressVO.setUnMergeList(unMergeList);
            mergeExpressVO.setMergeList((List<MergeExpressInfo>)mergeMap.values());
            reslut = mergeExpressVO;
        }else {
            ResponseUtil.badArgument();
        }

        return ResponseUtil.ok(reslut);
    }

    /**
     *  合并订单
     * @param type
     * @param adminId
     * @param message
     * @param release
     * @param orderIds
     * @return
     */
    @Transactional
    public Object merge(Byte type, Integer adminId, String message, Byte release, List<Integer> orderIds) {

        if (type != 0 || type != 1 || type != 2) {
            System.err.println("订单合并 - type：" + type);
            return ResponseUtil.badArgument();
        }
        JindouyunMergeOrder mergeOrder = new JindouyunMergeOrder();

        String orderSn = mergeOrderService.generateOrderSn(adminId);
        mergeOrder.setAdminId(adminId);
        mergeOrder.setOrderSn(orderSn);
        mergeOrder.setMessage(message);
        mergeOrder.setType(type);
        mergeOrder.setRelease(release);
        mergeOrder.setStatus((byte) 0);
        if (release == 1) {
            mergeOrder.setReleaseTime(LocalDateTime.now().plusSeconds(30));
        }

        if (mergeOrderService.add(mergeOrder) == 0) {
            System.err.println("订单合并 - 订单添加失败");
            return ResponseUtil.fail();
        }
        mergeOrder = mergeOrderService.queryByAdminIdAndOrderSn(adminId, orderSn);
        if (mergeOrder == null) {
            System.err.println("订单合并 - 订单添加后，未查询到该计量");
            return ResponseUtil.fail();
        }
        Short num = null;
        BigDecimal allPrice = new BigDecimal(0);

        // 合并订单
        if (type == 0 || type == 1) {
            for (Integer orderSplitId : orderIds) {
                JindouyunOrderSplit orderSplit = splitService.queryById(orderSplitId);
                if (orderSplit.getMergeId() == -1 || orderSplit.getMergeId()==null) {
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
                if (expressOrder.getMergeId() == -1 || expressOrder.getMergeId() == null) {
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

        if(mergeOrderService.updateAllPriceAndNum(mergeOrder.getId(), allPrice, num) ==0 ){
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok();
    }


    /**
     * 发布
     * @param mergeId
     * @param adminId
     * @return
     */
    @Transactional
    public Object release(Integer mergeId,Integer adminId){
        JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(mergeId);
        if(mergeOrder == null){
            System.err.println("发布mergeOrder - mergeOrder不存在");
            return ResponseUtil.badArgument();
        }
        if (mergeOrderService.updateRelease(mergeId,adminId)!=0){
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    /**
     *
     * @param mergeId
     * @return
     */
    @Transactional
    public Object delete(Byte type, Integer mergeId){
        if(type == 0|| type == 1){
            splitService.setMergeIdNull(mergeId);
        }else if(type == 2){
            expressOrderService.setMergeIdNull(mergeId);
        }else{
            return ResponseUtil.badArgument();
        }
        if(mergeOrderService.delete(mergeId)==0){
            return ResponseUtil.badArgument();
        }
        return ResponseUtil.ok();
    }
}
