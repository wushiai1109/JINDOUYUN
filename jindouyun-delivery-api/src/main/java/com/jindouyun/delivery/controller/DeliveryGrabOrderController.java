package com.jindouyun.delivery.controller;

import com.fasterxml.jackson.databind.PropertyMetadata;
import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.common.constant.MergeOrderConstant;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunGrabOrder;
import com.jindouyun.db.domain.JindouyunMergeOrder;
import com.jindouyun.db.domain.JindouyunRegisteBrand;
import com.jindouyun.db.service.JindouyunAddressService;
import com.jindouyun.db.service.JindouyunGrabOrderService;
import com.jindouyun.db.service.JindouyunMergeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @ClassName DeliveryGrabController
 * @Description
 * @Author Bruce
 * @Date 2020/8/10 2:34 下午
 */
@RestController
@RequestMapping("/delivery/grab")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DeliveryGrabOrderController {

    @Autowired
    private JindouyunGrabOrderService grabOrderService;
    @Autowired
    private JindouyunMergeOrderService mergeOrderService;



    /**
     *
     * @param userId
     * @param body {orderId:xxx}
     * @return
     */
    @PostMapping("receive")
    public Object receive(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Integer grabOrderId = JacksonUtil.parseInteger(body,"grabOrderId");
        JindouyunGrabOrder grabOrder = grabOrderService.queryById(grabOrderId);
        if(grabOrder == null){
            System.err.println("接单 - grabOrder不存在");
            return ResponseUtil.badArgument();
        }
        if(grabOrder.getDeliveryId() != null && grabOrder.getDeliveryId() != -1 ){
            System.err.println("接单 - grabOrder已接单");
            return ResponseUtil.badArgument();
        }
        JindouyunMergeOrder mergeOrder = new JindouyunMergeOrder();
        mergeOrder.setId(grabOrderId);
        mergeOrder.setStatus(MergeOrderConstant.MERGE_ORDER_REVEIVE);
        mergeOrder.setReceiveTime(LocalDateTime.now());
        mergeOrderService.updateOrderStatus(mergeOrder);
        if( grabOrderService.updateDeliveryId(grabOrderId,userId) == 0){
            System.err.println("接单 - 数据库更新失败");
            return ResponseUtil.fail();
        }
        grabOrder.setDeliveryId(userId);
        return ResponseUtil.ok(grabOrder);
    }

}
