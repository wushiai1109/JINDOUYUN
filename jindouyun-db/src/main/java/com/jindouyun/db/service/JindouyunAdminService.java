package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunAdminMapper;
import com.jindouyun.db.domain.JindouyunAdmin;
import com.jindouyun.db.domain.JindouyunAdmin.Column;
import com.jindouyun.db.domain.JindouyunAdminExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunAdminService {
    private final Column[] result = new Column[]{Column.id, Column.username, Column.avatar, Column.roleIds};
    @Resource
    private JindouyunAdminMapper adminMapper;

    /**
     * 根据管理员username 查找管理员列表
     * @param username
     * @return
     */
    public List<JindouyunAdmin> findAdmin(String username) {
        JindouyunAdminExample example = new JindouyunAdminExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return adminMapper.selectByExample(example);
    }

    /**
     * 根据adminId 查询
     * @param id
     * @return
     */
    public JindouyunAdmin findAdmin(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    /**
     * 模糊查询 username
     * @param username
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunAdmin> querySelective(String username, Integer page, Integer limit, String sort, String order) {
        JindouyunAdminExample example = new JindouyunAdminExample();
        JindouyunAdminExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return adminMapper.selectByExampleSelective(example, result);
    }

    public int updateById(JindouyunAdmin admin) {
        admin.setUpdateTime(LocalDateTime.now());
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(JindouyunAdmin admin) {
        admin.setAddTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        admin.setDeleted(false);
        adminMapper.insertSelective(admin);
    }


    public JindouyunAdmin findById(Integer id) {
        return adminMapper.selectByPrimaryKeySelective(id, result);
    }

    /**
     * 查询所有
     * @return
     */
    public List<JindouyunAdmin> all() {
        JindouyunAdminExample example = new JindouyunAdminExample();
        example.or().andDeletedEqualTo(false);
        return adminMapper.selectByExample(example);
    }
}
