package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunIssueMapper;
import com.jindouyun.db.domain.JindouyunIssue;
import com.jindouyun.db.domain.JindouyunIssueExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName JindouyunIssueService
 * @Description
 * @Author Bruce
 * @Date 2020/7/24 8:23 下午
 */
@Service
@Transactional
public class JindouyunIssueService {

    @Resource
    private JindouyunIssueMapper issueMapper;

    public void deleteById(Integer id) {
        issueMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(JindouyunIssue issue) {
        issue.setAddTime(LocalDateTime.now());
        issue.setUpdateTime(LocalDateTime.now());
        issueMapper.insertSelective(issue);
    }

    public List<JindouyunIssue> querySelective(String question, Integer page, Integer limit, String sort, String order) {
        JindouyunIssueExample example = new JindouyunIssueExample();
        JindouyunIssueExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(question)) {
            criteria.andQuestionLike("%" + question + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return issueMapper.selectByExample(example);
    }

    public int updateById(JindouyunIssue issue) {
        issue.setUpdateTime(LocalDateTime.now());
        return issueMapper.updateByPrimaryKeySelective(issue);
    }

    public JindouyunIssue findById(Integer id) {
        return issueMapper.selectByPrimaryKey(id);
    }
    
}
