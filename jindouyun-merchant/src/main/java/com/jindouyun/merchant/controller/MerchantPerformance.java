package com.jindouyun.merchant.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunBrandPerformance;
import com.jindouyun.db.domain.JindouyunOrderSplit;
import com.jindouyun.db.service.JindouyunBrandPerformanceService;
import com.jindouyun.db.service.JindouyunOrderSplitService;
import com.jindouyun.merchant.dto.BrandInfo;
import com.jindouyun.merchant.dto.Record;
import com.jindouyun.merchant.service.MerchantUserManager;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: MerchantPerformance
 * @description:
 * @author: ZSZ
 * @date: 2020/8/17 21:07
 */
@RestController
@RequestMapping("/merchant/data")
@Validated
public class MerchantPerformance {

    @Autowired
    private JindouyunOrderSplitService orderSplitService;
    @Autowired
    private JindouyunBrandPerformanceService performanceService;

    @GetMapping("/incomeDetails")
    public Object incomeDetails(@LoginUser Integer userId,@RequestParam(value = "date",defaultValue = "") LocalDateTime date){
        if(userId == null){
            return ResponseUtil.unlogin();
        }
        LocalDateTime startTime;
        LocalDateTime endTime;
        if(date == null){
            date = LocalDateTime.now();
        }
        startTime = LocalDateTime.of(date.getYear(),date.getMonth(),1,0,0);
        endTime = startTime.plusMonths(1);

        BrandInfo brandInfo = MerchantUserManager.merchantInfoMap.get(userId).getBrandInfo();

        /**
         * 101 订单生成，未支付；102，下单后未支付用户取消；103，下单后未支付超时系统自动取消
         * 201 支付完成，商家未发货；202，订单生产，已付款未发货，但是退款取消；
         * 301 商家发货，用户未确认；
         * 401 用户确认收货； 402 用户没有确认收货超过一定时间，系统自动确认收货；
         */
        List<Short> orderStatusArray = new ArrayList<>(){{add((short) 301);add((short) 302);add((short) 303);add((short) 401);add((short) 402);}};
        List<JindouyunOrderSplit>  orderSplits = orderSplitService.queryIncomesDetail(brandInfo.getId(),startTime,endTime,orderStatusArray);
        Map<String, List<Record>> map = new HashMap<>();
        if(orderSplits != null){
            for(JindouyunOrderSplit orderSplit:orderSplits){
                LocalDateTime orderTime = orderSplit.getAddTime();
                String time = orderTime.getMonthValue() +"月"+orderTime.getDayOfMonth()+"日";
                if(!map.containsKey(time)){
                    map.put(time,new ArrayList<>());
                }
                Record record = new Record(time,orderSplit.getOrderSn(),orderSplit.getGoodsPrice());
                map.get(time).add(record);
            }
        }

        return ResponseUtil.ok(map);
    }

    @GetMapping("/todayData")
    public Object todayData(@LoginUser Integer userId){
        if(userId == null){
            return ResponseUtil.unlogin();
        }
        JindouyunBrandPerformance performance = performanceService.queryTodayByUid(userId);
        if(performance == null){
            performance = new JindouyunBrandPerformance();
            performance.setTodayOrder(0);
            performance.setTodayTurnover(new BigDecimal(0));
        }
        return ResponseUtil.ok(performance);
    }

}
