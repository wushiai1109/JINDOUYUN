package com.jindouyun.admin.controller;

import com.jindouyun.admin.annotation.RequiresPermissionsDesc;
import com.jindouyun.admin.service.AdminOrderService;
import com.jindouyun.core.express.ExpressService;
import com.jindouyun.common.validator.Order;
import com.jindouyun.common.validator.Sort;
import com.jindouyun.core.util.ResponseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/order")
@Validated
public class AdminOrderController {
    private final Log logger = LogFactory.getLog(AdminOrderController.class);

    @Autowired
    private AdminOrderService adminOrderService;
    @Autowired
    private ExpressService expressService;

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
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(Integer userId, String orderSn,
                       @RequestParam(required = false) List<Short> orderStatusArray,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        return adminOrderService.list(userId, orderSn, orderStatusArray, page, limit, sort, order);
    }

//    /**
//     * 查询物流公司
//     *
//     * @return
//     */
//    @GetMapping("/channel")
//    public Object channel() {
//        return ResponseUtil.ok(expressService.getVendors());
//    }

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    @RequiresPermissions("admin:order:read")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "详情")
    @GetMapping("/detail")
    public Object detail(@NotNull Integer id) {
        return adminOrderService.detail(id);
    }

    /**
     * 订单退款
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    @RequiresPermissions("admin:order:refund")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "订单退款")
    @PostMapping("/refund")
    public Object refund(@RequestBody String body) {
        return adminOrderService.refund(body);
    }

//    /**
//     * 发货
//     *
//     * @param body 订单信息，{ orderId：xxx, shipSn: xxx, shipChannel: xxx }
//     * @return 订单操作结果
//     */
//    @RequiresPermissions("admin:order:ship")
//    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "订单发货")
//    @PostMapping("/ship")
//    public Object ship(@RequestBody String body) {
//        return adminOrderService.ship(body);
//    }


//    /**
//     * 回复订单商品
//     *
//     * @param body 订单信息，{ orderId：xxx }
//     * @return 订单操作结果
//     */
//    @RequiresPermissions("admin:order:reply")
//    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "订单商品回复")
//    @PostMapping("/reply")
//    public Object reply(@RequestBody String body) {
//        return adminOrderService.reply(body);
//    }

    @RequiresPermissions("admin:order:list")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "查询")
    @GetMapping("/listMergeOrder")
    public Object list(@RequestParam("type") Byte type,
                       Integer userId, Integer mergeId, String orderSn,Integer brandId,
                       String name, String mobile, Short building, String address,
                       LocalDateTime startTime, LocalDateTime endTime,
                       @RequestParam(required = false) List<Short> orderStatusArray,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        return adminOrderService.select(type, userId, mergeId, orderSn, brandId, name, mobile, building, address, startTime, endTime, orderStatusArray, page, limit, sort, order);
    }

    /**
     * 合并订单
     * @param type
     * @param adminId
     * @param message
     * @param release
     * @param orderIds
     * @return
     */
    @RequiresPermissions("admin:order:merge")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "订单合并")
    @GetMapping("/merge")
    public Object merge(@RequestParam("type") Byte type,@RequestParam("adminId") Integer adminId,
                        @RequestParam(value = "message", required = false) String message,
                        @RequestParam(value = "release") Byte release,
                        @RequestParam("orderIds") List<Integer> orderIds){
        return adminOrderService.merge(type,adminId,message,release,orderIds);

    }

    /**
     * 发布
     * @param mergeId
     * @param adminId
     * @return
     */
    @RequiresPermissions("admin:order:release")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "发布")
    @GetMapping("/release")
    public Object release(@RequestParam("mergeId") Integer mergeId,
                          @RequestParam("adminId") Integer adminId){
        return adminOrderService.release(mergeId,adminId);
    }

    /**
     * 删除
     * @param mergeId
     * @return
     */
    @RequiresPermissions("admin:order:delete")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "删除合并订单")
    @GetMapping("/delete")
    public Object delete(@RequestParam("type") Byte type,
                         @RequestParam("mergeId") Integer mergeId){
        return adminOrderService.delete(type, mergeId);
    }

}
