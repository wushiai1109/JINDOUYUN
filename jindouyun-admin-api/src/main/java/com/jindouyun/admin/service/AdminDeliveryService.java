package com.jindouyun.admin.service;

import com.jindouyun.admin.model.vo.StaffInfo;
import com.jindouyun.common.constant.MergeOrderConstant;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jindouyun.admin.util.AdminResponseCode.*;

/**
 * @className: AdminDeliveryService
 * @description:
 * @author: ZSZ
 * @date: 2020/8/13 17:20
 */
@Service
public class AdminDeliveryService {

    @Autowired
    private JindouyunDeliveryStaffService staffService;

    @Autowired
    private JindouyunClockinService clockinService;

    @Autowired
    private JindouyunDeliveriesPerformanceService performanceService;

    @Autowired
    private JindouyunUserService userService;

    @Autowired
    private JindouyunMergeOrderService mergeOrderService;

    @Autowired
    private JindouyunGrabOrderService grabOrderService;

    /**
     * 查询所有配送员
     * @param deliveryId
     * @param todayStatus
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public Object list(Integer deliveryId,Short todayStatus,Integer page,Integer limit,String sort,String order){
        return staffService.queryStaffVO(deliveryId,todayStatus,page,limit,sort,order);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    public Object info(Integer id){
        StaffVO staffVO = staffService.queryStaffVOById(id);
        if(staffVO == null){
            System.err.println("配送员详情 - id错误");
        }
        List<JindouyunClockin> workTimes = clockinService.todayWork(id, LocalDateTime.now());
        StaffPerformance performance = performanceService.queryStaffPerformance(id,LocalDateTime.now());

        StaffInfo staffInfo = new StaffInfo(staffVO,workTimes,performance);
        return ResponseUtil.ok(staffInfo);
    }

    /**
     * 获取所有派送员的名字和id
     * @return
     */
    public Object allStaffName(){
        List<JindouyunDeliveryStaff> all = staffService.all();
        Map<Integer,String> map = new HashMap<>();
        for (JindouyunDeliveryStaff staff:all) {
            JindouyunUser user = userService.findById(staff.getUserId());
            if(user != null){
                map.put(staff.getId(),user.getNickname());
            }
        }
        return ResponseUtil.ok(map);
    }

    /**
     *
     * 强制派单
     * @param mergeId
     * @return
     */
    @Transactional
    public Object forceOrder(Integer mergeId,Integer deliveryId){
        JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(mergeId);
        if(mergeOrder == null){
            System.err.println("强制派单 - 合单不存在");
            return ResponseUtil.fail(MERGE_ORDER_NUEXIST,"合单不存在");
        }
        if (mergeOrder.getRelease() == 0){
            System.err.println("强制派单 - 合单未发布");
            return ResponseUtil.fail(MERGE_ORDER_UNRELEASED,"合单未发布");
        }
        JindouyunGrabOrder grabOrder = grabOrderService.queryByMergeId(mergeId);
        if(grabOrder == null){
            System.err.println("强制派单 - grabOrder不存在");
            return ResponseUtil.badArgument();
        }
        if(grabOrder.getDeliveryId() != null && grabOrder.getDeliveryId() !=0){
            System.err.println("强制派单 - 该订单已被接单");
            return ResponseUtil.fail(GRABORDER_ORDER_RECEIVED,"该订单已被接单");
        }
        if(grabOrderService.updateDeliveryId(grabOrder.getId(),deliveryId)!=0){
            mergeOrder.setPickupTime(LocalDateTime.now());
            mergeOrder.setStatus(MergeOrderConstant.MERGE_ORDER_RECEIVE);
            mergeOrderService.updateOrderStatus(mergeOrder);
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }


}
