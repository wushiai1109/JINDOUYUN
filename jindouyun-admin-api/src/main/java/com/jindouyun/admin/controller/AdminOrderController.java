package com.jindouyun.admin.controller;

import com.jindouyun.admin.annotation.RequiresPermissionsDesc;
import com.jindouyun.admin.service.AdminOrderService;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.core.express.ExpressService;
import com.jindouyun.common.validator.Order;
import com.jindouyun.common.validator.Sort;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunExpressOrder;
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
     * @param orderId
     * @return
     */
    @RequiresPermissions("admin:order:read")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "用户订单详情")
    @GetMapping("/detail")
    public Object detail(@NotNull Integer orderId) {
        return adminOrderService.detail(orderId);
    }


    @RequiresPermissions("admin:order:read")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "详情")
    @GetMapping("/detailSplitOrder")
    public Object detailSplitOrder(@NotNull Integer orderSplitId) {
        return adminOrderService.queryDetailBySplitId(orderSplitId);
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
                       @RequestParam(defaultValue = "") LocalDateTime startTime,
                       @RequestParam(defaultValue = "") LocalDateTime endTime,
                       @RequestParam(required = false) List<Short> orderStatusArray,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        return adminOrderService.select(type, userId, mergeId, orderSn, brandId, name, mobile, building, address, startTime, endTime, orderStatusArray, page, limit, sort, order);
    }

    /**
     * 合并订单
     * @param body
     *          {
     *              "type":0,
     *              "message":"xxx",
     *              "release":1,
     *              "orderIds":[11,12]
     *          }
     * @return
     */
    @RequiresPermissions("admin:order:merge")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "订单合并")
    @PostMapping("/merge")
    public Object merge(@RequestBody String body){
        Byte type = JacksonUtil.parseByte(body,"type");
        String message = JacksonUtil.parseString(body,"message");
        Byte release = JacksonUtil.parseByte(body,"release");
        List<Integer> orderIds = JacksonUtil.parseIntegerList(body,"orderIds");
        return adminOrderService.merge(type,message,release,orderIds);

    }

    /**
     * 发布
     * @param body {"mergeId":8}
     * @return
     */
    @RequiresPermissions("admin:order:release")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "发布")
    @PostMapping("/release")
    public Object release(@RequestBody String body){
        Integer mergeId = JacksonUtil.parseInteger(body,"mergeId");
        return adminOrderService.release(mergeId);
    }

    /**
     * 删除
     * @param body {"type":0,"mergeId":8}
     * @return
     */
    @RequiresPermissions("admin:order:delete")
    @RequiresPermissionsDesc(menu = {"商场管理", "订单管理"}, button = "删除合并订单")
    @PostMapping("/delete")
    public Object delete(@RequestBody String body){
        Byte type = JacksonUtil.parseByte(body,"type");
        Integer mergeId = JacksonUtil.parseInteger(body,"mergeId");
        return adminOrderService.delete(type, mergeId);
    }

}
