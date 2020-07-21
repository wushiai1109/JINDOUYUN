package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunStorage;
import com.jindouyun.db.domain.JindouyunStorageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunStorageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_storage
     *
     * @mbg.generated
     */
    long countByExample(JindouyunStorageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_storage
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunStorageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_storage
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_storage
     *
     * @mbg.generated
     */
    int insert(JindouyunStorage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_storage
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunStorage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_storage
     *
     * @mbg.generated
     */
    List<JindouyunStorage> selectByExample(JindouyunStorageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_storage
     *
     * @mbg.generated
     */
    JindouyunStorage selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_storage
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunStorage record, @Param("example") JindouyunStorageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_storage
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunStorage record, @Param("example") JindouyunStorageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_storage
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunStorage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_storage
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunStorage record);
}