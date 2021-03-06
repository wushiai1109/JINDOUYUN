package com.jindouyun.merchant.controller;

import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunAddress;
import com.jindouyun.db.domain.JindouyunBrand;
import com.jindouyun.db.service.JindouyunAddressService;
import com.jindouyun.db.service.JindouyunBrandService;
import com.jindouyun.merchant.dto.BrandInfo;
import com.jindouyun.merchant.dto.MerchantInfo;
import com.jindouyun.merchant.service.MerchantUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


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

    @Autowired
    private JindouyunAddressService addressService;

    @PostMapping("/update")
    @Transactional
    public Object update(@LoginUser Integer userId, @RequestBody String body){
        if(userId == null){
            return ResponseUtil.unlogin();
        }
        String name = JacksonUtil.parseString(body,"name");
        String notice = JacksonUtil.parseString(body,"notice");
        Byte startTime = JacksonUtil.parseByte(body,"startTime");
        Byte endTime = JacksonUtil.parseByte(body,"endTime");
        String phone = JacksonUtil.parseString(body,"phone");
        String addressDetail = JacksonUtil.parseString(body,"addressDetail");
        String deliveryPrice = JacksonUtil.parseString(body,"deliveryPrice");
        String desc = JacksonUtil.parseString(body,"desc");
        String picUrl = JacksonUtil.parseString(body,"picUrl");

        JindouyunBrand brand = new JindouyunBrand();
        MerchantInfo merchantInfo = MerchantUserManager.merchantInfoMap.get(userId);
        BrandInfo brandInfo = merchantInfo.getBrandInfo();
        if(brandInfo == null){
            return ResponseUtil.unauthz();
        }
        brand.setId(brandInfo.getId());
        if(!StringUtils.isEmpty(name)){
            brand.setName(name);
            brandInfo.setName(name);
        }
        if(!StringUtils.isEmpty(notice)){
            brand.setNotice(notice);
            brandInfo.setNotice(notice);
        }
        if(startTime != null && endTime != null){
            brand.setStartTime(startTime);
            brand.setEndTime(endTime);
            brandInfo.setStart_time(startTime);
            brandInfo.setEnd_time(endTime);
        }
        if(!StringUtils.isEmpty(desc)){
            brand.setDesc(desc);
            brandInfo.setDesc(desc);
        }
        if(!StringUtils.isEmpty(picUrl)){
            brand.setPicUrl(picUrl);
            brandInfo.setPicUrl(picUrl);
        }
        if(deliveryPrice != null){
            BigDecimal price;
            try{
                price = new BigDecimal(Double.parseDouble(deliveryPrice));
            }catch (Exception e){
                return ResponseUtil.badArgument();
            }
            brand.setDeliveryPrice(price);
        }
        JindouyunAddress address = merchantInfo.getAddress();
        if(!StringUtils.isEmpty(addressDetail)){
            address.setAddressDetail(addressDetail);
        }
        if(!StringUtils.isEmpty(phone)){
            address.setTel(phone);
        }
        if(addressService.update(address) == 0){
            System.err.println("商家信息更新 - 地址信息更新失败");
            return ResponseUtil.fail();
        }
        if(brandService.updateById(brand) == 0){
            System.err.println("商家信息更新 - 信息更新失败");
            return ResponseUtil.fail();
        }

        //更新缓存
        merchantInfo.setAddress(address);
        merchantInfo.setBrandInfo(brandInfo);
        MerchantUserManager.merchantInfoMap.put(userId,merchantInfo);

        return ResponseUtil.ok(merchantInfo);
    }

    @PostMapping("/updateStatus")
    @Transactional
    public Object updateStatus(@LoginUser Integer userId,@RequestBody String body){
        if(userId == null){
            return ResponseUtil.unlogin();
        }
        Byte status = JacksonUtil.parseByte(body,"status");

        if(status == null){
            System.err.println("更新状态 - status为null");
            return ResponseUtil.badArgument();
        }

        JindouyunBrand brand = brandService.findByUserId(userId);
        if(brand == null){
            System.err.println("更新状态 - brand为不存在");
            return ResponseUtil.badArgument();
        }
        brand.setStatus(status);
        if(brandService.updateById(brand) == 0){
            System.err.println("更新状态 - 数据库更新brand失败");
            return ResponseUtil.fail();
        }

        //更新缓存
        MerchantInfo merchantInfo = MerchantUserManager.merchantInfoMap.get(userId);
        BrandInfo brandInfo = merchantInfo.getBrandInfo();
        brandInfo.setStatus((short) status);
        merchantInfo.setBrandInfo(brandInfo);
        MerchantUserManager.merchantInfoMap.put(userId,merchantInfo);

        return ResponseUtil.ok(merchantInfo);

    }

}
