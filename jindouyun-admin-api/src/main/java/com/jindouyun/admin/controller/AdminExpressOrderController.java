package com.jindouyun.admin.controller;

import com.jindouyun.admin.annotation.RequiresPermissionsDesc;
import com.jindouyun.admin.service.AdminOrderService;
import com.jindouyun.common.validator.Order;
import com.jindouyun.common.validator.Sort;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @className: AdminExpressOrderController
 * @description:
 * @author: ZSZ
 * @date: 2020/8/16 13:26
 */
@RestController
@RequestMapping("/admin/expressOrder")
@Validated
public class AdminExpressOrderController {

    @Autowired
    private AdminOrderService adminOrderService;

    /**
     * 订单详情
     *
     * @param orderId
     * @return
     */
    @RequiresPermissions("admin:order:read")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "快递订单详情")
    @GetMapping("/expressOrderDetail")
    public Object queryExpressOrderDetail(@NotNull Integer orderId) {
        return adminOrderService.queryExpressOrderDetail(orderId);
    }

    /**
     * 查询订单
     *
     * @param userId
     * @param orderSn
     * @param orderStatusArray
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @RequiresPermissions("admin:order:list")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "快递订单查询")
    @GetMapping("/listExpressOrder")
    public Object listExpressOrder(Integer userId, String orderSn,
                                   @RequestParam(required = false) List<Short> orderStatusArray,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer limit,
                                   @Sort @RequestParam(defaultValue = "add_time") String sort,
                                   @Order @RequestParam(defaultValue = "desc") String order) {
        return adminOrderService.listExpressOrder(userId, orderSn, orderStatusArray, page, limit, sort, order);
    }

}
