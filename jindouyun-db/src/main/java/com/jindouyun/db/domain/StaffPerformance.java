package com.jindouyun.db.domain;

import java.util.List;

/**
 * @className: StaffPerformance
 * @description:
 * @author: ZSZ
 * @date: 2020/8/14 8:52
 */
public class StaffPerformance {

    //工作时间段
    private List<JindouyunClockin> todayWorkTimeList;
    //今日工作时间（分）
    private Integer todayWorkSumMinute;
    //今日商品订单配送次数
    private Integer todayGoodsDeliveriesSum;
    //今日外卖订单配送次数
    private Integer todayMenuDeliveriesSum;
    //今日快递订单配送次数
    private Integer todayExpressDeliveriesSum;
    //今日配送总数
    private Integer todayWorkDeliveriesSum;
    //本月工作时间
    private Integer monthWorkSumMinute;
    //本月最长工作时间
    private Integer monthWorkMax;
    //本月最短工作时间
    private Integer monthWorkMin;
    //本月商品订单配送次数
    private Integer monthGoodsDeliveriesSum;
    //本月外卖订单配送次数
    private Integer monthMenuDeliveriesSum;
    //本月快递订单配送次数
    private Integer monthExpressDeliveriesSum;
    //本月配送总次数
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

    public Integer getTodayGoodsDeliveriesSum() {
        return todayGoodsDeliveriesSum;
    }

    public void setTodayGoodsDeliveriesSum(Integer todayGoodsDeliveriesSum) {
        this.todayGoodsDeliveriesSum = todayGoodsDeliveriesSum;
    }

    public Integer getTodayMenuDeliveriesSum() {
        return todayMenuDeliveriesSum;
    }

    public void setTodayMenuDeliveriesSum(Integer todayMenuDeliveriesSum) {
        this.todayMenuDeliveriesSum = todayMenuDeliveriesSum;
    }

    public Integer getTodayExpressDeliveriesSum() {
        return todayExpressDeliveriesSum;
    }

    public void setTodayExpressDeliveriesSum(Integer todayExpressDeliveriesSum) {
        this.todayExpressDeliveriesSum = todayExpressDeliveriesSum;
    }

    public Integer getMonthWorkMin() {
        return monthWorkMin;
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
