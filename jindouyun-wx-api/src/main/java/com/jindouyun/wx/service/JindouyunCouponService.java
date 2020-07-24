package com.jindouyun.wx.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunCouponMapper;
import com.jindouyun.db.dao.JindouyunCouponUserMapper;
import com.jindouyun.db.domain.JindouyunCoupon;
import com.jindouyun.db.domain.JindouyunCouponExample;
import com.jindouyun.db.domain.JindouyunCoupon.Column;
import com.jindouyun.db.util.CouponConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private Column[] result = new Column[]{Column.id, Column.name, Column.desc,
            Column.days, Column.startTime, Column.endTime,
            Column.discount, Column.min};
    
    
    public JindouyunCoupon findById(Integer couponId) {
        return couponMapper.selectByPrimaryKey(couponId);
    }

    /**
     * 查询，空参数
     *
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunCoupon> queryList(Integer page, Integer limit, String sort, String order) {
        return queryList(JindouyunCouponExample.newAndCreateCriteria(), page, limit, sort, order);
    }

    /**
     * 查询
     *
     * @param criteria 可扩展的条件
     * @param offset
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunCoupon> queryList(JindouyunCouponExample.Criteria criteria, int offset, int limit, String sort, String order) {
        criteria.andTypeEqualTo(CouponConstant.TYPE_COMMON).andStatusEqualTo(CouponConstant.STATUS_NORMAL).andDeletedEqualTo(false);
        criteria.example().setOrderByClause(sort + " " + order);
        PageHelper.startPage(offset, limit);
        return couponMapper.selectByExampleSelective(criteria.example(), result);
    }

    public JindouyunCoupon findByCode(String code) {
        JindouyunCouponExample example = new JindouyunCouponExample();
        example.or().andCodeEqualTo(code).andTypeEqualTo(CouponConstant.TYPE_CODE).andStatusEqualTo(CouponConstant.STATUS_NORMAL).andDeletedEqualTo(false);
        List<JindouyunCoupon> couponList =  couponMapper.selectByExample(example);
        if(couponList.size() > 1){
            throw new RuntimeException("");
        }
        else if(couponList.size() == 0){
            return null;
        }
        else {
            return couponList.get(0);
        }
    }
}
