package com.jindouyun.db.service;

import com.jindouyun.db.domain.JindouyunCoupon;
import com.jindouyun.db.domain.JindouyunCouponUser;
import com.jindouyun.common.constant.CouponConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponAssignService {

    @Autowired
    private JindouyunCouponUserService couponUserService;
    @Autowired
    private JindouyunCouponService couponService;

    /**
     * 分发注册优惠券
     *
     * @param userId
     * @return
     */
    public void assignForRegister(Integer userId) {
        List<JindouyunCoupon> couponList = couponService.queryRegister();
        for(JindouyunCoupon coupon : couponList){
            Integer couponId = coupon.getId();

            Integer count = couponUserService.countUserAndCoupon(userId, couponId);
            if (count > 0) {
                continue;
            }

            Short limit = coupon.getLimit();
            while(limit > 0){
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

                limit--;
            }
        }

    }

}