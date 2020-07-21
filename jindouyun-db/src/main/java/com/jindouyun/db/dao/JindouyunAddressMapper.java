package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunAddress;
import com.jindouyun.db.domain.JindouyunAddressExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunAddressMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_address
     *
     * @mbg.generated
     */
    long countByExample(JindouyunAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_address
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_address
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_address
     *
     * @mbg.generated
     */
    int insert(JindouyunAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_address
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_address
     *
     * @mbg.generated
     */
    List<JindouyunAddress> selectByExample(JindouyunAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_address
     *
     * @mbg.generated
     */
    JindouyunAddress selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_address
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunAddress record, @Param("example") JindouyunAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_address
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunAddress record, @Param("example") JindouyunAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_address
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_address
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunAddress record);
}