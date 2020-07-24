package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunStorageMapper;
import com.jindouyun.db.domain.JindouyunStorage;
import com.jindouyun.db.domain.JindouyunStorageExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class JindouyunStorageService {

    @Resource
    private JindouyunStorageMapper storageMapper;

    public void deleteByKey(String key) {
        JindouyunStorageExample example = new JindouyunStorageExample();
        example.or().andKeyEqualTo(key);
        storageMapper.logicalDeleteByExample(example);
    }

    public void add(JindouyunStorage storageInfo) {
        storageInfo.setAddTime(LocalDateTime.now());
        storageInfo.setUpdateTime(LocalDateTime.now());
        storageMapper.insertSelective(storageInfo);
    }

    public JindouyunStorage findByKey(String key) {
        JindouyunStorageExample example = new JindouyunStorageExample();
        example.or().andKeyEqualTo(key).andDeletedEqualTo(false);
        return storageMapper.selectOneByExample(example);
    }

    public int update(JindouyunStorage storageInfo) {
        storageInfo.setUpdateTime(LocalDateTime.now());
        return storageMapper.updateByPrimaryKeySelective(storageInfo);
    }

    public JindouyunStorage findById(Integer id) {
        return storageMapper.selectByPrimaryKey(id);
    }

    public List<JindouyunStorage> querySelective(String key, String name, Integer page, Integer limit, String sort, String order) {
        JindouyunStorageExample example = new JindouyunStorageExample();
        JindouyunStorageExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(key)) {
            criteria.andKeyEqualTo(key);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return storageMapper.selectByExample(example);
    }
}
