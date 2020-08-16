package com.jindouyun.delivery.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunClockin;
import com.jindouyun.db.domain.JindouyunDeliveriesPerformance;
import com.jindouyun.db.domain.StaffPerformance;
import com.jindouyun.db.service.JindouyunClockinService;
import com.jindouyun.db.service.JindouyunDeliveriesPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DeliveryDataController
 * @Description
 * @Author Bruce
 * @Date 2020/8/9 3:07 下午
 */
@RestController
@RequestMapping("/delivery/data")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DeliveryDataController {

    @Autowired
    private JindouyunClockinService clockinService;

    @Autowired
    private JindouyunDeliveriesPerformanceService deliveriesPerformanceService;

    /**
     * @param userId 用户ID
     * @param date   yyyy-MM-dd格式
     * @return
     */
    @GetMapping("work_data")
    public Object workData(@LoginUser Integer userId, @RequestParam(value = "date",defaultValue = "") LocalDateTime date) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        if (date == null){
            return ResponseUtil.badArgument();
        }

        StaffPerformance performance = deliveriesPerformanceService.queryStaffPerformance(userId,date);
        return ResponseUtil.ok(performance);
    }

}
