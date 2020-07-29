package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunSearchHistoryMapper;
import com.jindouyun.db.domain.JindouyunSearchHistory;
import com.jindouyun.db.domain.JindouyunSearchHistoryExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName JindouyunSearchHistoryService
 * @Description
 * @Author Bruce
 * @Date 2020/7/29 2:27 下午
 */
@Service
@Transactional
public class JindouyunSearchHistoryService {

    @Resource
    private JindouyunSearchHistoryMapper searchHistoryMapper;

    public void save(JindouyunSearchHistory searchHistory) {
        searchHistory.setAddTime(LocalDateTime.now());
        searchHistory.setUpdateTime(LocalDateTime.now());
        searchHistoryMapper.insertSelective(searchHistory);
    }

    public List<JindouyunSearchHistory> queryByUid(int uid) {
        JindouyunSearchHistoryExample example = new JindouyunSearchHistoryExample();
        example.or().andUserIdEqualTo(uid).andDeletedEqualTo(false);
        example.setDistinct(true);
        return searchHistoryMapper.selectByExampleSelective(example, JindouyunSearchHistory.Column.keyword);
    }

    public void deleteByUid(int uid) {
        JindouyunSearchHistoryExample example = new JindouyunSearchHistoryExample();
        example.or().andUserIdEqualTo(uid);
        searchHistoryMapper.logicalDeleteByExample(example);
    }

    public List<JindouyunSearchHistory> querySelective(String userId, String keyword, Integer page, Integer size, String sort, String order) {
        JindouyunSearchHistoryExample example = new JindouyunSearchHistoryExample();
        JindouyunSearchHistoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andKeywordLike("%" + keyword + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return searchHistoryMapper.selectByExample(example);
    }
    
}
