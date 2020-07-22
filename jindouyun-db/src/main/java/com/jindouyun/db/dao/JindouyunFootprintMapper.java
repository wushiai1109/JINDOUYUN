package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunFootprint;
import com.jindouyun.db.domain.JindouyunFootprintExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunFootprintMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    long countByExample(JindouyunFootprintExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunFootprintExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    int insert(JindouyunFootprint record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunFootprint record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    JindouyunFootprint selectOneByExample(JindouyunFootprintExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    JindouyunFootprint selectOneByExampleSelective(@Param("example") JindouyunFootprintExample example, @Param("selective") JindouyunFootprint.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    List<JindouyunFootprint> selectByExampleSelective(@Param("example") JindouyunFootprintExample example, @Param("selective") JindouyunFootprint.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    List<JindouyunFootprint> selectByExample(JindouyunFootprintExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    JindouyunFootprint selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") JindouyunFootprint.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    JindouyunFootprint selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    JindouyunFootprint selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunFootprint record, @Param("example") JindouyunFootprintExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunFootprint record, @Param("example") JindouyunFootprintExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunFootprint record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunFootprint record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") JindouyunFootprintExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_footprint
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}