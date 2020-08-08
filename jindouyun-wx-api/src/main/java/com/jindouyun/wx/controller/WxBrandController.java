package com.jindouyun.wx.controller;

import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.common.validator.Order;
import com.jindouyun.common.validator.Sort;
import com.jindouyun.db.domain.JindouyunBrand;
import com.jindouyun.db.domain.JindouyunCoupon;
import com.jindouyun.db.domain.JindouyunOrder;
import com.jindouyun.db.domain.JindouyunOrderGoods;
import com.jindouyun.db.service.JindouyunBrandService;
import com.jindouyun.db.service.JindouyunCouponService;
import com.jindouyun.db.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WxBrandController
 * @Description 专题服务
 * @Author Bruce
 * @Date 2020/7/23 1:17 下午
 */
@RestController
@RequestMapping("/wx/brand")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WxBrandController {

    @Autowired
    private JindouyunBrandService brandService;

    @Autowired
    private JindouyunCouponService couponService;

    /**
     * 品牌列表
     *
     * @param page  分页页数
     * @param limit 分页大小
     * @return 品牌列表
     */
    @GetMapping("list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<JindouyunBrand> brandList = brandService.query(page, limit, sort, order);

        List<Map<String, Object>> mapList = new ArrayList<>();

        for (JindouyunBrand brand : brandList) {
            Map<String, Object> map = new HashMap<>();
            List<JindouyunCoupon> couponList = couponService.findByBrandId(brand.getId());
            map.put("id", brand.getId());
            map.put("name", brand.getName());
            map.put("desc", brand.getDesc());
            map.put("notice", brand.getNotice());
            map.put("picUrl", brand.getPicUrl());
            map.put("floorPrice", brand.getFloorPrice());
            map.put("couponList", couponList);
            mapList.add(map);
        }
        return ResponseUtil.okList(mapList);
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
//        List<Map<String, Object>> mapList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        List<JindouyunCoupon> couponList = couponService.findByBrandId(id);
        map.put("id", entity.getId());
        map.put("userId", entity.getUserId());
        map.put("addressId", entity.getAdderssId());
        map.put("name", entity.getName());
        map.put("desc", entity.getDesc());
        map.put("notice", entity.getNotice());
        map.put("picUrl", entity.getPicUrl());
        map.put("startTime", entity.getStartTime());
        map.put("endTime", entity.getEndTime());
        map.put("deliveryPrice", entity.getDeliveryPrice());
        map.put("totalTurnover", entity.getTotalTurnover());
//        map.put("todayTurnover", entity.getTodayTurnover());
        map.put("totalOrder", entity.getTotalOrder());
//        map.put("todayOrder", entity.getTodayOrder());
        map.put("floorPrice", entity.getFloorPrice());
        map.put("sortOrder", entity.getSortOrder());
        map.put("status", entity.getStatus());
        map.put("couponList", couponList);
//        mapList.add(map);

        return ResponseUtil.ok(map);
    }

}
