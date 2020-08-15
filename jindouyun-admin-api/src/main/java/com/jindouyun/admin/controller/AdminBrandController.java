package com.jindouyun.admin.controller;

import com.jindouyun.admin.annotation.RequiresPermissionsDesc;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.common.validator.Order;
import com.jindouyun.common.validator.Sort;
import com.jindouyun.db.domain.JindouyunBrand;
import com.jindouyun.db.service.JindouyunBrandService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static com.jindouyun.admin.util.ValidateUtil.validate;

@RestController
@RequestMapping("/admin/brand")
@Validated
public class AdminBrandController {
    private final Log logger = LogFactory.getLog(AdminBrandController.class);

    @Autowired
    private JindouyunBrandService brandService;

    @RequiresPermissions("admin:brand:list")
    @RequiresPermissionsDesc(menu = {"商场管理", "外卖店家管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String id, String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort(accepts = {"id","delivery_price","floor_price","sort_order","add_time"}) @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<JindouyunBrand> brandList = brandService.querySelective(id, name, page, limit, sort, order);
        return ResponseUtil.okList(brandList);
    }

    @RequiresPermissions("admin:brand:detail")
    @RequiresPermissionsDesc(menu = {"商场管理", "外卖店家管理"}, button = "详情")
    @GetMapping("/detail")
    public Object read(@NotNull Integer id) {
        JindouyunBrand brand = brandService.findById(id);
        return ResponseUtil.ok(brand);
    }


    @RequiresPermissions("admin:brand:delete")
    @RequiresPermissionsDesc(menu = {"商场管理", "外卖店家管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody String body) {
        Integer id = JacksonUtil.parseInteger(body,"id");
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        brandService.deleteById(id);
        return ResponseUtil.ok();
    }

}
