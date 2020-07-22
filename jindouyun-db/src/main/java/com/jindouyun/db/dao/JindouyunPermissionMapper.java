package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunPermission;
import com.jindouyun.db.domain.JindouyunPermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JindouyunPermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    long countByExample(JindouyunPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    int deleteByExample(JindouyunPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    int insert(JindouyunPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    int insertSelective(JindouyunPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    JindouyunPermission selectOneByExample(JindouyunPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    JindouyunPermission selectOneByExampleSelective(@Param("example") JindouyunPermissionExample example, @Param("selective") JindouyunPermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    List<JindouyunPermission> selectByExampleSelective(@Param("example") JindouyunPermissionExample example, @Param("selective") JindouyunPermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    List<JindouyunPermission> selectByExample(JindouyunPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    JindouyunPermission selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") JindouyunPermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    JindouyunPermission selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    JindouyunPermission selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") JindouyunPermission record, @Param("example") JindouyunPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") JindouyunPermission record, @Param("example") JindouyunPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(JindouyunPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(JindouyunPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") JindouyunPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_permission
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}