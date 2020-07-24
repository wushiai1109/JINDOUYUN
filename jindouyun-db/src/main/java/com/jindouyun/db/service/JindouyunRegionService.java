package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunRegionMapper;
import com.jindouyun.db.domain.JindouyunRegion;
import com.jindouyun.db.domain.JindouyunRegionExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JindouyunRegionService {

    @Resource
    private JindouyunRegionMapper regionMapper;

    public List<JindouyunRegion> getAll(){
        JindouyunRegionExample example = new JindouyunRegionExample();
        byte b = 4;
        example.or().andTypeNotEqualTo(b);
        return regionMapper.selectByExample(example);
    }

    /**
     * 查询 子列表
     * @param parentId
     * @return
     */
    public List<JindouyunRegion> queryByPid(Integer parentId) {
        JindouyunRegionExample example = new JindouyunRegionExample();
        example.or().andPidEqualTo(parentId);
        return regionMapper.selectByExample(example);
    }

    public JindouyunRegion findById(Integer id) {
        return regionMapper.selectByPrimaryKey(id);
    }

    /**
     * 条件查询 name or code
     * @param name
     * @param code
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
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
