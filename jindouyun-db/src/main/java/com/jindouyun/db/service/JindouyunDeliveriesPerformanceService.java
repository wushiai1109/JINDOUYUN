package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunDeliveriesPerformanceMapper;
import com.jindouyun.db.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * @ClassName JindouyunDeliveriesPerformanceService
 * @Description
 * @Author Bruce
 * @Date 2020/8/10 1:12 下午
 */
@Service
public class JindouyunDeliveriesPerformanceService {

    @Resource
    private JindouyunDeliveriesPerformanceMapper deliveriesPerformanceMapper;
    @Resource
    private JindouyunClockinService clockinService;
    @Resource
    private JindouyunGrabOrderService grabOrderService;

    public StaffPerformance queryStaffPerformance(Integer userId, LocalDateTime date){
        //当日配送各时间段
        List<JindouyunClockin> workTimeList = clockinService.todayWork(userId, date);

        //当日各配送快递的数量
        LocalDate todayTime = date.toLocalDate();
        JindouyunDeliveriesPerformance deliveriesPerformance = findDeliveriesCountByTime(userId, todayTime);

        //当日总派送快递数量
        int todayWorkDeliveriesSum = deliveriesPerformance.getGoodsOrderNum() + deliveriesPerformance.getMenuOrderNum() + deliveriesPerformance.getExpressOrderNum();

        //本月配送信息
        //本月第一天
        LocalDate monthFirstDay = LocalDate.of(todayTime.getYear(), todayTime.getMonth(), 1);
        //本月最后一天
        LocalDate monthLastDay = todayTime.with(TemporalAdjusters.lastDayOfMonth());
        List<JindouyunDeliveriesPerformance> list = findMonthJindouyunDeliveriesPerformance(userId, monthFirstDay, monthLastDay);
        int monthWorkSumMinute = 0;
        int monthWorkMax = 0;
        //一个月最多的分钟时间，24*60*31=44640
        int monthWorkMin = 44640;
        int monthGoodsDeliveriesSum = 0;
        int monthMenuDeliveriesSum = 0;
        int monthExpressDeliveriesSum = 0;
        int monthWorkDeliveriesSum = 0;
        for (JindouyunDeliveriesPerformance performance : list) {
            monthWorkSumMinute += performance.getTodayWorkTime();
            monthGoodsDeliveriesSum += performance.getGoodsOrderNum();
            monthMenuDeliveriesSum += performance.getMenuOrderNum();
            monthExpressDeliveriesSum += performance.getExpressOrderNum();
            if (performance.getTodayWorkTime() > monthWorkMax) {
                monthWorkMax = performance.getTodayWorkTime();
            }
            if (performance.getTodayWorkTime() < monthWorkMin) {
                monthWorkMin = performance.getTodayWorkTime();
            }
        }
        monthWorkDeliveriesSum = monthGoodsDeliveriesSum + monthMenuDeliveriesSum + monthExpressDeliveriesSum;

        //本月未工作
        if (monthWorkMax == monthWorkMin) {
            monthWorkMax = 0;
            monthWorkMin = 0;
        }

        StaffPerformance performance = new StaffPerformance();

        performance.setTodayWorkTimeList(workTimeList);
        performance.setTodayWorkSumMinute(deliveriesPerformance.getTodayWorkTime());
        performance.setTodayGoodsDeliveriesSum(deliveriesPerformance.getGoodsOrderNum());
        performance.setTodayMenuDeliveriesSum(deliveriesPerformance.getMenuOrderNum());
        performance.setTodayExpressDeliveriesSum(deliveriesPerformance.getExpressOrderNum());
        performance.setTodayWorkDeliveriesSum(todayWorkDeliveriesSum);
        performance.setMonthWorkSumMinute(monthWorkSumMinute);
        performance.setMonthWorkMax(monthWorkMax);
        performance.setMonthWorkMin(monthWorkMin);
        performance.setMonthGoodsDeliveriesSum(monthGoodsDeliveriesSum);
        performance.setMonthMenuDeliveriesSum(monthMenuDeliveriesSum);
        performance.setMonthExpressDeliveriesSum(monthExpressDeliveriesSum);
        performance.setMonthWorkDeliveriesSum(monthWorkDeliveriesSum);

        return performance;
    }

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
