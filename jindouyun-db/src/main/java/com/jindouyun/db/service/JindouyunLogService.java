package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunLogMapper;
import com.jindouyun.db.domain.JindouyunLog;
import com.jindouyun.db.domain.JindouyunLogExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunLogService {
    @Resource
    private JindouyunLogMapper logMapper;

    public void deleteById(Integer id) {
        logMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(JindouyunLog log) {
        log.setAddTime(LocalDateTime.now());
        log.setUpdateTime(LocalDateTime.now());
        logMapper.insertSelective(log);
    }

    public List<JindouyunLog> querySelective(String name, Integer page, Integer size, String sort, String order) {
        JindouyunLogExample example = new JindouyunLogExample();
        JindouyunLogExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andAdminLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return logMapper.selectByExample(example);
    }
}
