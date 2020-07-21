package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunCoupon;
import com.jindouyun.db.domain.JindouyunCouponExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunCouponMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon
     *
     * @mbg.generated
     */
    long countByExample(JindouyunCouponExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunCouponExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon
     *
     * @mbg.generated
     */
    int insert(JindouyunCoupon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunCoupon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon
     *
     * @mbg.generated
     */
    List<JindouyunCoupon> selectByExample(JindouyunCouponExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon
     *
     * @mbg.generated
     */
    JindouyunCoupon selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunCoupon record, @Param("example") JindouyunCouponExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunCoupon record, @Param("example") JindouyunCouponExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunCoupon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunCoupon record);
}