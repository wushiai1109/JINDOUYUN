package com.jindouyun.wx.service;

import com.jindouyun.db.dao.JindouyunCouponMapper;
import com.jindouyun.db.dao.JindouyunCouponUserMapper;
import com.jindouyun.db.domain.JindouyunCoupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName JindouyunCouponService
 * @Description
 * @Author Bruce
 * @Date 2020/7/21 8:19 下午
 */
@Service
@Transactional
public class JindouyunCouponService {

    @Autowired
    private JindouyunCouponMapper couponMapper;
    
    @Autowired
    private JindouyunCouponUserMapper couponUserMapper;
    
    public JindouyunCoupon findById(Integer couponId) {
        return couponMapper.selectByPrimaryKey(couponId);
    }
}
