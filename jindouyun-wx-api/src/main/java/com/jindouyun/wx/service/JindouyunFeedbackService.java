package com.jindouyun.wx.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunFeedbackMapper;
import com.jindouyun.db.domain.JindouyunFeedback;
import com.jindouyun.db.domain.JindouyunFeedbackExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName JindouyunFeedbackService
 * @Description
 * @Author Bruce
 * @Date 2020/7/24 3:15 下午
 */
@Service
@Transactional
public class JindouyunFeedbackService {
    @Autowired
    private JindouyunFeedbackMapper feedbackMapper;

    public Integer add(JindouyunFeedback feedback) {
        feedback.setAddTime(LocalDateTime.now());
        feedback.setUpdateTime(LocalDateTime.now());
        return feedbackMapper.insertSelective(feedback);
    }

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
