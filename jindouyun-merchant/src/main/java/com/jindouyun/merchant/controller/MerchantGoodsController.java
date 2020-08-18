package com.jindouyun.merchant.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.GoodsAllinone;
import com.jindouyun.db.domain.JindouyunGoods;
import com.jindouyun.merchant.service.MerchantGoodsService;
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
        return merchantGoodsService.list(userId);
    }


    /**
     * 编辑商品
     *
     * @param goodsAllinone
     * @return
     */
    @PostMapping("/update")
    public Object update(@RequestBody GoodsAllinone goodsAllinone) {
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
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public Object detail(@NotNull Integer id) {
        return merchantGoodsService.detail(id);

    }

}
