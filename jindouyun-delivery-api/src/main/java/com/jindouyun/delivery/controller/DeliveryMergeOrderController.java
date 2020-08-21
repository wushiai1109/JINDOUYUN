package com.jindouyun.delivery.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.common.constant.MergeOrderConstant;
import com.jindouyun.common.validator.Sort;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.service.JindouyunExpressOrderService;
import com.jindouyun.db.service.JindouyunGrabOrderService;
import com.jindouyun.db.service.JindouyunMergeOrderService;
import com.jindouyun.db.service.JindouyunOrderSplitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jindouyun.db.util.OrderUtil.STATUS_ARRIVED;
import static com.jindouyun.db.util.OrderUtil.STATUS_RECEIVE;
import static java.time.LocalDateTime.now;

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

    @Autowired
    private JindouyunOrderSplitService orderSplitService;

    @Autowired
    private JindouyunExpressOrderService expressOrderService;

    /**
     * 查询快递订单详情
     * @param userId
     * @param orderId
     * @return
     */
    @GetMapping("/expressOrderDetail")
    public Object queryExpressOrderDetail(@LoginUser Integer userId, @NotNull Integer orderId){
        if(userId == null){
            return ResponseUtil.unlogin();
        }
        if(orderId == null){
            return ResponseUtil.badArgument();
        }
        JindouyunExpressOrder expressOrder = expressOrderService.queryById(orderId);
        return ResponseUtil.ok(expressOrder);
    }

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
                       @RequestParam(required = false)List<Byte> orderStatusList,
                       Byte type,
                       Integer deliveryId, Integer mergeId,
                       Boolean force,
                       @RequestParam(defaultValue = "") LocalDateTime date,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @RequestParam(defaultValue = "desc") String order){
        if( userId == null){
            return ResponseUtil.unlogin();
        }
        if (orderStatusList == null ){
            orderStatusList = new ArrayList<>(){{add((byte)31);}};
        }
        Map result = grabOrderService.queryMergeInfoList(orderStatusList, type, deliveryId, mergeId, force, date, page, limit, sort, order);
        return ResponseUtil.ok(result);
    }


//    /**
//     * 完成订单
//     *
//     * @param userId
//     * @return
//     */
//    @GetMapping("complete")
//    public Object complete(@LoginUser Integer userId) {
//        if (userId == null) {
//            return ResponseUtil.unlogin();
//        }
//        List<JindouyunGrabOrder> grabOrderList = grabOrderService.selectAllOrder(userId);
//        List<JindouyunMergeOrder> mergeOrderList = new ArrayList<>();
//        for (JindouyunGrabOrder grabOrder : grabOrderList) {
//            JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(grabOrder.getOrderId());
//            if (mergeOrder.getStatus() != null && mergeOrder.getStatus() == 2) {
//                mergeOrderList.add(mergeOrder);
//            }
//        }
//        return ResponseUtil.ok(mergeOrderList);
//    }




    /**
     * 确认取件
     *
     * @param userId
     * @return
     */
    @PostMapping("pickup")
    public Object pickup(@LoginUser Integer userId, @RequestParam("orderId") Integer orderId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        //更新合单状态
        JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(orderId);
        mergeOrder.setStatus(MergeOrderConstant.MERGE_ORDER_RECEIVE);
        mergeOrder.setPickupTime(LocalDateTime.now());
        mergeOrderService.updateOrderStatus(mergeOrder);
        //更新子订单状态 302
        if(orderSplitService.updateStatusByMergeId(mergeOrder.getId(),STATUS_RECEIVE) == 0){
            System.err.println("取件 - 数据库更新失败");
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(mergeOrder);
    }

    /**
     * 确认送达
     *
     * @param userId
     * @return
     */
    @PostMapping("arrive")
    public Object arrive(@LoginUser Integer userId, @RequestParam("orderId") Integer orderId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        //更新合单状态
        JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(orderId);
        mergeOrder.setStatus(MergeOrderConstant.MERGE_ORDER_ARRIVED);
        mergeOrder.setArriveTime(LocalDateTime.now());
        mergeOrderService.updateOrderStatus(mergeOrder);
        //更新子订单状态 303
        if(orderSplitService.updateStatusByMergeId(mergeOrder.getId(),STATUS_ARRIVED) == 0){
            System.err.println("送达 - 数据库更新失败");
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(mergeOrder);
    }


}
