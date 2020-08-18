package com.jindouyun.merchant.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunOrderSplit;
import com.jindouyun.db.service.JindouyunOrderSplitService;
import com.jindouyun.merchant.dto.BrandInfo;
import com.jindouyun.merchant.service.MerchantUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/incomeDetails")
    public Object incomeDetails(@LoginUser Integer userId, LocalDateTime data){
        if(userId == null){
            return ResponseUtil.unlogin();
        }
        LocalDateTime startTime;
        LocalDateTime endTime;
        if(data == null){
            data = LocalDateTime.now();
        }
        startTime = LocalDateTime.of(data.getYear(),data.getMonth(),0,0,0);
        endTime = LocalDateTime.of(data.getYear(),data.getMonth(),data.getDayOfMonth(),23,59);

        BrandInfo brandInfo = MerchantUserManager.merchantInfoMap.get(userId).getBrandInfo();

        /**
         * 101 订单生成，未支付；102，下单后未支付用户取消；103，下单后未支付超时系统自动取消
         * 201 支付完成，商家未发货；202，订单生产，已付款未发货，但是退款取消；
         * 301 商家发货，用户未确认；
         * 401 用户确认收货； 402 用户没有确认收货超过一定时间，系统自动确认收货；
         */
        List<Short> orderStatusArray = new ArrayList<>(){{add((short) 301);add((short) 401);add((short) 402);}};
        List<JindouyunOrderSplit>  orderSplits = orderSplitService.queryIncomesDetail(brandInfo.getId(),startTime,endTime,orderStatusArray);

        return ResponseUtil.ok(orderSplits);
    }

}
