package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunFootprintMapper;
import com.jindouyun.db.domain.JindouyunFootprint;
import com.jindouyun.db.domain.JindouyunFootprintExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunFootprintService {
    @Resource
    private JindouyunFootprintMapper footprintMapper;

    /**
     * 查询 用户所有的足迹 倒序
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public List<JindouyunFootprint> queryByAddTime(Integer userId, Integer page, Integer size) {
        JindouyunFootprintExample example = new JindouyunFootprintExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        example.setOrderByClause(JindouyunFootprint.Column.addTime.desc());
        PageHelper.startPage(page, size);
        return footprintMapper.selectByExample(example);
    }

    public JindouyunFootprint findById(Integer id) {
        return footprintMapper.selectByPrimaryKey(id);
    }

    public void deleteById(Integer id) {
        footprintMapper.logicalDeleteByPrimaryKey(id);
    }

    /**
     * 添加足迹
     * @param footprint
     */
    public void add(JindouyunFootprint footprint) {
        footprint.setAddTime(LocalDateTime.now());
        footprint.setUpdateTime(LocalDateTime.now());
        footprintMapper.insertSelective(footprint);
    }

    /**
     * 条件查询 userId or goodsId
     * @param userId
     * @param goodsId
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunFootprint> querySelective(String userId, String goodsId, Integer page, Integer size, String sort, String order) {
        JindouyunFootprintExample example = new JindouyunFootprintExample();
        JindouyunFootprintExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(Integer.valueOf(goodsId));
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return footprintMapper.selectByExample(example);
    }
}
