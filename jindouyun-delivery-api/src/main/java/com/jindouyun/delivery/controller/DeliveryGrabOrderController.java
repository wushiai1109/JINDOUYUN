package com.jindouyun.delivery.controller;

import com.fasterxml.jackson.databind.PropertyMetadata;
import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.common.constant.MergeOrderConstant;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunGrabOrder;
import com.jindouyun.db.domain.JindouyunMergeOrder;
import com.jindouyun.db.domain.JindouyunOrderSplit;
import com.jindouyun.db.domain.JindouyunRegisteBrand;
import com.jindouyun.db.service.JindouyunAddressService;
import com.jindouyun.db.service.JindouyunGrabOrderService;
import com.jindouyun.db.service.JindouyunMergeOrderService;
import com.jindouyun.db.service.JindouyunOrderSplitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.jindouyun.db.util.OrderUtil.STATUS_SHIP;

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
    @Autowired
    private JindouyunOrderSplitService splitService;



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
        //更新合单状态
        JindouyunMergeOrder mergeOrder = new JindouyunMergeOrder();
        mergeOrder.setId(grabOrder.getOrderId());
        //31
        mergeOrder.setStatus(MergeOrderConstant.MERGE_ORDER_RECEIVE);
        mergeOrder.setReceiveTime(LocalDateTime.now());
        mergeOrderService.updateOrderStatus(mergeOrder);
        if( grabOrderService.updateDeliveryId(grabOrderId,userId) == 0){
            System.err.println("接单 - 数据库更新失败");
            return ResponseUtil.fail();
        }
        //更新子订单状态 301
        if(splitService.updateStatusByMergeId(mergeOrder.getId(),STATUS_SHIP) == 0){
            System.err.println("接单 - 数据库更新失败");
            return ResponseUtil.fail();
        }
        grabOrder.setDeliveryId(userId);
        return ResponseUtil.ok(grabOrder);
    }

}
