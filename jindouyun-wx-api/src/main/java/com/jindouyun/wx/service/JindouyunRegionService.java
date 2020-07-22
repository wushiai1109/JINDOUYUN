package com.jindouyun.wx.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunRegionMapper;
import com.jindouyun.db.domain.JindouyunRegion;
import com.jindouyun.db.domain.JindouyunRegionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName JindouyunRegionService
 * @Description
 * @Author Bruce
 * @Date 2020/7/22 9:11 上午
 */
@Service
@Transactional
public class JindouyunRegionService {

    @Autowired
    private JindouyunRegionMapper regionMapper;

    public List<JindouyunRegion> getAll() {
        JindouyunRegionExample regionExample = new JindouyunRegionExample();
        byte b = 4;
        regionExample.or().andTypeNotEqualTo(b);
        return regionMapper.selectByExample(regionExample);
    }

    public List<JindouyunRegion> queryByPid(Integer parentId) {
        JindouyunRegionExample example = new JindouyunRegionExample();
        example.or().andPidEqualTo(parentId);
        return regionMapper.selectByExample(example);
    }

    public JindouyunRegion findById(Integer id) {
        return regionMapper.selectByPrimaryKey(id);
    }

    public List<JindouyunRegion> querySelective(String name, Integer code, Integer page, Integer size, String sort, String order) {
        JindouyunRegionExample example = new JindouyunRegionExample();
        JindouyunRegionExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(code)) {
            criteria.andCodeEqualTo(code);
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return regionMapper.selectByExample(example);
    }

}
