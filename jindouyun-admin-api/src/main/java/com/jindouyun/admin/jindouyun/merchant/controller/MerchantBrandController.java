package com.jindouyun.admin.jindouyun.merchant.controller;

import com.jindouyun.admin.annotation.RequiresPermissionsDesc;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunBrand;
import com.jindouyun.db.service.JindouyunBrandService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.jindouyun.admin.util.ValidateUtil.validate;

/**
 * @className:
 * @description:
 * @author: ZSZ
 * @date: 2020/8/6 15:10
 */
@RestController
@RequestMapping("/merchant/brand")
@Validated
public class MerchantBrandController {

    @Autowired
    private JindouyunBrandService brandService;

    @RequiresPermissions("admin:brand:update")
    @RequiresPermissionsDesc(menu = {"商场管理", "品牌管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody JindouyunBrand brand) {
        Object error = validate(brand);
        if (error != null) {
            return error;
        }
        if (brandService.updateById(brand) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(brand);
    }

    @RequiresPermissions("admin:brand:create")
    @RequiresPermissionsDesc(menu = {"商场管理", "品牌管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody JindouyunBrand brand) {
        Object error = validate(brand);
        if (error != null) {
            return error;
        }
        brandService.add(brand);
        return ResponseUtil.ok(brand);
    }





}
