package com.jindouyun.wx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName CouponAssignService
 * @Description
 * @Author Bruce
 * @Date 2020/7/22 7:37 下午
 */
@Service
@Transactional
public class CouponAssignService {

    @Autowired
    private JindouyunCouponUserService couponUserService;
    @Autowired
    private JindouyunCouponService couponService;

    /**
     * 分发注册优惠券
     * @param id
     */
    public void assignForRegister(Integer id) {

    }
}
