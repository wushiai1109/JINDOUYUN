package com.jindouyun.merchant.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.GoodsAllinone;
import com.jindouyun.db.domain.JindouyunGoods;
import com.jindouyun.merchant.dto.BrandInfo;
import com.jindouyun.merchant.service.MerchantGoodsService;
import com.jindouyun.merchant.service.MerchantUserManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/merchant/goods")
@Validated
public class MerchantGoodsController {
    private final Log logger = LogFactory.getLog(MerchantGoodsController.class);

    @Autowired
    private MerchantGoodsService merchantGoodsService;

    /**
     * 查询商品
     *
     * @param userId
     * @return
     */
    @GetMapping("/list")
    public Object list(@LoginUser Integer userId) {
        if(userId == null){
            return ResponseUtil.unlogin();
        }
        BrandInfo brandInfo = MerchantUserManager.merchantInfoMap.get(userId).getBrandInfo();
        return merchantGoodsService.list(brandInfo.getId());
    }


    /**
     * 编辑商品
     *
     * @param goodsAllinone
     * @return
     */
    @PostMapping("/update")
    public Object update(@RequestBody GoodsAllinone goodsAllinone) {
        goodsAllinone.setAttributes(null);
        return merchantGoodsService.update(goodsAllinone);
    }

    /**
     * 删除商品
     *
     * @param goods
     * @return
     */
    @PostMapping("/delete")
    public Object delete(@RequestBody JindouyunGoods goods) {
        return merchantGoodsService.delete(goods);
    }

    /**
     * 添加商品
     *
     * @param goodsAllinone
     * @return
     */
    @PostMapping("/create")
    public Object create(@RequestBody GoodsAllinone goodsAllinone) {
        return merchantGoodsService.create(goodsAllinone);
    }

    /**
     * 商品详情
     *
     * @param goodId
     * @return
     */
    @GetMapping("/detail")
    public Object detail(@NotNull Integer goodId) {
        return merchantGoodsService.detail(goodId);
    }

}
