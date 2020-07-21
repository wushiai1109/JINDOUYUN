package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunCouponUser;
import com.jindouyun.db.domain.JindouyunCouponUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunCouponUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon_user
     *
     * @mbg.generated
     */
    long countByExample(JindouyunCouponUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon_user
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunCouponUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon_user
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon_user
     *
     * @mbg.generated
     */
    int insert(JindouyunCouponUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon_user
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunCouponUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon_user
     *
     * @mbg.generated
     */
    List<JindouyunCouponUser> selectByExample(JindouyunCouponUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon_user
     *
     * @mbg.generated
     */
    JindouyunCouponUser selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon_user
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunCouponUser record, @Param("example") JindouyunCouponUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon_user
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunCouponUser record, @Param("example") JindouyunCouponUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunCouponUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_coupon_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunCouponUser record);
}