package com.jindouyun.merchant.controller;

import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunBrand;
import com.jindouyun.db.service.JindouyunBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.jindouyun.merchant.util.ValidateUtil.validate;

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

}
