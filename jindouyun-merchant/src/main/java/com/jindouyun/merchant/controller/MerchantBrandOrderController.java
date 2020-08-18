package com.jindouyun.merchant.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.common.validator.Sort;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.service.*;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @className: MerchantBrandOrderController
 * @description:
 * @author: ZSZ
 * @date: 2020/8/18 13:26
 */
@RestController
@RequestMapping("/merchant/order")
@Validated
public class MerchantBrandOrderController {

    @Autowired
    private JindouyunOrderSplitService orderSplitService;

    @Autowired
    private JindouyunBrandOrderService brandOrderService;

    @Autowired
    private JindouyunGrabOrderService grabOrderService;

    @Autowired
    private JindouyunDeliveryStaffService deliveryStaffService;

    @Autowired
    private JindouyunMergeOrderService mergeOrderService;

    @GetMapping("/detail")
    public Object detail(@LoginUser Integer userId, @NotNull Integer splitOrderId){
        if( userId == null){
            return ResponseUtil.unlogin();
        }
        OrderSplitVO splitVO = orderSplitService.queryOrderSplitVO(splitOrderId);
        return ResponseUtil.ok(splitVO);
    }

    @GetMapping("/list")
    public Object list(@LoginUser Integer userId,
                       @RequestParam(required = false) List<Byte> orderStatusList,
                       @RequestParam("brandId") Integer brandId, Integer mergeId,
                       @RequestParam(defaultValue = "") LocalDateTime date,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @RequestParam(defaultValue = "desc") String order){
        if( userId == null){
            return ResponseUtil.unlogin();
        }
        if (orderStatusList == null ){
            orderStatusList = new ArrayList<>(){{add((byte)21);add((byte) 22);add((byte)31);add((byte) 32);add((byte)33);}};
        }

        Map result = brandOrderService.queryMergeInfoList(orderStatusList, brandId, mergeId, date, page, limit, sort, order);
        return ResponseUtil.ok(result);
    }

    /**
     *
     * @param userId
     * @param mergeId
     * @return
     */
    @GetMapping("/queryDelivery")
    public Object queryDelivery(@LoginUser Integer userId,Integer mergeId){
        if(userId == null){
            return ResponseUtil.unlogin();
        }
        if(mergeId == null){
            return ResponseUtil.badArgument();
        }
        JindouyunGrabOrder grabOrder = grabOrderService.queryByMergeId(mergeId);
        StaffVO staffVO = null;
        if(grabOrder != null){
            staffVO = deliveryStaffService.queryStaffVOById(grabOrder.getDeliveryId());
        }
        return ResponseUtil.ok(staffVO);
    }

    @PostMapping("/receive")
    public Object receive(@LoginUser Integer userId,@RequestBody String body){
        if (userId == null){
            return ResponseUtil.unlogin();
        }
        Integer mergeId = JacksonUtil.parseInteger(body,"mergeId");
        if (mergeId == null){
            return ResponseUtil.badArgument();
        }
        JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(mergeId);
        if(mergeOrder == null){
            return ResponseUtil.badArgument();
        }
        mergeOrder.setStatus((byte) 22);
        if(mergeOrderService.updateOrderStatus(mergeOrder) == 0){
            return ResponseUtil.fail();
        }

        return ResponseUtil.ok(mergeOrder);
    }



}
