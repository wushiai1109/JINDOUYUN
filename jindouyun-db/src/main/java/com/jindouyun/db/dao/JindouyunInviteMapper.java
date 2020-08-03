package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunInvite;
import com.jindouyun.db.domain.JindouyunInviteExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunInviteMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    long countByExample(JindouyunInviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunInviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    int insert(JindouyunInvite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunInvite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    JindouyunInvite selectOneByExample(JindouyunInviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    JindouyunInvite selectOneByExampleSelective(@Param("example") JindouyunInviteExample example, @Param("selective") JindouyunInvite.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    List<JindouyunInvite> selectByExampleSelective(@Param("example") JindouyunInviteExample example, @Param("selective") JindouyunInvite.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    List<JindouyunInvite> selectByExample(JindouyunInviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    JindouyunInvite selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") JindouyunInvite.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    JindouyunInvite selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    JindouyunInvite selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunInvite record, @Param("example") JindouyunInviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunInvite record, @Param("example") JindouyunInviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunInvite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunInvite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") JindouyunInviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_invite
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}