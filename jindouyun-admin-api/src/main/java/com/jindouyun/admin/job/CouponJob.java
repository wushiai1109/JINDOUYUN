package com.jindouyun.admin.job;

import com.jindouyun.db.domain.JindouyunCoupon;
import com.jindouyun.db.domain.JindouyunCouponUser;
import com.jindouyun.db.service.JindouyunCouponService;
import com.jindouyun.db.service.JindouyunCouponUserService;
import com.jindouyun.common.constant.CouponConstant;
import com.jindouyun.common.constant.CouponUserConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 检测优惠券过期情况
 */
@Component
public class CouponJob {
    private final Log logger = LogFactory.getLog(CouponJob.class);

    @Autowired
    private JindouyunCouponService couponService;
    @Autowired
    private JindouyunCouponUserService couponUserService;

    /**
     * 每隔一个小时检查
     * TODO
     * 注意，因为是相隔一分钟检查，因此导致优惠券真正超时时间可能比设定时间延迟1分钟
     */
    @Scheduled(fixedDelay = 60 * 1000)
    public void checkCouponExpired() {
        logger.info("系统开启任务检查优惠券是否已经过期");

        List<JindouyunCoupon> couponList = couponService.queryExpired();
        for (JindouyunCoupon coupon : couponList) {
            coupon.setStatus(CouponConstant.STATUS_EXPIRED);
            couponService.updateById(coupon);
        }

        List<JindouyunCouponUser> couponUserList = couponUserService.queryExpired();
        for (JindouyunCouponUser couponUser : couponUserList) {
            couponUser.setStatus(CouponUserConstant.STATUS_EXPIRED);
            couponUserService.update(couponUser);
        }
    }

}
