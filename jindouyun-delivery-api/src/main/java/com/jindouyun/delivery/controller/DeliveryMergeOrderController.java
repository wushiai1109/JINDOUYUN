package com.jindouyun.delivery.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunGrabOrder;
import com.jindouyun.db.domain.JindouyunMergeOrder;
import com.jindouyun.db.service.JindouyunGrabOrderService;
import com.jindouyun.db.service.JindouyunMergeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DeliveryManageOrderController
 * @Description
 * @Author Bruce
 * @Date 2020/8/10 5:16 下午
 */
@RestController
@RequestMapping("/delivery/manage")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DeliveryMergeOrderController {

    @Autowired
    private JindouyunGrabOrderService grabOrderService;

    @Autowired
    private JindouyunMergeOrderService mergeOrderService;


    /**
     * @param userId
     * @return
     */
    @GetMapping("complete")
    public Object complete(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        List<JindouyunGrabOrder> grabOrderList = grabOrderService.selectAllOrder(userId);
        List<JindouyunMergeOrder> mergeOrderList = new ArrayList<>();
        for (JindouyunGrabOrder grabOrder : grabOrderList) {
            JindouyunMergeOrder mergeOrder = mergeOrderService.selectById(grabOrder.getOrderId());
            if (mergeOrder.getStatus() != null && mergeOrder.getStatus() == 2){
                mergeOrderList.add(mergeOrder);
            }
        }
        return ResponseUtil.ok(mergeOrderList);
    }


}
