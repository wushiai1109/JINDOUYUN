package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunPermissionMapper;
import com.jindouyun.db.domain.JindouyunPermission;
import com.jindouyun.db.domain.JindouyunPermissionExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JindouyunPermissionService {
    @Resource
    private JindouyunPermissionMapper permissionMapper;

    /**
     * 根据 角色roleIds列表 查询所有 权限字符串
     * @param roleIds
     * @return
     */
    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = new HashSet<String>();
        if(roleIds.length == 0){
            return permissions;
        }

        JindouyunPermissionExample example = new JindouyunPermissionExample();
        example.or().andRoleIdIn(Arrays.asList(roleIds)).andDeletedEqualTo(false);
        List<JindouyunPermission> permissionList = permissionMapper.selectByExample(example);

        for(JindouyunPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }


    /**
     * 根据角色roleId 查询权限字符串
     * @param roleId
     * @return
     */
    public Set<String> queryByRoleId(Integer roleId) {
        Set<String> permissions = new HashSet<String>();
        if(roleId == null){
            return permissions;
        }

        JindouyunPermissionExample example = new JindouyunPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        List<JindouyunPermission> permissionList = permissionMapper.selectByExample(example);

        for(JindouyunPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    /**
     * 检查该角色 是否有超级权限 *
     * @param roleId
     * @return
     */
    public boolean checkSuperPermission(Integer roleId) {
        if(roleId == null){
            return false;
        }

        JindouyunPermissionExample example = new JindouyunPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andPermissionEqualTo("*").andDeletedEqualTo(false);
        return permissionMapper.countByExample(example) != 0;
    }

    /**
     * 删除某一角色的所有权限
     * @param roleId
     */
    public void deleteByRoleId(Integer roleId) {
        JindouyunPermissionExample example = new JindouyunPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        permissionMapper.logicalDeleteByExample(example);
    }

    /**
     * 增加权限
     * @param JindouyunPermission
     */
    public void add(JindouyunPermission JindouyunPermission) {
        JindouyunPermission.setAddTime(LocalDateTime.now());
        JindouyunPermission.setUpdateTime(LocalDateTime.now());
        permissionMapper.insertSelective(JindouyunPermission);
    }
}
