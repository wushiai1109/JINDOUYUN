package com.jindouyun.admin.controller;

import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.service.JindouyunGoodsProductService;
import com.jindouyun.db.service.JindouyunGoodsService;
import com.jindouyun.db.service.JindouyunOrderService;
import com.jindouyun.db.service.JindouyunUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
@Validated
public class AdminDashbordController {
    private final Log logger = LogFactory.getLog(AdminDashbordController.class);

    @Autowired
    private JindouyunUserService userService;
    @Autowired
    private JindouyunGoodsService goodsService;
    @Autowired
    private JindouyunGoodsProductService productService;
    @Autowired
    private JindouyunOrderService orderService;

    @GetMapping("")
    public Object info() {
        int userTotal = userService.count();
        int goodsTotal = goodsService.count();
        int productTotal = productService.count();
        int orderTotal = orderService.count();
        Map<String, Integer> data = new HashMap<>();
        data.put("userTotal", userTotal);
        data.put("goodsTotal", goodsTotal);
        data.put("productTotal", productTotal);
        data.put("orderTotal", orderTotal);

        return ResponseUtil.ok(data);
    }

}
