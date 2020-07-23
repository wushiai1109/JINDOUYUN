package com.jindouyun.wx.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunCouponUserMapper;
import com.jindouyun.db.domain.JindouyunCouponUser;
import com.jindouyun.db.domain.JindouyunCouponUserExample;
import com.jindouyun.db.util.CouponUserConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName JindouyunCouponUserService
 * @Description
 * @Author Bruce
 * @Date 2020/7/22 7:38 下午
 */
@Service
@Transactional
public class JindouyunCouponUserService {
    
    @Autowired
    private JindouyunCouponUserMapper couponUserMapper;

    public List<JindouyunCouponUser> queryList(Integer userId, Integer couponId, Short status, Integer page, Integer size, String sort, String order) {
        JindouyunCouponUserExample example = new JindouyunCouponUserExample();
        JindouyunCouponUserExample.Criteria criteria = example.createCriteria();
        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if(couponId != null){
            criteria.andCouponIdEqualTo(couponId);
        }
        if (status != null) {
            criteria.andStatusEqualTo(status);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        if (!StringUtils.isEmpty(page) && !StringUtils.isEmpty(size)) {
            PageHelper.startPage(page, size);
        }

        return couponUserMapper.selectByExample(example);
    }

    public List<JindouyunCouponUser> queryAll(Integer userId) {
        return queryList(userId, null, CouponUserConstant.STATUS_USABLE, null, null, "add_time", "desc");
    }

    public JindouyunCouponUser findById(Integer userCouponId) {
        return couponUserMapper.selectByPrimaryKey(userCouponId);
    }

    public JindouyunCouponUser queryOne(Integer userId, Integer couponId) {
        List<JindouyunCouponUser> couponUserList = queryList(userId, couponId, CouponUserConstant.STATUS_USABLE, 1, 1, "add_time", "desc");
        if(couponUserList.size() == 0){
            return null;
        }
        return couponUserList.get(0);
    }
}
