package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunUser;
import com.jindouyun.db.domain.JindouyunUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_user
     *
     * @mbg.generated
     */
    long countByExample(JindouyunUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_user
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_user
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_user
     *
     * @mbg.generated
     */
    int insert(JindouyunUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_user
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_user
     *
     * @mbg.generated
     */
    List<JindouyunUser> selectByExample(JindouyunUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_user
     *
     * @mbg.generated
     */
    JindouyunUser selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_user
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunUser record, @Param("example") JindouyunUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_user
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunUser record, @Param("example") JindouyunUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunUser record);
}