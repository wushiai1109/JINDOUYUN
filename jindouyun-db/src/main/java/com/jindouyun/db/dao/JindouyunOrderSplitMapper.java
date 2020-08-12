package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunOrderSplit;
import com.jindouyun.db.domain.JindouyunOrderSplitExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunOrderSplitMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    long countByExample(JindouyunOrderSplitExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunOrderSplitExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    int insert(JindouyunOrderSplit record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunOrderSplit record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    JindouyunOrderSplit selectOneByExample(JindouyunOrderSplitExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    JindouyunOrderSplit selectOneByExampleSelective(@Param("example") JindouyunOrderSplitExample example, @Param("selective") JindouyunOrderSplit.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    List<JindouyunOrderSplit> selectByExampleSelective(@Param("example") JindouyunOrderSplitExample example, @Param("selective") JindouyunOrderSplit.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    List<JindouyunOrderSplit> selectByExample(JindouyunOrderSplitExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    JindouyunOrderSplit selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") JindouyunOrderSplit.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    JindouyunOrderSplit selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    JindouyunOrderSplit selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunOrderSplit record, @Param("example") JindouyunOrderSplitExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunOrderSplit record, @Param("example") JindouyunOrderSplitExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunOrderSplit record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunOrderSplit record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") JindouyunOrderSplitExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_order_split
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}