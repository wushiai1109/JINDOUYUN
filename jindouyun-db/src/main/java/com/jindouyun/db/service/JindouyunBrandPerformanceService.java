package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunBrandPerformanceMapper;
import com.jindouyun.db.domain.JindouyunBrandPerformance;
import com.jindouyun.db.domain.JindouyunBrandPerformanceExample;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @className: JindouyunBrandPerformanceService
 * @description:
 * @author: ZSZ
 * @date: 2020/8/20 9:36
 */
@Service
public class JindouyunBrandPerformanceService {

    @Resource
    private JindouyunBrandPerformanceMapper performanceMapper;

    public JindouyunBrandPerformance queryTodayByUid(Integer userId){
        JindouyunBrandPerformanceExample example = new JindouyunBrandPerformanceExample();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.of(now.getYear(),now.getMonth(),now.getDayOfMonth(),0,0);
        example.or().andUserIdEqualTo(userId).andAddTimeBetween(startTime,startTime.plusDays(1)).andDeletedEqualTo(false);
        return performanceMapper.selectOneByExample(example);
    }

}
