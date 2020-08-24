package com.jindouyun.wx.service;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jindouyun.core.express.ExpressService;
import com.jindouyun.core.notify.NotifyService;
import com.jindouyun.core.notify.NotifyType;
import com.jindouyun.core.qcode.QCodeService;
import com.jindouyun.core.system.SystemConfig;
import com.jindouyun.core.task.TaskService;
import com.jindouyun.common.util.DateTimeUtil;
import com.jindouyun.common.util.IpUtil;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.db.dao.JindouyunOrderGoodsMapper;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.service.*;
import com.jindouyun.common.constant.CouponUserConstant;
import com.jindouyun.db.util.OrderHandleOption;
import com.jindouyun.db.util.OrderUtil;
import com.jindouyun.wx.task.OrderUnpaidTask;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jindouyun.core.util.ResponseUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jindouyun.common.constant.WxResponseCode.*;

/**
 * @ClassName WxOrderService
 * @Description * 订单服务
 * *
 * * <p>
 * * 订单状态：
 * * 101 订单生成，未支付；102，下单后未支付用户取消；103，下单后未支付超时系统自动取消
 * * 201 支付完成，商家未发货；202，订单生产，已付款未发货，但是退款取消；
 * * 301 商家发货，用户未确认；
 * * 401 用户确认收货； 402 用户没有确认收货超过一定时间，系统自动确认收货；
 * *
 * * <p>
 * * 用户操作：
 * * 当101用户未付款时，此时用户可以进行的操作是取消订单，或者付款操作
 * * 当201支付完成而商家未发货时，此时用户可以取消订单并申请退款
 * * 当301商家已发货时，此时用户可以有确认收货的操作
 * * 当401用户确认收货以后，此时用户可以进行的操作是删除订单，评价商品，或者再次购买
 * * 当402系统自动确认收货以后，此时用户可以删除订单，评价商品，或者再次购买
 * *
 * * <p>
 * * 注意：目前不支持订单退货和售后服务
 * @Author Bruce
 * @Date 2020/7/25 2:51 下午
 */
@Service
@Transactional
public class WxOrderService {

    private final Log logger = LogFactory.getLog(WxOrderService.class);

    @Autowired
    private JindouyunUserService userService;

    @Autowired
    private JindouyunOrderSplitService orderSplitService;

    @Autowired
    private JindouyunGoodsService goodsService;

    @Autowired
    private JindouyunOrderService orderService;
    @Autowired
    private JindouyunOrderGoodsService orderGoodsService;
    @Autowired
    private JindouyunAddressService addressService;
    @Autowired
    private JindouyunCartService cartService;
    @Autowired
    private JindouyunGoodsProductService productService;

    @Autowired
    private JindouyunBrandService brandService;

    @Resource
    private JindouyunOrderGoodsMapper orderGoodsMapper;

    @Autowired
    @Qualifier("wxPayService")
    private WxPayService wxPayService;
    @Autowired
    private NotifyService notifyService;
    //    @Autowired
//    private JindouyunGrouponRulesService grouponRulesService;
//    @Autowired
//    private JindouyunGrouponService grouponService;
    @Autowired
    private QCodeService qCodeService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private JindouyunCommentService commentService;
    @Autowired
    private JindouyunCouponService couponService;
    @Autowired
    private JindouyunCouponUserService couponUserService;
    @Autowired
    private CouponVerifyService couponVerifyService;
    @Autowired
    private TaskService taskService;

    /**
     * 订单列表
     *
     * @param userId   用户ID
     * @param showType 订单信息：
     *                 0，全部订单；
     *                 1，待付款；
     *                 2，待发货；
     *                 3，待收货；
     *                 4，待评价。
     * @param page     分页页数
     * @param limit    分页大小
     * @return 订单列表
     */
    public Object list(Integer userId, Integer showType, Integer page, Integer limit, String sort, String order) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        List<Short> orderStatus = OrderUtil.orderStatus(showType);
        List<JindouyunOrder> orderList = orderService.queryByOrderStatus(userId, orderStatus, page, limit, sort, order);

        List<Map<String, Object>> orderVoList = new ArrayList<>(orderList.size());
        for (JindouyunOrder o : orderList) {
            Map<String, Object> orderVo = new HashMap<>();
            orderVo.put("id", o.getId());
            orderVo.put("orderSn", o.getOrderSn());
            orderVo.put("actualPrice", o.getActualPrice());
            orderVo.put("orderStatusText", OrderUtil.orderStatusText(o));
            orderVo.put("handleOption", OrderUtil.build(o));

//            JindouyunGroupon groupon = grouponService.queryByOrderId(o.getId());
//            if (groupon != null) {
//                orderVo.put("isGroupin", true);
//            } else {
//                orderVo.put("isGroupin", false);
//            }

            List<JindouyunOrderGoods> orderGoodsList = orderGoodsService.queryByOid(o.getId());
            List<Map<String, Object>> orderGoodsVoList = new ArrayList<>(orderGoodsList.size());
            for (JindouyunOrderGoods orderGoods : orderGoodsList) {
                Map<String, Object> orderGoodsVo = new HashMap<>();
                orderGoodsVo.put("id", orderGoods.getId());
                orderGoodsVo.put("goodsName", orderGoods.getGoodsName());
                orderGoodsVo.put("number", orderGoods.getNumber());
                orderGoodsVo.put("picUrl", orderGoods.getPicUrl());
                orderGoodsVo.put("specifications", orderGoods.getSpecifications());
                orderGoodsVo.put("price", orderGoods.getPrice());
                orderGoodsVoList.add(orderGoodsVo);
            }
            orderVo.put("goodsList", orderGoodsVoList);

            orderVoList.add(orderVo);
        }

        return ResponseUtil.okList(orderVoList, orderList);
    }

    /**
     * 订单详情
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    public Object detail(Integer userId, Integer orderId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        // 订单信息
        JindouyunOrder order = orderService.findById(orderId);
        if (null == order) {
            return ResponseUtil.fail(ORDER_UNKNOWN, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.fail(ORDER_INVALID, "不是当前用户的订单");
        }
        Map<String, Object> orderVo = new HashMap<String, Object>();
        orderVo.put("id", order.getId());
        orderVo.put("orderSn", order.getOrderSn());
        orderVo.put("message", order.getMessage());
        orderVo.put("addTime", order.getAddTime());
        orderVo.put("consignee", order.getConsignee());
        orderVo.put("mobile", order.getMobile());
        orderVo.put("address", order.getAddress());
        orderVo.put("goodsPrice", order.getGoodsPrice());
        orderVo.put("couponPrice", order.getCouponPrice());
        orderVo.put("freightPrice", order.getFreightPrice());
        orderVo.put("actualPrice", order.getActualPrice());
        orderVo.put("orderStatusText", OrderUtil.orderStatusText(order));
        orderVo.put("handleOption", OrderUtil.build(order));
//        orderVo.put("expCode", order.getShipChannel());
//        orderVo.put("expName", expressService.getVendorName(order.getShipChannel()));
//        orderVo.put("expNo", order.getShipSn());

        List<JindouyunOrderGoods> orderGoodsList = orderGoodsService.queryByOid(order.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("orderInfo", orderVo);
        result.put("orderGoods", orderGoodsList);

//        // 订单状态为已发货且物流信息不为空
//        //"YTO", "800669400640887922"
//        if (order.getOrderStatus().equals(OrderUtil.STATUS_SHIP)) {
//            ExpressInfo ei = expressService.getExpressInfo(order.getShipChannel(), order.getShipSn());
//            if(ei == null){
//                result.put("expressInfo", new ArrayList<>());
//            }
//            else {
//                result.put("expressInfo", ei);
//            }
//        }
//        else{
//            result.put("expressInfo", new ArrayList<>());
//        }

        return ResponseUtil.ok(result);

    }

    /**
     * 提交订单
     * <p>
     * 1. 创建订单表项和订单商品表项;
     * 2. 购物车清空;
     * 3. 优惠券设置已用;
     * 4. 商品货品库存减少;
     * 5. 如果是团购商品，则创建团购活动表项。
     *
     * @param userId 用户ID
     * @param body   订单信息，{ cartIds：[xx,x], addressId: xxx, couponId: xxx, message: xxx, grouponRulesId: xxx,  grouponLinkId: xxx}
     * @return 提交订单操作结果
     */
    @Transactional
    public Object submit(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        if (body == null) {
            return ResponseUtil.badArgument();
        }
//        Integer cartId = JacksonUtil.parseInteger(body, "cartId");
        List<Integer> cartIds = JacksonUtil.parseIntegerList(body, "cartIds");

        Integer addressId = JacksonUtil.parseInteger(body, "addressId");
        Integer couponId = JacksonUtil.parseInteger(body, "couponId");
        Integer userCouponId = JacksonUtil.parseInteger(body, "userCouponId");
        String message = JacksonUtil.parseString(body, "message");
//        Integer grouponRulesId = JacksonUtil.parseInteger(body, "grouponRulesId");
//        Integer grouponLinkId = JacksonUtil.parseInteger(body, "grouponLinkId");

//        //如果是团购项目,验证活动是否有效
//        if (grouponRulesId != null && grouponRulesId > 0) {
//            JindouyunGrouponRules rules = grouponRulesService.findById(grouponRulesId);
//            //找不到记录
//            if (rules == null) {
//                return ResponseUtil.badArgument();
//            }
//            //团购规则已经过期
//            if (rules.getStatus().equals(GrouponConstant.RULE_STATUS_DOWN_EXPIRE)) {
//                return ResponseUtil.fail(GROUPON_EXPIRED, "团购已过期!");
//            }
//            //团购规则已经下线
//            if (rules.getStatus().equals(GrouponConstant.RULE_STATUS_DOWN_ADMIN)) {
//                return ResponseUtil.fail(GROUPON_OFFLINE, "团购已下线!");
//            }
//
//            if (grouponLinkId != null && grouponLinkId > 0) {
//                //团购人数已满
//                if(grouponService.countGroupon(grouponLinkId) >= (rules.getDiscountMember() - 1)){
//                    return ResponseUtil.fail(GROUPON_FULL, "团购活动人数已满!");
//                }
//                // NOTE
//                // 这里业务方面允许用户多次开团，以及多次参团，
//                // 但是会限制以下两点：
//                // （1）不允许参加已经加入的团购
//                if(grouponService.hasJoin(userId, grouponLinkId)){
//                    return ResponseUtil.fail(GROUPON_JOIN, "团购活动已经参加!");
//                }
//                // （2）不允许参加自己开团的团购
//                JindouyunGroupon groupon = grouponService.queryById(grouponLinkId);
//                if(groupon.getCreatorUserId().equals(userId)){
//                    return ResponseUtil.fail(GROUPON_JOIN, "团购活动已经参加!");
//                }
//            }
//        }

//        if (cartId == null || addressId == null || couponId == null) {
//            return ResponseUtil.badArgument();
//        }

        if (cartIds == null || addressId == null || couponId == null) {
            return ResponseUtil.badArgument();
        }

        // 收货地址
        JindouyunAddress checkedAddress = addressService.query(userId, addressId);
        if (checkedAddress == null) {
            return ResponseUtil.badArgument();
        }

        // 团购优惠
//        BigDecimal grouponPrice = new BigDecimal(0);
//        JindouyunGrouponRules grouponRules = grouponRulesService.findById(grouponRulesId);
//        if (grouponRules != null) {
//            grouponPrice = grouponRules.getDiscount();
//        }

        // 货品价格
        BigDecimal checkedGoodsPrice = new BigDecimal(0);
        List<JindouyunCart> checkedGoodsList = new ArrayList<>();
        for (Integer cartId : cartIds) {
            if (cartId.equals(0)) {
                checkedGoodsList = cartService.queryByUidAndChecked(userId);
            } else {
                JindouyunCart cart = cartService.findById(cartId);
//                checkedGoodsList = new ArrayList<>();
                checkedGoodsList.add(cart);
            }
        }
        System.out.println(checkedGoodsList);

        if (checkedGoodsList.size() == 0) {
            return ResponseUtil.badArgumentValue();
        }

        for (JindouyunCart checkGoods : checkedGoodsList) {
//            //  只有当团购规格商品ID符合才进行团购优惠
//            if (grouponRules != null && grouponRules.getGoodsId().equals(checkGoods.getGoodsId())) {
//                checkedGoodsPrice = checkedGoodsPrice.add(checkGoods.getPrice().subtract(grouponPrice).multiply(new BigDecimal(checkGoods.getNumber())));
//            } else {
            System.out.println(checkedGoodsPrice.add(checkGoods.getPrice().multiply(new BigDecimal(checkGoods.getNumber()))));
            checkedGoodsPrice = checkedGoodsPrice.add(checkGoods.getPrice().multiply(new BigDecimal(checkGoods.getNumber())));
//            }
        }

        // 获取可用的优惠券信息
        // 使用优惠券减免的金额
        BigDecimal couponPrice = new BigDecimal(0);
        // 如果couponId=0则没有优惠券，couponId=-1则不使用优惠券
        if (couponId != 0 && couponId != -1) {
            JindouyunCoupon coupon = couponVerifyService.checkCoupon(userId, couponId, userCouponId, checkedGoodsPrice);
            if (coupon == null) {
                return ResponseUtil.badArgumentValue();
            }
            couponPrice = coupon.getDiscount();
        }


//        // 根据订单商品总价计算运费，满足条件（例如88元）则免运费，否则需要支付运费（例如8元）；
//        BigDecimal freightPrice = new BigDecimal(0);
//        if (checkedGoodsPrice.compareTo(SystemConfig.getFreightLimit()) < 0) {
//            freightPrice = SystemConfig.getFreight();
//        }

        // 根据订单商品总价计算运费，满10则1运费，否则2元；
        BigDecimal freightPrice = new BigDecimal(1.00);
        if (checkedGoodsPrice.compareTo(SystemConfig.getFreightLimit()) < 0) {
            freightPrice = SystemConfig.getFreight();
        }

        // 可以使用的其他钱，例如用户积分
        BigDecimal integralPrice = new BigDecimal(0);

        // 订单费用
        BigDecimal orderTotalPrice = checkedGoodsPrice.add(freightPrice).subtract(couponPrice).max(new BigDecimal(0));
        // 最终支付费用
        BigDecimal actualPrice = orderTotalPrice.subtract(integralPrice);

        Integer orderId = null;
        JindouyunOrder order = null;
        // 订单
        order = new JindouyunOrder();
        order.setUserId(userId);
        order.setOrderSn(orderService.generateOrderSn(userId));
        order.setOrderStatus(OrderUtil.STATUS_CREATE);
        order.setConsignee(checkedAddress.getName());
        order.setMobile(checkedAddress.getTel());
        order.setMessage(message);
        order.setBuilding(checkedAddress.getBuilding());
//        String detailedAddress = checkedAddress.getProvince() + checkedAddress.getCity() + checkedAddress.getCounty() + " " + checkedAddress.getAddressDetail();
        String detailedAddress = checkedAddress.getAddressDetail();
        order.setAddress(detailedAddress);
        order.setGoodsPrice(checkedGoodsPrice);
        order.setFreightPrice(freightPrice);
        order.setCouponPrice(couponPrice);
//        order.setIntegralPrice(integralPrice);
        order.setOrderPrice(orderTotalPrice);
        order.setActualPrice(actualPrice);

//        // 有团购
//        if (grouponRules != null) {
//            order.setGrouponPrice(grouponPrice);    //  团购价格
//        } else {
//            order.setGrouponPrice(new BigDecimal(0));    //  团购价格
//        }

        // 添加订单表项
        orderService.add(order);
        orderId = order.getId();
        System.out.println("~~~~~~~~~" + orderId);

        //订单中的商品集合
        List<Integer> productIds = new ArrayList<>();

        //订单中各商家的商品
        Map<Integer, List<JindouyunGoodsProduct>> map = new HashMap<>();
        List<JindouyunGoodsProduct> goodsProductList = new ArrayList<>();


        // 添加订单商品表项
        for (JindouyunCart cartGoods : checkedGoodsList) {
            // 订单商品
            JindouyunOrderGoods orderGoods = new JindouyunOrderGoods();
            orderGoods.setOrderId(order.getId());
            JindouyunGoods goods = goodsService.findByGoodsSn(cartGoods.getGoodsSn());
            orderGoods.setBrandId(goods.getBrandId());
            orderGoods.setGoodsId(cartGoods.getGoodsId());
            orderGoods.setGoodsSn(cartGoods.getGoodsSn());
            orderGoods.setProductId(cartGoods.getProductId());
            orderGoods.setGoodsName(cartGoods.getGoodsName());
            orderGoods.setPicUrl(cartGoods.getPicUrl());
            orderGoods.setPrice(cartGoods.getPrice());
            orderGoods.setNumber(cartGoods.getNumber());
            orderGoods.setSpecifications(cartGoods.getSpecifications());
            orderGoods.setAddTime(LocalDateTime.now());
            orderGoodsService.add(orderGoods);

            //订单中的商品集合
            productIds.add(cartGoods.getProductId());

            //订单中各商家的商品
            goodsProductList.add(productService.findById(cartGoods.getProductId()));
            map.put(goods.getBrandId(), goodsProductList);
        }

        //修改商家总销售量，总销量金额
        for (Integer brandId : map.keySet()) {
            List<JindouyunGoodsProduct> productList = map.get(brandId);
            BigDecimal productListSum = new BigDecimal(0);
            for (JindouyunGoodsProduct goodsProduct : productList) {
                productListSum.add(goodsProduct.getPrice());
            }
            brandService.increaseTotalTurnover(brandId,productListSum);
            //写入分单
            JindouyunOrderSplit orderSplit = new JindouyunOrderSplit();
            orderSplit.setUserId(userId);
            orderSplit.setBrandId(brandId);
            orderSplit.setOrderId(orderId);
            orderSplit.setOrderSn(orderService.generateOrderSn(userId));
            orderSplit.setOrderStatus(OrderUtil.STATUS_CREATE);
            orderSplit.setConsignee(checkedAddress.getName());
            orderSplit.setMobile(checkedAddress.getTel());
            orderSplit.setMessage(message);
            orderSplit.setBuilding(checkedAddress.getBuilding());
            orderSplit.setGoodsPrice(productListSum);
            orderSplitService.add(orderSplit);
        }



//        // 删除购物车里面的商品信息
//        cartService.clearGoods(userId);
        //删除购物车对应商品
        cartService.delete(productIds, userId);


        // 商品货品数量减少
        for (JindouyunCart checkGoods : checkedGoodsList) {
            Integer productId = checkGoods.getProductId();
            JindouyunGoodsProduct product = productService.findById(productId);

            int remainNumber = product.getNumber() - checkGoods.getNumber();
            if (remainNumber < 0) {
                throw new RuntimeException("下单的商品货品数量大于库存量");
            }
            if (productService.reduceStock(productId, checkGoods.getNumber()) == 0) {
                throw new RuntimeException("商品货品库存减少失败");
            }
        }

        // 如果使用了优惠券，设置优惠券使用状态
        if (couponId != 0 && couponId != -1) {
            JindouyunCouponUser couponUser = couponUserService.findById(userCouponId);
            couponUser.setStatus(CouponUserConstant.STATUS_USED);
            couponUser.setUsedTime(LocalDateTime.now());
            couponUser.setOrderId(orderId);
            couponUserService.update(couponUser);
        }

//        //如果是团购项目，添加团购信息
//        if (grouponRulesId != null && grouponRulesId > 0) {
//            JindouyunGroupon groupon = new JindouyunGroupon();
//            groupon.setOrderId(orderId);
//            groupon.setStatus(GrouponConstant.STATUS_NONE);
//            groupon.setUserId(userId);
//            groupon.setRulesId(grouponRulesId);
//
//            //参与者
//            if (grouponLinkId != null && grouponLinkId > 0) {
//                //参与的团购记录
//                JindouyunGroupon baseGroupon = grouponService.queryById(grouponLinkId);
//                groupon.setCreatorUserId(baseGroupon.getCreatorUserId());
//                groupon.setGrouponId(grouponLinkId);
//                groupon.setShareUrl(baseGroupon.getShareUrl());
//                grouponService.createGroupon(groupon);
//            } else {
//                groupon.setCreatorUserId(userId);
//                groupon.setCreatorUserTime(LocalDateTime.now());
//                groupon.setGrouponId(0);
//                grouponService.createGroupon(groupon);
//                grouponLinkId = groupon.getId();
//            }
//        }

        // 订单支付超期任务
        taskService.addTask(new OrderUnpaidTask(orderId));

        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
//        if (grouponRulesId != null && grouponRulesId > 0) {
//            data.put("grouponLinkId", grouponLinkId);
//        }
//        else {
//            data.put("grouponLinkId", 0);
//        }


        return ResponseUtil.ok(data);
    }


    /**
     * 搜索订单
     *
     * @param userId
     * @param keyword
     * @return
     */
    public Object find(Integer userId, String keyword) {
        JindouyunOrderGoodsExample example = new JindouyunOrderGoodsExample();
        JindouyunOrderGoodsExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(keyword)) {
            criteria.andGoodsNameLike("%" + keyword + "%");
        }
        criteria.andDeletedEqualTo(false);

        List<JindouyunOrderGoods> jindouyunOrderGoods = orderGoodsMapper.selectByExampleSelective(example);
        System.out.println(jindouyunOrderGoods);

        List<Map<String, Object>> mapList = new ArrayList<>();

        for (JindouyunOrderGoods orderGoods : jindouyunOrderGoods) {
            JindouyunOrder jindouyunOrder = orderService.findById(orderGoods.getOrderId());
            if (jindouyunOrder.getUserId().intValue() == userId.intValue()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", jindouyunOrder.getId());
                map.put("orderSn", jindouyunOrder.getOrderSn());
                map.put("actualPrice", jindouyunOrder.getActualPrice());
                map.put("orderStatusText", OrderUtil.orderStatusText(jindouyunOrder));
                map.put("goodsList", orderGoodsService.queryByOid(jindouyunOrder.getId()));
                mapList.add(map);
            }
        }
//        List<Map<String, Object>> maps = orderService.find(userId, keyword);
//        return ResponseUtil.okList(maps);
        return ResponseUtil.okList(mapList);
    }

    /**
     * 取消订单
     * <p>
     * 1. 检测当前订单是否能够取消；
     * 2. 设置订单取消状态；
     * 3. 商品货品库存恢复；
     * 4. TODO 优惠券；
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 取消订单操作结果
     */
//    @Transactional
//    public Object cancel(Integer userId, String body) {
//        if (userId == null) {
//            return ResponseUtil.unlogin();
//        }
//        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
//        if (orderId == null) {
//            return ResponseUtil.badArgument();
//        }
//
//        JindouyunOrder order = orderService.findById(orderId);
//        if (order == null) {
//            return ResponseUtil.badArgumentValue();
//        }
//        if (!order.getUserId().equals(userId)) {
//            return ResponseUtil.badArgumentValue();
//        }
//
//        LocalDateTime preUpdateTime = order.getUpdateTime();
//
//        // 检测是否能够取消
//        OrderHandleOption handleOption = OrderUtil.build(order);
//        if (!handleOption.isCancel()) {
//            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "订单不能取消");
//        }
//
//        // 设置订单已取消状态
//        order.setOrderStatus(OrderUtil.STATUS_CANCEL);
//        order.setEndTime(LocalDateTime.now());
//        if (orderService.updateWithOptimisticLocker(order) == 0) {
//            throw new RuntimeException("更新数据已失效");
//        }
//
//        // 商品货品数量增加
//        List<JindouyunOrderGoods> orderGoodsList = orderGoodsService.queryByOid(orderId);
//        for (JindouyunOrderGoods orderGoods : orderGoodsList) {
//            Integer productId = orderGoods.getProductId();
//            Short number = orderGoods.getNumber();
//            if (productService.addStock(productId, number) == 0) {
//                throw new RuntimeException("商品货品库存增加失败");
//            }
//        }
//
//        return ResponseUtil.ok();
//    }

    /**
     * 付款订单的预支付会话标识
     * <p>
     * 1. 检测当前订单是否能够付款
     * 2. 微信商户平台返回支付订单ID
     * 3. 设置订单付款状态
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 支付订单ID
     */
    @Transactional
    public Object prepay(Integer userId, String body, HttpServletRequest request) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }

        JindouyunOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgumentValue();
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

        // 检测是否能够取消
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isPay()) {
            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "订单不能支付");
        }

        JindouyunUser user = userService.findById(userId);
        String openid = user.getWeixinOpenid();
        if (openid == null) {
            return ResponseUtil.fail(AUTH_OPENID_UNACCESS, "订单不能支付");
        }
        WxPayMpOrderResult result = null;
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setOutTradeNo(order.getOrderSn());
            orderRequest.setOpenid(openid);
            orderRequest.setBody("订单：" + order.getOrderSn());
            // 元转成分
            int fee = 0;
            BigDecimal actualPrice = order.getActualPrice();
            fee = actualPrice.multiply(new BigDecimal(100)).intValue();
            orderRequest.setTotalFee(fee);
            orderRequest.setSpbillCreateIp(IpUtil.getIpAddr(request));

            result = wxPayService.createOrder(orderRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.fail(ORDER_PAY_FAIL, "订单不能支付");
        }

        if (orderService.updateWithOptimisticLocker(order) == 0) {
            return ResponseUtil.updatedDateExpired();
        }
        return ResponseUtil.ok(result);
    }

    /**
     * 微信H5支付
     *
     * @param userId
     * @param body
     * @param request
     * @return
     */
    @Transactional
    public Object h5pay(Integer userId, String body, HttpServletRequest request) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }

        JindouyunOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgumentValue();
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

        // 检测是否能够取消
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isPay()) {
            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "订单不能支付");
        }

        WxPayMwebOrderResult result = null;
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setOutTradeNo(order.getOrderSn());
            orderRequest.setTradeType("MWEB");
            orderRequest.setBody("订单：" + order.getOrderSn());
            // 元转成分
            int fee = 0;
            BigDecimal actualPrice = order.getActualPrice();
            fee = actualPrice.multiply(new BigDecimal(100)).intValue();
            orderRequest.setTotalFee(fee);
            orderRequest.setSpbillCreateIp(IpUtil.getIpAddr(request));

            result = wxPayService.createOrder(orderRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseUtil.ok(result);
    }

    /**
     * 微信付款成功或失败回调接口
     * <p>
     * 1. 检测当前订单是否是付款状态;
     * 2. 设置订单付款成功状态相关信息;
     * 3. 响应微信商户平台.
     *
     * @param request  请求内容
     * @param response 响应内容
     * @return 操作结果
     */
    @Transactional
    public Object payNotify(HttpServletRequest request, HttpServletResponse response) {
        String xmlResult = null;
        try {
            xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        } catch (IOException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        WxPayOrderNotifyResult result = null;
        try {
            result = wxPayService.parseOrderNotifyResult(xmlResult);

            if (!WxPayConstants.ResultCode.SUCCESS.equals(result.getResultCode())) {
                logger.error(xmlResult);
                throw new WxPayException("微信通知支付失败！");
            }
            if (!WxPayConstants.ResultCode.SUCCESS.equals(result.getReturnCode())) {
                logger.error(xmlResult);
                throw new WxPayException("微信通知支付失败！");
            }
        } catch (WxPayException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        logger.info("处理腾讯支付平台的订单支付");
        logger.info(result);

        String orderSn = result.getOutTradeNo();
        String payId = result.getTransactionId();

        // 分转化成元
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());
        JindouyunOrder order = orderService.findBySn(orderSn);
        if (order == null) {
            return WxPayNotifyResponse.fail("订单不存在 sn=" + orderSn);
        }

        // 检查这个订单是否已经处理过
        if (OrderUtil.hasPayed(order)) {
            return WxPayNotifyResponse.success("订单已经处理成功!");
        }

        // 检查支付订单金额
        if (!totalFee.equals(order.getActualPrice().toString())) {
            return WxPayNotifyResponse.fail(order.getOrderSn() + " : 支付金额不符合 totalFee=" + totalFee);
        }

        order.setPayId(payId);
        order.setPayTime(LocalDateTime.now());
//        order.setOrderStatus(OrderUtil.STATUS_PAY);
        order.setOrderStatus(OrderUtil.STATUS_SHIP);
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            return WxPayNotifyResponse.fail("更新数据已失效");
        }

//        //  支付成功，有团购信息，更新团购信息
//        JindouyunGroupon groupon = grouponService.queryByOrderId(order.getId());
//        if (groupon != null) {
//            JindouyunGrouponRules grouponRules = grouponRulesService.findById(groupon.getRulesId());
//
//            //仅当发起者才创建分享图片
//            if (groupon.getGrouponId() == 0) {
//                String url = qCodeService.createGrouponShareImage(grouponRules.getGoodsName(), grouponRules.getPicUrl(), groupon);
//                groupon.setShareUrl(url);
//            }
//            groupon.setStatus(GrouponConstant.STATUS_ON);
//            if (grouponService.updateById(groupon) == 0) {
//                return WxPayNotifyResponse.fail("更新数据已失效");
//            }
//
//
//            List<JindouyunGroupon> grouponList = grouponService.queryJoinRecord(groupon.getGrouponId());
//            if (groupon.getGrouponId() != 0 && (grouponList.size() >= grouponRules.getDiscountMember() - 1)) {
//                for (JindouyunGroupon grouponActivity : grouponList) {
//                    grouponActivity.setStatus(GrouponConstant.STATUS_SUCCEED);
//                    grouponService.updateById(grouponActivity);
//                }
//
//                JindouyunGroupon grouponSource = grouponService.queryById(groupon.getGrouponId());
//                grouponSource.setStatus(GrouponConstant.STATUS_SUCCEED);
//                grouponService.updateById(grouponSource);
//            }
//        }

        //发送邮件和短信通知，这里采用异步发送
        // 订单支付成功以后，会发送短信给用户，以及发送邮件给管理员
        notifyService.notifyMail("新订单通知", order.toString());
        // 这里微信的短信平台对参数长度有限制，所以将订单号只截取后6位
        notifyService.notifySmsTemplateSync(order.getMobile(), NotifyType.PAY_SUCCEED, new String[]{orderSn.substring(8, 14)});

        // 请依据自己的模版消息配置更改参数
        String[] parms = new String[]{
                order.getOrderSn(),
                order.getOrderPrice().toString(),
                DateTimeUtil.getDateTimeDisplayString(order.getAddTime()),
                order.getConsignee(),
                order.getMobile(),
                order.getAddress()
        };

        // 取消订单超时未支付任务
        taskService.removeTask(new OrderUnpaidTask(order.getId()));

        return WxPayNotifyResponse.success("处理成功!");
    }

    /**
     * 订单申请退款
     * <p>
     * 1. 检测当前订单是否能够退款；
     * 2. 设置订单申请退款状态。
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
//    public Object refund(Integer userId, String body) {
//        if (userId == null) {
//            return ResponseUtil.unlogin();
//        }
//        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
//        if (orderId == null) {
//            return ResponseUtil.badArgument();
//        }
//
//        JindouyunOrder order = orderService.findById(orderId);
//        if (order == null) {
//            return ResponseUtil.badArgument();
//        }
//        if (!order.getUserId().equals(userId)) {
//            return ResponseUtil.badArgumentValue();
//        }
//
//        OrderHandleOption handleOption = OrderUtil.build(order);
//        if (!handleOption.isRefund()) {
//            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "订单不能取消");
//        }
//
//        // 设置订单申请退款状态
//        order.setOrderStatus(OrderUtil.STATUS_REFUND);
//        if (orderService.updateWithOptimisticLocker(order) == 0) {
//            return ResponseUtil.updatedDateExpired();
//        }
//
//        //TODO 发送邮件和短信通知，这里采用异步发送
//        // 有用户申请退款，邮件通知运营人员
//        notifyService.notifyMail("退款申请", order.toString());
//
//        return ResponseUtil.ok();
//    }

    /**
     * 确认收货
     * <p>
     * 1. 检测当前订单是否能够确认收货；
     * 2. 设置订单确认收货状态。
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @Transactional
    public Object confirm(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }

        JindouyunOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

//        OrderHandleOption handleOption = OrderUtil.build(order);
//        if (!handleOption.isConfirm()) {
//            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "订单不能确认收货");
//        }

        Short comments = orderGoodsService.getComments(orderId);
        order.setComments(comments);

        order.setOrderStatus(OrderUtil.STATUS_CONFIRM);
        order.setConfirmTime(LocalDateTime.now());
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            return ResponseUtil.updatedDateExpired();
        }
        return ResponseUtil.ok();
    }

    /**
     * 删除订单
     * <p>
     * 1. 检测当前订单是否可以删除；
     * 2. 删除订单。
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    public Object delete(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }

        JindouyunOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isDelete()) {
            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "订单不能删除");
        }

        // 订单order_status没有字段用于标识删除
        // 而是存在专门的delete字段表示是否删除
        orderService.deleteById(orderId);

        return ResponseUtil.ok();
    }

    /**
     * 待评价订单商品信息
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @param goodsId 商品ID
     * @return 待评价订单商品信息
     */
    public Object goods(Integer userId, Integer orderId, Integer goodsId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        List<JindouyunOrderGoods> orderGoodsList = orderGoodsService.findByOidAndGid(orderId, goodsId);
        int size = orderGoodsList.size();

        Assert.state(size < 2, "存在多个符合条件的订单商品");

        if (size == 0) {
            return ResponseUtil.badArgumentValue();
        }

        JindouyunOrderGoods orderGoods = orderGoodsList.get(0);
        return ResponseUtil.ok(orderGoods);
    }

    /**
     * 评价订单商品
     * <p>
     * 确认商品收货或者系统自动确认商品收货后7天内可以评价，过期不能评价。
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @Transactional
    public Object comment(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        Integer orderGoodsId = JacksonUtil.parseInteger(body, "orderGoodsId");
        if (orderGoodsId == null) {
            return ResponseUtil.badArgument();
        }
        JindouyunOrderGoods orderGoods = orderGoodsService.findById(orderGoodsId);
        if (orderGoods == null) {
            return ResponseUtil.badArgumentValue();
        }
        Integer orderId = orderGoods.getOrderId();
        JindouyunOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgumentValue();
        }
        Short orderStatus = order.getOrderStatus();
        if (!OrderUtil.isConfirmStatus(order) && !OrderUtil.isAutoConfirmStatus(order)) {
            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "当前商品不能评价");
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.fail(ORDER_INVALID, "当前商品不属于用户");
        }
        Integer commentId = orderGoods.getComment();
        if (commentId == -1) {
            return ResponseUtil.fail(ORDER_COMMENT_EXPIRED, "当前商品评价时间已经过期");
        }
        if (commentId != 0) {
            return ResponseUtil.fail(ORDER_COMMENTED, "订单商品已评价");
        }

        String content = JacksonUtil.parseString(body, "content");
        Integer star = JacksonUtil.parseInteger(body, "star");
        if (star == null || star < 0 || star > 5) {
            return ResponseUtil.badArgumentValue();
        }
        Boolean hasPicture = JacksonUtil.parseBoolean(body, "hasPicture");
        List<String> picUrls = JacksonUtil.parseStringList(body, "picUrls");
        if (hasPicture == null || !hasPicture) {
            picUrls = new ArrayList<>(0);
        }

        // 1. 创建评价
        JindouyunComment comment = new JindouyunComment();
        comment.setUserId(userId);
        comment.setType((byte) 0);
        comment.setValueId(orderGoods.getGoodsId());
        comment.setStar(star.shortValue());
        comment.setContent(content);
        comment.setHasPicture(hasPicture);
        comment.setPicUrls(picUrls.toArray(new String[]{}));
        commentService.save(comment);

        // 2. 更新订单商品的评价列表
        orderGoods.setComment(comment.getId());
        orderGoodsService.updateById(orderGoods);

        // 3. 更新订单中未评价的订单商品可评价数量
        Short commentCount = order.getComments();
        if (commentCount > 0) {
            commentCount--;
        }
        order.setComments(commentCount);
        orderService.updateWithOptimisticLocker(order);

        return ResponseUtil.ok();
    }
}
