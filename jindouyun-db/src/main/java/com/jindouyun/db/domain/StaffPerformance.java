package com.jindouyun.db.domain;

import java.util.List;

/**
 * @className: StaffPerformance
 * @description:
 * @author: ZSZ
 * @date: 2020/8/14 8:52
 */
public class StaffPerformance {

    private List<JindouyunClockin> todayWorkTimeList;
    private Integer todayWorkSumMinute;
    private Integer todayWorkDeliveriesCount;
    private Integer todayWorkDeliveriesSum;
    private Integer monthWorkSumMinute;
    private Integer monthWorkMax;
    private Integer monthWorkMin;
    private Integer monthGoodsDeliveriesSum;
    private Integer monthMenuDeliveriesSum;
    private Integer monthExpressDeliveriesSum;
    private Integer monthWorkDeliveriesSum;

    public List<JindouyunClockin> getTodayWorkTimeList() {
        return todayWorkTimeList;
    }

    public void setTodayWorkTimeList(List<JindouyunClockin> todayWorkTimeList) {
        this.todayWorkTimeList = todayWorkTimeList;
    }

    public Integer getTodayWorkSumMinute() {
        return todayWorkSumMinute;
    }

    public void setTodayWorkSumMinute(Integer todayWorkSumMinute) {
        this.todayWorkSumMinute = todayWorkSumMinute;
    }

    public Integer getTodayWorkDeliveriesCount() {
        return todayWorkDeliveriesCount;
    }

    public void setTodayWorkDeliveriesCount(Integer todayWorkDeliveriesCount) {
        this.todayWorkDeliveriesCount = todayWorkDeliveriesCount;
    }

    public Integer getTodayWorkDeliveriesSum() {
        return todayWorkDeliveriesSum;
    }

    public void setTodayWorkDeliveriesSum(Integer todayWorkDeliveriesSum) {
        this.todayWorkDeliveriesSum = todayWorkDeliveriesSum;
    }

    public Integer getMonthWorkSumMinute() {
        return monthWorkSumMinute;
    }

    public void setMonthWorkSumMinute(Integer monthWorkSumMinute) {
        this.monthWorkSumMinute = monthWorkSumMinute;
    }

    public Integer getMonthWorkMax() {
        return monthWorkMax;
    }

    public void setMonthWorkMax(Integer monthWorkMax) {
        this.monthWorkMax = monthWorkMax;
    }

    public Integer getMothWorkMin() {
        return monthWorkMin;
    }

    public void setMonthWorkMin(Integer mothWorkMin) {
        this.monthWorkMin = mothWorkMin;
    }

    public Integer getMonthGoodsDeliveriesSum() {
        return monthGoodsDeliveriesSum;
    }

    public void setMonthGoodsDeliveriesSum(Integer monthGoodsDeliveriesSum) {
        this.monthGoodsDeliveriesSum = monthGoodsDeliveriesSum;
    }

    public Integer getMonthMenuDeliveriesSum() {
        return monthMenuDeliveriesSum;
    }

    public void setMonthMenuDeliveriesSum(Integer monthMenuDeliveriesSum) {
        this.monthMenuDeliveriesSum = monthMenuDeliveriesSum;
    }

    public Integer getMonthExpressDeliveriesSum() {
        return monthExpressDeliveriesSum;
    }

    public void setMonthExpressDeliveriesSum(Integer monthExpressDeliveriesSum) {
        this.monthExpressDeliveriesSum = monthExpressDeliveriesSum;
    }

    public Integer getMonthWorkDeliveriesSum() {
        return monthWorkDeliveriesSum;
    }

    public void setMonthWorkDeliveriesSum(Integer monthWorkDeliveriesSum) {
        this.monthWorkDeliveriesSum = monthWorkDeliveriesSum;
    }
}
