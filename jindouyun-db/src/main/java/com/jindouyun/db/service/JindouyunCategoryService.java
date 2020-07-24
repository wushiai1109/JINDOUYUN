package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunCategoryMapper;
import com.jindouyun.db.domain.JindouyunCategory;
import com.jindouyun.db.domain.JindouyunCategoryExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunCategoryService {
    @Resource
    private JindouyunCategoryMapper categoryMapper;
    private JindouyunCategory.Column[] CHANNEL = {JindouyunCategory.Column.id, JindouyunCategory.Column.name, JindouyunCategory.Column.iconUrl};

    /**
     * 分页查询所有一级目录 不包括 “推荐”
     * @param offset
     * @param limit
     * @return
     */
    public List<JindouyunCategory> queryL1WithoutRecommend(int offset, int limit) {
        JindouyunCategoryExample example = new JindouyunCategoryExample();
        example.or().andLevelEqualTo("L1").andNameNotEqualTo("推荐").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    /**
     * 分页查询所有一级目录 包括 “推荐”
     * @param offset
     * @param limit
     * @return
     */
    public List<JindouyunCategory> queryL1(int offset, int limit) {
        JindouyunCategoryExample example = new JindouyunCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    /**
     * 查询所有一级目录 包括 “推荐”
     * @return
     */
    public List<JindouyunCategory> queryL1() {
        JindouyunCategoryExample example = new JindouyunCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    /**
     * 通过父id查询 所有子目录
     * @param pid
     * @return
     */
    public List<JindouyunCategory> queryByPid(Integer pid) {
        JindouyunCategoryExample example = new JindouyunCategoryExample();
        example.or().andPidEqualTo(pid).andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    /**
     * 根据id列表 查询子目录
     * @param ids
     * @return
     */
    public List<JindouyunCategory> queryL2ByIds(List<Integer> ids) {
        JindouyunCategoryExample example = new JindouyunCategoryExample();
        example.or().andIdIn(ids).andLevelEqualTo("L2").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }


    public JindouyunCategory findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 条件查询 id 或 name
     * @param id
     * @param name
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunCategory> querySelective(String id, String name, Integer page, Integer size, String sort, String order) {
        JindouyunCategoryExample example = new JindouyunCategoryExample();
        JindouyunCategoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(id)) {
            criteria.andIdEqualTo(Integer.valueOf(id));
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return categoryMapper.selectByExample(example);
    }

    public int updateById(JindouyunCategory category) {
        category.setUpdateTime(LocalDateTime.now());
        return categoryMapper.updateByPrimaryKeySelective(category);
    }

    public void deleteById(Integer id) {
        categoryMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(JindouyunCategory category) {
        category.setAddTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.insertSelective(category);
    }


    public List<JindouyunCategory> queryChannel() {
        JindouyunCategoryExample example = new JindouyunCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExampleSelective(example, CHANNEL);
    }
}
