package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunOrder;
import com.jindouyun.db.domain.JindouyunOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order
     *
     * @mbg.generated
     */
    long countByExample(JindouyunOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order
     *
     * @mbg.generated
     */
    int insert(JindouyunOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order
     *
     * @mbg.generated
     */
    List<JindouyunOrder> selectByExample(JindouyunOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order
     *
     * @mbg.generated
     */
    JindouyunOrder selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunOrder record, @Param("example") JindouyunOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunOrder record, @Param("example") JindouyunOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunOrder record);
}