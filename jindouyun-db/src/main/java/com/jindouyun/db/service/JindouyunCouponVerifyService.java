package com.jindouyun.db.service;

import com.jindouyun.db.domain.JindouyunCoupon;
import com.jindouyun.db.domain.JindouyunCouponUser;
import com.jindouyun.common.constant.CouponConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName JindouyunCouponVerifyService
 * @Description
 * @Author Bruce
 * @Date 2020/7/23 4:22 下午
 */
@Service
@Transactional
public class JindouyunCouponVerifyService {

    @Autowired
    private JindouyunCouponUserService couponUserService;

    @Autowired
    private JindouyunCouponService couponService;

    /**
     * 检测优惠券是否适合
     *
     * @param userId
     * @param couponId
     * @param checkedGoodsPrice
     * @return
     */
    public JindouyunCoupon checkCoupon(Integer userId, Integer couponId, Integer userCouponId, BigDecimal checkedGoodsPrice, Integer brandId) {
        JindouyunCoupon coupon = couponService.findById(couponId);
        if (coupon == null) {
            return null;
        }

        JindouyunCouponUser couponUser = couponUserService.findById(userCouponId);
        if (couponUser == null) {
            couponUser = couponUserService.queryOne(userId, couponId);
        } else if (!couponId.equals(couponUser.getCouponId())) {
            return null;
        }

        if (couponUser == null) {
            return null;
        }

        //检查是否是通用券或者是否是此商家的优惠券
        if (brandId != 0 && brandId != 1) {
            System.out.println("brand"+brandId.intValue());
            System.out.println("~~~~"+couponId);
            System.out.println("!!!"+coupon.getId());
            System.out.println("coupon.getType()"+(coupon.getType()).intValue());
            System.out.println(brandId.intValue() == coupon.getType().intValue());
            boolean flag = brandId.intValue() != coupon.getType().intValue();
            System.out.println("flag:"+flag);
            if (flag) {
                return null;
            }
        }

        // 检查是否超期
        Short timeType = coupon.getTimeType();
        Short days = coupon.getDays();
        LocalDateTime now = LocalDateTime.now();
        if (timeType.equals(CouponConstant.TIME_TYPE_TIME)) {
            if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
                return null;
            }
        } else if (timeType.equals(CouponConstant.TIME_TYPE_DAYS)) {
            LocalDateTime expired = couponUser.getAddTime().plusDays(days);
            if (now.isAfter(expired)) {
                return null;
            }
        } else {
            return null;
        }

//        // 检测商品是否符合
//        Short goodType = coupon.getGoodsType();
//        if (!goodType.equals(CouponConstant.GOODS_TYPE_ALL)) {
//            return null;
//        }

        // 检测订单状态
        Short status = coupon.getStatus();
        if (!status.equals(CouponConstant.STATUS_NORMAL)) {
            return null;
        }

        // 检测是否满足最低消费
        if (checkedGoodsPrice.compareTo(coupon.getMin()) < 0) {
            return null;
        }

        return coupon;
    }

}
