package com.jindouyun.wx.controller;

import com.jindouyun.core.util.JacksonUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.core.validator.Order;
import com.jindouyun.core.validator.Sort;
import com.jindouyun.db.domain.JindouyunCart;
import com.jindouyun.db.domain.JindouyunCoupon;
import com.jindouyun.db.domain.JindouyunCouponUser;
import com.jindouyun.db.util.CouponConstant;
import com.jindouyun.wx.annotation.LoginUser;
import com.jindouyun.wx.domain.CouponVo;
import com.jindouyun.wx.service.CouponVerifyService;
import com.jindouyun.wx.service.JindouyunCartService;
import com.jindouyun.wx.service.JindouyunCouponService;
import com.jindouyun.wx.service.JindouyunCouponUserService;
import com.jindouyun.wx.util.WxResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName WxCouponController
 * @Description 优惠券服务
 * @Author Bruce
 * @Date 2020/7/24 12:41 下午
 */
@RestController
@RequestMapping("/wx/coupon")
@CrossOrigin(origins = "*",maxAge = 3600)
public class WxCouponController {

    @Autowired
    private JindouyunCouponService couponService;

    @Autowired
    private JindouyunCouponUserService couponUserService;

    @Autowired
    private JindouyunCartService cartService;

    @Autowired
    private CouponVerifyService couponVerifyService;

    /**
     * 优惠券列表
     *
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @GetMapping("list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {

        List<JindouyunCoupon> couponList = couponService.queryList(page, limit, sort, order);
        return ResponseUtil.okList(couponList);
    }

    /**
     * 个人优惠券列表
     *
     * @param userId
     * @param status
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @GetMapping("mylist")
    public Object mylist(@LoginUser Integer userId,
                         Short status,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer limit,
                         @Sort @RequestParam(defaultValue = "add_time") String sort,
                         @Order @RequestParam(defaultValue = "desc") String order) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        List<JindouyunCouponUser> couponUserList = couponUserService.queryList(userId, null, status, page, limit, sort, order);
        List<CouponVo> couponVoList = change(couponUserList);
        return ResponseUtil.okList(couponVoList, couponUserList);
    }

    private List<CouponVo> change(List<JindouyunCouponUser> couponList) {
        List<CouponVo> couponVoList = new ArrayList<>(couponList.size());
        for(JindouyunCouponUser couponUser : couponList){
            Integer couponId = couponUser.getCouponId();
            JindouyunCoupon coupon = couponService.findById(couponId);
            CouponVo couponVo = new CouponVo();
            couponVo.setId(couponUser.getId());
            couponVo.setCid(coupon.getId());
            couponVo.setName(coupon.getName());
            couponVo.setDesc(coupon.getDesc());
            couponVo.setMin(coupon.getMin());
            couponVo.setDiscount(coupon.getDiscount());
            couponVo.setStartTime(couponUser.getStartTime());
            couponVo.setEndTime(couponUser.getEndTime());

            couponVoList.add(couponVo);
        }

        return couponVoList;
    }


    /**
     * 当前购物车下单商品订单可用优惠券
     *
     * @param userId
     * @param cartId
     * @param grouponRulesId
     * @return
     */
    @GetMapping("selectlist")
    public Object selectlist(@LoginUser Integer userId, Integer cartId, Integer grouponRulesId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        // 团购优惠
        BigDecimal grouponPrice = new BigDecimal(0.00);
//        JindouyunGrouponRules grouponRules = grouponRulesService.findById(grouponRulesId);
//        if (grouponRules != null) {
//            grouponPrice = grouponRules.getDiscount();
//        }

        // 商品价格
        List<JindouyunCart> checkedGoodsList = null;
        if (cartId == null || cartId.equals(0)) {
            checkedGoodsList = cartService.queryByUidAndChecked(userId);
        } else {
            JindouyunCart cart = cartService.findById(cartId);
            if (cart == null) {
                return ResponseUtil.badArgumentValue();
            }
            checkedGoodsList = new ArrayList<>(1);
            checkedGoodsList.add(cart);
        }
        BigDecimal checkedGoodsPrice = new BigDecimal(0.00);
        for (JindouyunCart cart : checkedGoodsList) {
            //  只有当团购规格商品ID符合才进行团购优惠
//            if (grouponRules != null && grouponRules.getGoodsId().equals(cart.getGoodsId())) {
//                checkedGoodsPrice = checkedGoodsPrice.add(cart.getPrice().subtract(grouponPrice).multiply(new BigDecimal(cart.getNumber())));
//            } else {
                checkedGoodsPrice = checkedGoodsPrice.add(cart.getPrice().multiply(new BigDecimal(cart.getNumber())));
//            }
        }

        // 计算优惠券可用情况
        List<JindouyunCouponUser> couponUserList = couponUserService.queryAll(userId);
        List<CouponVo> couponVoList = change(couponUserList);
        for (CouponVo cv : couponVoList) {
            JindouyunCoupon coupon = couponVerifyService.checkCoupon(userId, cv.getCid(), cv.getId(), checkedGoodsPrice);
            cv.setAvailable(coupon != null);
        }

        return ResponseUtil.okList(couponVoList);
    }

    /**
     * 优惠券领取
     *
     * @param userId 用户ID
     * @param body 请求内容， { couponId: xxx }
     * @return 操作结果
     */
    @PostMapping("receive")
    public Object receive(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        Integer couponId = JacksonUtil.parseInteger(body, "couponId");
        if(couponId == null){
            return ResponseUtil.badArgument();
        }

        JindouyunCoupon coupon = couponService.findById(couponId);
        if(coupon == null){
            return ResponseUtil.badArgumentValue();
        }

        // 当前已领取数量和总数量比较
        Integer total = coupon.getTotal();
        Integer totalCoupons = couponUserService.countCoupon(couponId);
        if((total != 0) && (totalCoupons >= total)){
            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已领完");
        }

        // 当前用户已领取数量和用户限领数量比较
        Integer limit = coupon.getLimit().intValue();
        Integer userCounpons = couponUserService.countUserAndCoupon(userId, couponId);
        if((limit != 0) && (userCounpons >= limit)){
            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已经领取过");
        }

        // 优惠券分发类型
        // 例如注册赠券类型的优惠券不能领取
        Short type = coupon.getType();
        if(type.equals(CouponConstant.TYPE_REGISTER)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "新用户优惠券自动发送");
        }
        else if(type.equals(CouponConstant.TYPE_CODE)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券只能兑换");
        }
        else if(!type.equals(CouponConstant.TYPE_COMMON)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券类型不支持");
        }

        // 优惠券状态，已下架或者过期不能领取
        Short status = coupon.getStatus();
        if(status.equals(CouponConstant.STATUS_OUT)){
            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已领完");
        }
        else if(status.equals(CouponConstant.STATUS_EXPIRED)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券已经过期");
        }

        // 用户领券记录
        JindouyunCouponUser couponUser = new JindouyunCouponUser();
        couponUser.setCouponId(couponId);
        couponUser.setUserId(userId);
        Short timeType = coupon.getTimeType();
        if (timeType.equals(CouponConstant.TIME_TYPE_TIME)) {
            couponUser.setStartTime(coupon.getStartTime());
            couponUser.setEndTime(coupon.getEndTime());
        }
        else{
            LocalDateTime now = LocalDateTime.now();
            couponUser.setStartTime(now);
            couponUser.setEndTime(now.plusDays(coupon.getDays()));
        }
        couponUserService.add(couponUser);

        return ResponseUtil.ok();
    }

//    /**
//     * 优惠券兑换
//     *
//     * @param userId 用户ID
//     * @param body 请求内容， { code: xxx }
//     * @return 操作结果
//     */
//    @PostMapping("exchange")
//    public Object exchange(@LoginUser Integer userId, @RequestBody String body) {
//        if (userId == null) {
//            return ResponseUtil.unlogin();
//        }
//
//        String code = JacksonUtil.parseString(body, "code");
//        if(code == null){
//            return ResponseUtil.badArgument();
//        }
//
//        JindouyunCoupon coupon = couponService.findByCode(code);
//        if(coupon == null){
//            return ResponseUtil.fail(WxResponseCode.COUPON_CODE_INVALID, "优惠券不正确");
//        }
//        Integer couponId = coupon.getId();
//
//        // 当前已领取数量和总数量比较
//        Integer total = coupon.getTotal();
//        Integer totalCoupons = couponUserService.countCoupon(couponId);
//        if((total != 0) && (totalCoupons >= total)){
//            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已兑换");
//        }
//
//        // 当前用户已领取数量和用户限领数量比较
//        Integer limit = coupon.getLimit().intValue();
//        Integer userCounpons = couponUserService.countUserAndCoupon(userId, couponId);
//        if((limit != 0) && (userCounpons >= limit)){
//            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已兑换");
//        }
//
//        // 优惠券分发类型
//        // 例如注册赠券类型的优惠券不能领取
//        Short type = coupon.getType();
//        if(type.equals(CouponConstant.TYPE_REGISTER)){
//            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "新用户优惠券自动发送");
//        }
//        else if(type.equals(CouponConstant.TYPE_COMMON)){
//            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券只能领取，不能兑换");
//        }
//        else if(!type.equals(CouponConstant.TYPE_CODE)){
//            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券类型不支持");
//        }
//
//        // 优惠券状态，已下架或者过期不能领取
//        Short status = coupon.getStatus();
//        if(status.equals(CouponConstant.STATUS_OUT)){
//            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已兑换");
//        }
//        else if(status.equals(CouponConstant.STATUS_EXPIRED)){
//            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券已经过期");
//        }
//
//        // 用户领券记录
//        JindouyunCouponUser couponUser = new JindouyunCouponUser();
//        couponUser.setCouponId(couponId);
//        couponUser.setUserId(userId);
//        Short timeType = coupon.getTimeType();
//        if (timeType.equals(CouponConstant.TIME_TYPE_TIME)) {
//            couponUser.setStartTime(coupon.getStartTime());
//            couponUser.setEndTime(coupon.getEndTime());
//        }
//        else{
//            LocalDateTime now = LocalDateTime.now();
//            couponUser.setStartTime(now);
//            couponUser.setEndTime(now.plusDays(coupon.getDays()));
//        }
//        couponUserService.add(couponUser);
//
//        return ResponseUtil.ok();
//    }
    
}
