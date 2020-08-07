package com.jindouyun.delivery.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.service.JindouyunDeliveryStaffService;
import com.jindouyun.delivery.dto.DeliveryInfo;
import com.jindouyun.delivery.service.LoginUserManager;
import com.jindouyun.delivery.util.BadArgumentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.jindouyun.common.constant.WxResponseCode.*;

/**
 * @className: DeliveryStaffController
 * @description: 员工状态
 * @author: ZSZ
 * @date: 2020/8/4 20:27
 */
@RestController
@RequestMapping("/delivery/staff")
@Validated
public class DeliveryStaffController {

    @Autowired
    JindouyunDeliveryStaffService staffService;

    /**
     * 改变用户今日状态以及工作状态
     * @param userId
     * @param body {today_status: 0:休息，1：工作，work_status:0:未工作，1：工作中，未配送，2：工作中，配送中}
     * @return
     */
    @PostMapping("/status")
    public Object changeTodayStatus(@LoginUser Integer userId, @RequestBody String body){
        DeliveryInfo deliveryInfo = LoginUserManager.deliveryInfoMap.get(userId);
        short today_status = JacksonUtil.parseShort(body,"today_status");
        short work_status = JacksonUtil.parseShort(body,"work_status");
        if(deliveryInfo == null){
            return ResponseUtil.fail(AUTH_INVALID_ACCOUNT,"用户不存在");
        }

        if(!BadArgumentUtil.judgeTodayStatus(today_status)){
            return ResponseUtil.badArgument();
        }

        if(!BadArgumentUtil.judgeWorkStatus(work_status)){
            return ResponseUtil.badArgument();
        }


        //修改数据库骑手状态
        staffService.updateStaffStatus(userId,today_status,work_status);
        //修改缓存
        deliveryInfo.getDeliveryStaff().setTodayStatus(today_status);
        deliveryInfo.getDeliveryStaff().setWorkStatus(work_status);

        LoginUserManager.deliveryInfoMap.put(userId,deliveryInfo);

        return ResponseUtil.ok(deliveryInfo);

    }

}
