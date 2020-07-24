package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunIssueMapper;
import com.jindouyun.db.domain.JindouyunIssue;
import com.jindouyun.db.domain.JindouyunIssueExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunIssueService {
    @Resource
    private JindouyunIssueMapper issueMapper;

    public void deleteById(Integer id) {
        issueMapper.logicalDeleteByPrimaryKey(id);
    }

    /**
     * 添加
     * @param issue
     */
    public void add(JindouyunIssue issue) {
        issue.setAddTime(LocalDateTime.now());
        issue.setUpdateTime(LocalDateTime.now());
        issueMapper.insertSelective(issue);
    }

    /**
     * 条件查询 question
     * @param question
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
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
