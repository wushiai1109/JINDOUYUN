package com.jindouyun.admin.controller;

import com.jindouyun.admin.annotation.RequiresPermissionsDesc;
import com.jindouyun.admin.service.AdminDeliveryService;
import com.jindouyun.common.validator.Order;
import com.jindouyun.common.validator.Sort;
import com.jindouyun.db.dao.JindouyunDeliveryStaffMapper;
import com.jindouyun.db.domain.JindouyunDeliveryStaff;
import com.jindouyun.db.service.JindouyunDeliveryStaffService;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @className: AdminDeliveryController
 * @description: 配送员管理
 * @author: ZSZ
 * @date: 2020/8/13 17:14
 */
@RestController
@RequestMapping("/admin/delivery")
@Validated
public class AdminDeliveryController {

    @Autowired
    private AdminDeliveryService deliveryService;

    @RequiresPermissions("admin:delivery:list")
    @RequiresPermissionsDesc(menu = {"派送管理", "派送人员管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(@RequestParam("deliveryId") Integer deliveryId,
                       @RequestParam("todayStatus") Short today_status,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order){
        return deliveryService.list(deliveryId, today_status, page, limit, sort, order);
    }

    @RequiresPermissions("admin:delivery:info")
    @RequiresPermissionsDesc(menu = {"派送管理", "派送人员管理"}, button = "查询详情")
    @GetMapping("/info")
    public Object info (@RequestParam("id")Integer id){
        return null;
    }

}
