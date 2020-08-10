package com.jindouyun.delivery.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunClockin;
import com.jindouyun.db.domain.JindouyunDeliveriesPerformance;
import com.jindouyun.db.service.JindouyunClockinService;
import com.jindouyun.db.service.JindouyunDeliveriesPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DeliveryDataController
 * @Description
 * @Author Bruce
 * @Date 2020/8/9 3:07 下午
 */
@RestController
@RequestMapping("/delivery/data")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DeliveryDataController {

    @Autowired
    private JindouyunClockinService clockinService;

    @Autowired
    private JindouyunDeliveriesPerformanceService deliveriesPerformanceService;

    /**
     * @param userId 用户ID
     * @param date   yyyy-MM-dd格式
     * @return
     */
    @GetMapping("work_data")
    public Object workData(@LoginUser Integer userId, @RequestParam("date") String date) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        //当日配送各时间段
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(date, df);
        List<JindouyunClockin> workTimeList = clockinService.todayWork(userId, parse);

//        //当日配送总时间
//        int todayWorkSumMinute = 0;
//        for (JindouyunClockin clockin :workTimeList) {
//            todayWorkSumMinute += clockin.getWorkTime();
//        }

        //当日各配送快递的数量
        LocalDate todayTime = parse.toLocalDate();
        JindouyunDeliveriesPerformance deliveriesPerformance = deliveriesPerformanceService.findDeliveriesCountByTime(userId, todayTime);

        //当日总派送快递数量
        int todayWorkDeliveriesSum = deliveriesPerformance.getGoodsOrderNum() + deliveriesPerformance.getMenuOrderNum() + deliveriesPerformance.getExpressOrderNum();

        //本月配送信息
        //本月第一天
        LocalDate monthFirstDay = LocalDate.of(todayTime.getYear(), todayTime.getMonth(), 1);
        //本月最后一天
        LocalDate monthLastDay = todayTime.with(TemporalAdjusters.lastDayOfMonth());
        List<JindouyunDeliveriesPerformance> list = deliveriesPerformanceService.findMonthJindouyunDeliveriesPerformance(userId, monthFirstDay, monthLastDay);
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

        Map<String, Object> map = new HashMap<>();
        map.put("todayWorkTimeList", workTimeList);
        map.put("todayWorkSumMinute", deliveriesPerformance.getTodayWorkTime());
        map.put("todayWorkDeliveriesCount", deliveriesPerformance);
        map.put("todayWorkDeliveriesSum", todayWorkDeliveriesSum);
        map.put("monthWorkSumMinute", monthWorkSumMinute);
        map.put("monthWorkMax", monthWorkMax);
        map.put("monthWorkMin", monthWorkMin);
        map.put("monthGoodsDeliveriesSum", monthGoodsDeliveriesSum);
        map.put("monthMenuDeliveriesSum", monthMenuDeliveriesSum);
        map.put("monthExpressDeliveriesSum", monthExpressDeliveriesSum);
        map.put("monthWorkDeliveriesSum", monthWorkDeliveriesSum);

        return ResponseUtil.ok(map);
    }

}
