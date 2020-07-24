package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunFeedbackMapper;
import com.jindouyun.db.domain.JindouyunFeedback;
import com.jindouyun.db.domain.JindouyunFeedbackExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yogeek
 * @date 2018/8/27 11:39
 */
@Service
public class JindouyunFeedbackService {

    @Autowired
    private JindouyunFeedbackMapper feedbackMapper;

    public Integer add(JindouyunFeedback feedback) {
        feedback.setAddTime(LocalDateTime.now());
        feedback.setUpdateTime(LocalDateTime.now());
        return feedbackMapper.insertSelective(feedback);
    }

    /**
     * 查询 回复
     * @param userId
     * @param username
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunFeedback> querySelective(Integer userId, String username, Integer page, Integer limit, String sort, String order) {
        JindouyunFeedbackExample example = new JindouyunFeedbackExample();
        JindouyunFeedbackExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return feedbackMapper.selectByExample(example);
    }
}
