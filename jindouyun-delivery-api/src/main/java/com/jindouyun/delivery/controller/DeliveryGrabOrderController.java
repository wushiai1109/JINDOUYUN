package com.jindouyun.delivery.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunGrabOrder;
import com.jindouyun.db.service.JindouyunGrabOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

//    /**
//     *
//     * @param userId
//     * @param jindouyunGrabOrder {orderId:xxx,deliveryId:xxx,adminId:xxx,force:xxx}
//     * @return
//     */
//    @PostMapping("receive")
//    public Object receive(@LoginUser Integer userId, @RequestBody JindouyunGrabOrder jindouyunGrabOrder) {
//        if (userId == null) {
//            return ResponseUtil.unlogin();
//        }
//        jindouyunGrabOrder.setUserId(userId);
//        Object result = grabOrderService.insertOneByGrabOrder(jindouyunGrabOrder);
//        return ResponseUtil.ok(result);
//    }

}
