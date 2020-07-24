package com.jindouyun.wx.controller;

import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.core.validator.Order;
import com.jindouyun.core.validator.Sort;
import com.jindouyun.db.domain.JindouyunBrand;
import com.jindouyun.db.service.JindouyunBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName WxBrandController
 * @Description 专题服务
 * @Author Bruce
 * @Date 2020/7/23 1:17 下午
 */
@RestController
@RequestMapping("/wx/brand")
@CrossOrigin(origins = "*",maxAge = 3600)
public class WxBrandController {
    
    @Autowired
    private JindouyunBrandService brandService;

    /**
     * 品牌列表
     *
     * @param page 分页页数
     * @param limit 分页大小
     * @return 品牌列表
     */
    @GetMapping("list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<JindouyunBrand> brandList = brandService.query(page, limit, sort, order);
        return ResponseUtil.okList(brandList);
    }

    /**
     * 品牌详情
     *
     * @param id 品牌ID
     * @return 品牌详情
     */
    @GetMapping("detail")
    public Object detail(@NotNull Integer id) {
        JindouyunBrand entity = brandService.findById(id);
        if (entity == null) {
            return ResponseUtil.badArgumentValue();
        }

        return ResponseUtil.ok(entity);
    }
    
}
