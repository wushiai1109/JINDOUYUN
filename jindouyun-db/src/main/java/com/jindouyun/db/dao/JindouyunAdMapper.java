package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunAd;
import com.jindouyun.db.domain.JindouyunAdExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunAdMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    long countByExample(JindouyunAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    int insert(JindouyunAd record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunAd record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    JindouyunAd selectOneByExample(JindouyunAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    JindouyunAd selectOneByExampleSelective(@Param("example") JindouyunAdExample example, @Param("selective") JindouyunAd.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    List<JindouyunAd> selectByExampleSelective(@Param("example") JindouyunAdExample example, @Param("selective") JindouyunAd.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    List<JindouyunAd> selectByExample(JindouyunAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    JindouyunAd selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") JindouyunAd.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    JindouyunAd selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    JindouyunAd selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunAd record, @Param("example") JindouyunAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunAd record, @Param("example") JindouyunAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunAd record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunAd record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") JindouyunAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_ad
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}