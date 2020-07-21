package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunComment;
import com.jindouyun.db.domain.JindouyunCommentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunCommentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_comment
     *
     * @mbg.generated
     */
    long countByExample(JindouyunCommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_comment
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunCommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_comment
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_comment
     *
     * @mbg.generated
     */
    int insert(JindouyunComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_comment
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_comment
     *
     * @mbg.generated
     */
    List<JindouyunComment> selectByExample(JindouyunCommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_comment
     *
     * @mbg.generated
     */
    JindouyunComment selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_comment
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunComment record, @Param("example") JindouyunCommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_comment
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunComment record, @Param("example") JindouyunCommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_comment
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_comment
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunComment record);
}