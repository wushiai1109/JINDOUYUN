package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunClockin;
import com.jindouyun.db.domain.JindouyunClockinExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunClockinMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    long countByExample(JindouyunClockinExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunClockinExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    int insert(JindouyunClockin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunClockin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    JindouyunClockin selectOneByExample(JindouyunClockinExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    JindouyunClockin selectOneByExampleSelective(@Param("example") JindouyunClockinExample example, @Param("selective") JindouyunClockin.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    List<JindouyunClockin> selectByExampleSelective(@Param("example") JindouyunClockinExample example, @Param("selective") JindouyunClockin.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    List<JindouyunClockin> selectByExample(JindouyunClockinExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    JindouyunClockin selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") JindouyunClockin.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    JindouyunClockin selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    JindouyunClockin selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunClockin record, @Param("example") JindouyunClockinExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunClockin record, @Param("example") JindouyunClockinExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunClockin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunClockin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") JindouyunClockinExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_clockin
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}