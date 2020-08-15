package com.jindouyun.merchant.util;

import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.*;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @className: ValidateUtil
 * @description: 校验
 * @author: ZSZ
 * @date: 2020/8/7 14:57
 */
public class ValidateUtil {

    /**
     * 校验商家信息
     * @param brand
     * @return
     */
    public static Object validate(JindouyunBrand brand) {
        String name = brand.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }

        String desc = brand.getDesc();
        if (StringUtils.isEmpty(desc)) {
            return ResponseUtil.badArgument();
        }

//        BigDecimal price = brand.getFloorPrice();
//        if (price == null) {
//            return ResponseUtil.badArgument();
//        }
        return null;
    }


    /**
     * 验证商品
     * @param goodsAllinone
     * @return
     */
    public static Object validate(GoodsAllinone goodsAllinone) {
        JindouyunGoods goods = goodsAllinone.getGoods();
        String name = goods.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        String goodsSn = goods.getGoodsSn();
        if (StringUtils.isEmpty(goodsSn)) {
            return ResponseUtil.badArgument();
        }

        // 商家可以不设置，如果设置则需要验证商家存在
//        Integer brandId = goods.getBrandId();
//        if (brandId != null && brandId != 0) {
//            if (brandService.findById(brandId) == null) {
//                return ResponseUtil.badArgumentValue();
//            }
//        }
//        // 分类可以不设置，如果设置则需要验证分类存在
//        Integer categoryId = goods.getCategoryId();
//        if (categoryId != null && categoryId != 0) {
//            if (categoryService.findById(categoryId) == null) {
//                return ResponseUtil.badArgumentValue();
//            }
//        }

//        验证商品属性
        JindouyunGoodsAttribute[] attributes = goodsAllinone.getAttributes();
        for (JindouyunGoodsAttribute attribute : attributes) {
            validate(attribute);
        }

        //验证商品Specification
        JindouyunGoodsSpecification[] specifications = goodsAllinone.getSpecifications();
        for (JindouyunGoodsSpecification specification : specifications) {
            validate(specification);
        }

        //验证商品规格
        JindouyunGoodsProduct[] products = goodsAllinone.getProducts();
        for (JindouyunGoodsProduct product : products) {
            validate(product);
        }

        return null;
    }

    /**
     * 检验属性
     * @param attribute
     * @return
     */
    public static Object validate(JindouyunGoodsAttribute attribute){
        String attr = attribute.getAttribute();
        if (StringUtils.isEmpty(attr)) {
            return ResponseUtil.badArgument();
        }
        String value = attribute.getValue();
        if (StringUtils.isEmpty(value)) {
            return ResponseUtil.badArgument();
        }
        return  null;
    }

    /**
     * 检验规格
     * @param product
     * @return
     */
    public static Object validate(JindouyunGoodsProduct product){
        Integer number = product.getNumber();
        if (number == null || number < 0) {
            return ResponseUtil.badArgument();
        }

        BigDecimal price = product.getPrice();
        if (price == null) {
            return ResponseUtil.badArgument();
        }

        String[] productSpecifications = product.getSpecifications();
        if (productSpecifications.length == 0) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    /**
     * 检验 Specification
     * @param goodsSpecification
     * @return
     */
    public static Object validate(JindouyunGoodsSpecification goodsSpecification){
        String specification = goodsSpecification.getSpecification();
        if(!StringUtils.isEmpty(specification)){
            return ResponseUtil.badArgument();
        }
        String value = goodsSpecification.getValue();
        if(!StringUtils.isEmpty(value)){
            return ResponseUtil.badArgument();
        }
        return null;
    }

}
