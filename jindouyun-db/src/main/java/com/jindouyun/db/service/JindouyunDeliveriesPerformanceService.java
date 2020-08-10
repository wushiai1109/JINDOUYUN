package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunDeliveriesPerformanceMapper;
import com.jindouyun.db.domain.JindouyunDeliveriesPerformance;
import com.jindouyun.db.domain.JindouyunDeliveriesPerformanceExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName JindouyunDeliveriesPerformanceService
 * @Description
 * @Author Bruce
 * @Date 2020/8/10 1:12 下午
 */
@Service
@Transactional
public class JindouyunDeliveriesPerformanceService {

    @Autowired
    private JindouyunDeliveriesPerformanceMapper deliveriesPerformanceMapper;

    public JindouyunDeliveriesPerformance findDeliveriesCountByTime(Integer userId, LocalDate todayTime) {
        JindouyunDeliveriesPerformanceExample example = new JindouyunDeliveriesPerformanceExample();
        example.or().andUserIdEqualTo(userId).andTodayTimeEqualTo(todayTime).andDeletedEqualTo(false);
        return deliveriesPerformanceMapper.selectOneByExample(example);
    }

    public List<JindouyunDeliveriesPerformance> findMonthJindouyunDeliveriesPerformance(Integer userId, LocalDate monthFirstDay, LocalDate monthLastDay) {
        JindouyunDeliveriesPerformanceExample example = new JindouyunDeliveriesPerformanceExample();
        example.or().andUserIdEqualTo(userId).andTodayTimeBetween(monthFirstDay, monthLastDay).andDeletedEqualTo(false);
        return deliveriesPerformanceMapper.selectByExample(example);
    }
}
