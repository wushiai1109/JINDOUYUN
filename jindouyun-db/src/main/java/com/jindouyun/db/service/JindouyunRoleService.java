package com.jindouyun.db.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunRoleMapper;
import com.jindouyun.db.domain.JindouyunRole;
import com.jindouyun.db.domain.JindouyunRoleExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JindouyunRoleService {
    @Resource
    private JindouyunRoleMapper roleMapper;

    /**
     * 根据 rolesId 查询所有的角色
     * @param roleIds
     * @return
     */
    public Set<String> queryByIds(Integer[] roleIds) {
        Set<String> roles = new HashSet<String>();
        if(roleIds.length == 0){
            return roles;
        }

        JindouyunRoleExample example = new JindouyunRoleExample();
        example.or().andIdIn(Arrays.asList(roleIds)).andEnabledEqualTo(true).andDeletedEqualTo(false);
        List<JindouyunRole> roleList = roleMapper.selectByExample(example);

        for(JindouyunRole role : roleList){
            roles.add(role.getName());
        }

        return roles;

    }

    /**
     * 条件查询 name
     * @param name
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunRole> querySelective(String name, Integer page, Integer limit, String sort, String order) {
        JindouyunRoleExample example = new JindouyunRoleExample();
        JindouyunRoleExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return roleMapper.selectByExample(example);
    }

    public JindouyunRole findById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public void add(JindouyunRole role) {
        role.setAddTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.insertSelective(role);
    }

    public void deleteById(Integer id) {
        roleMapper.logicalDeleteByPrimaryKey(id);
    }

    public void updateById(JindouyunRole role) {
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateByPrimaryKeySelective(role);
    }

    /**
     * 查询 该角色是否存在 by name
     * @param name
     * @return
     */
    public boolean checkExist(String name) {
        JindouyunRoleExample example = new JindouyunRoleExample();
        example.or().andNameEqualTo(name).andDeletedEqualTo(false);
        return roleMapper.countByExample(example) != 0;
    }

    public List<JindouyunRole> queryAll() {
        JindouyunRoleExample example = new JindouyunRoleExample();
        example.or().andDeletedEqualTo(false);
        return roleMapper.selectByExample(example);
    }
}
