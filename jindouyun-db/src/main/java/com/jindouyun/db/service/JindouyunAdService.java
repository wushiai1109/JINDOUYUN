package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunAdMapper;
import com.jindouyun.db.domain.JindouyunAd;
import com.jindouyun.db.domain.JindouyunAdExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunAdService {
    @Resource
    private JindouyunAdMapper adMapper;

    /**
     * 查询首页广告
     * Position = 1
     * @return
     */
    public List<JindouyunAd> queryIndex() {
        JindouyunAdExample example = new JindouyunAdExample();
        example.or().andPositionEqualTo((byte) 1).andDeletedEqualTo(false).andEnabledEqualTo(true);
        return adMapper.selectByExample(example);
    }

    /**
     * 根据 name 或 content 模糊查询
     * @param name
     * @param content
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunAd> querySelective(String name, String content, Integer page, Integer limit, String sort, String order) {
        JindouyunAdExample example = new JindouyunAdExample();
        JindouyunAdExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(content)) {
            criteria.andContentLike("%" + content + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return adMapper.selectByExample(example);
    }

    public int updateById(JindouyunAd ad) {
        ad.setUpdateTime(LocalDateTime.now());
        return adMapper.updateByPrimaryKeySelective(ad);
    }

    public void deleteById(Integer id) {
        adMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(JindouyunAd ad) {
        ad.setAddTime(LocalDateTime.now());
        ad.setUpdateTime(LocalDateTime.now());
        adMapper.insertSelective(ad);
    }

    public JindouyunAd findById(Integer id) {
        return adMapper.selectByPrimaryKey(id);
    }
}
