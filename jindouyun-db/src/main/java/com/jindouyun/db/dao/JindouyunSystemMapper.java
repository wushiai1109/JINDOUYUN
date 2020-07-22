package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunSystem;
import com.jindouyun.db.domain.JindouyunSystemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunSystemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    long countByExample(JindouyunSystemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunSystemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    int insert(JindouyunSystem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunSystem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    JindouyunSystem selectOneByExample(JindouyunSystemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    JindouyunSystem selectOneByExampleSelective(@Param("example") JindouyunSystemExample example, @Param("selective") JindouyunSystem.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    List<JindouyunSystem> selectByExampleSelective(@Param("example") JindouyunSystemExample example, @Param("selective") JindouyunSystem.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    List<JindouyunSystem> selectByExample(JindouyunSystemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    JindouyunSystem selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") JindouyunSystem.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    JindouyunSystem selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    JindouyunSystem selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunSystem record, @Param("example") JindouyunSystemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunSystem record, @Param("example") JindouyunSystemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunSystem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunSystem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") JindouyunSystemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_system
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}