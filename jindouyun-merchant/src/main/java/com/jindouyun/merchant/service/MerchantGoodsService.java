package com.jindouyun.merchant.service;

import com.jindouyun.core.qcode.QCodeService;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jindouyun.merchant.util.ValidateUtil.validate;


@Service
public class MerchantGoodsService {
    private final Log logger = LogFactory.getLog(MerchantGoodsService.class);

    @Autowired
    private JindouyunGoodsService goodsService;
    @Autowired
    private JindouyunGoodsSpecificationService specificationService;
    @Autowired
    private JindouyunGoodsAttributeService attributeService;
    @Autowired
    private JindouyunGoodsProductService productService;
    @Autowired
    private JindouyunCategoryService categoryService;
    @Autowired
    private JindouyunBrandService brandService;
    @Autowired
    private JindouyunCartService cartService;


    /**
     * @param brandId
     * @return
     */
    public Object list(Integer brandId) {
        List<JindouyunGoods> goodsList = goodsService.queryByBrandId(brandId);
        return ResponseUtil.ok(goodsList);
    }

    /**
     * 编辑商品
     *
     * NOTE：
     * 由于商品涉及到四个表，特别是Jindouyun_goods_product表依赖Jindouyun_goods_specification表，
     * 这导致允许所有字段都是可编辑会带来一些问题，因此这里商品编辑功能是受限制：
     * （1）Jindouyun_goods表可以编辑字段；
     * （2）Jindouyun_goods_specification表只能编辑pic_url字段，其他操作不支持；
     * （3）Jindouyun_goods_product表只能编辑price, number和url字段，其他操作不支持；
     * （4）Jindouyun_goods_attribute表支持编辑、添加和删除操作。
     *
     * NOTE2:
     * 前后端这里使用了一个小技巧：
     * 如果前端传来的update_time字段是空，则说明前端已经更新了某个记录，则这个记录会更新；
     * 否则说明这个记录没有编辑过，无需更新该记录。
     *
     * NOTE3:
     * （1）购物车缓存了一些商品信息，因此需要及时更新。
     * 目前这些字段是goods_sn, goods_name, price, pic_url。
     * （2）但是订单里面的商品信息则是不会更新。
     * 如果订单是未支付订单，此时仍然以旧的价格支付。
     */
    @Transactional
    public Object update(GoodsAllinone goodsAllinone) {
        Object error = validate(goodsAllinone);
        if (error != null) {
            return error;
        }

        JindouyunGoods goods = goodsAllinone.getGoods();

        Integer brandId = goods.getBrandId();
        if (brandId != null && brandId != 0) {
            if (brandService.findById(brandId) == null) {
                return ResponseUtil.badArgumentValue();
            }
        }
        // 分类可以不设置，如果设置则需要验证分类存在
//        Integer categoryId = goods.getCategoryId();
//        if (categoryId != null && categoryId != 0) {
//            if (categoryService.findById(categoryId) == null) {
//                return ResponseUtil.badArgumentValue();
//            }
//        }

        JindouyunGoodsAttribute[] attributes = goodsAllinone.getAttributes();
        JindouyunGoodsSpecification[] specifications = goodsAllinone.getSpecifications();
        JindouyunGoodsProduct[] products = goodsAllinone.getProducts();

        //将生成的分享图片地址写入数据库
//        String url = qCodeService.createGoodShareImage(goods.getId().toString(), goods.getPicUrl(), goods.getName());
//        goods.setShareUrl(url);

        // 商品表里面有一个字段nowPrice记录当前商品的最低价
        BigDecimal retailPrice = new BigDecimal(Integer.MAX_VALUE);
        for (JindouyunGoodsProduct product : products) {
            BigDecimal productPrice = product.getPrice();
            if(retailPrice.compareTo(productPrice) == 1){
                retailPrice = productPrice;
            }
        }
        goods.setNowPrice(retailPrice);
        
        // 商品基本信息表Jindouyun_goods
        if (goodsService.updateById(goods) == 0) {
            throw new RuntimeException("更新数据失败");
        }

        Integer gid = goods.getId();

        // 商品规格表Jindouyun_goods_specification
        for (JindouyunGoodsSpecification specification : specifications) {
            // 目前只支持更新规格表的图片字段
            if(specification.getUpdateTime() == null){
                specificationService.updateById(specification);
            }
        }

        // 商品货品表Jindouyun_product
        for (JindouyunGoodsProduct product : products) {
            if(product.getUpdateTime() == null) {
                productService.updateById(product);
            }
        }

        // 商品参数表Jindouyun_goods_attribute
        for (JindouyunGoodsAttribute attribute : attributes) {
            if (attribute.getId() == null || attribute.getId().equals(0)){
                attribute.setGoodsId(goods.getId());
                attributeService.add(attribute);
            }
            else if(attribute.getDeleted()){
                attributeService.deleteById(attribute.getId());
            }
            else if(attribute.getUpdateTime() == null){
                attributeService.updateById(attribute);
            }
        }

        // 这里需要注意的是购物车Jindouyun_cart有些字段是拷贝商品的一些字段，因此需要及时更新
        // 目前这些字段是goods_sn, goods_name, price, pic_url
        for (JindouyunGoodsProduct product : products) {
            cartService.updateProduct(product.getId(), goods.getGoodsSn(), goods.getName(), product.getPrice(), product.getUrl());
        }

        return ResponseUtil.ok();
    }

    @Transactional
    public Object delete(JindouyunGoods goods) {
        Integer gid = goods.getId();
        if (gid == null) {
            return ResponseUtil.badArgument();
        }

        gid = goods.getId();
        goodsService.deleteById(gid);
        specificationService.deleteByGid(gid);
        attributeService.deleteByGid(gid);
        productService.deleteByGid(gid);
        return ResponseUtil.ok();
    }

    @Transactional
    public Object create(GoodsAllinone goodsAllinone) {
        Object error = validate(goodsAllinone);
        if (error != null) {
            return error;
        }

        JindouyunGoods goods = goodsAllinone.getGoods();
        JindouyunGoodsAttribute[] attributes = goodsAllinone.getAttributes();
        JindouyunGoodsSpecification[] specifications = goodsAllinone.getSpecifications();
        JindouyunGoodsProduct[] products = goodsAllinone.getProducts();

        String name = goods.getName();
        if (goodsService.checkExistByName(name)) {
            //GOODS_NAME_EXIST = 611
            return ResponseUtil.fail(611, "商品名已经存在");
        }

        // 商品表里面有一个字段nowPrice记录当前商品的最低价
        BigDecimal retailPrice = new BigDecimal(Integer.MAX_VALUE);
        for (JindouyunGoodsProduct product : products) {
            BigDecimal productPrice = product.getPrice();
            if(retailPrice.compareTo(productPrice) == 1){
                retailPrice = productPrice;
            }
        }
        goods.setNowPrice(retailPrice);

        // 商品基本信息表Jindouyun_goods
        goodsService.add(goods);

        //将生成的分享图片地址写入数据库
//        String url = qCodeService.createGoodShareImage(goods.getId().toString(), goods.getPicUrl(), goods.getName());
//        if (!StringUtils.isEmpty(url)) {
//            goods.setShareUrl(url);
//            if (goodsService.updateById(goods) == 0) {
//                throw new RuntimeException("更新数据失败");
//            }
//        }

        // 商品规格表Jindouyun_goods_specification
        for (JindouyunGoodsSpecification specification : specifications) {
            specification.setGoodsId(goods.getId());
            specificationService.add(specification);
        }

        // 商品参数表Jindouyun_goods_attribute
        for (JindouyunGoodsAttribute attribute : attributes) {
            attribute.setGoodsId(goods.getId());
            attributeService.add(attribute);
        }

        // 商品货品表Jindouyun_product
        for (JindouyunGoodsProduct product : products) {
            product.setGoodsId(goods.getId());
            productService.add(product);
        }
        return ResponseUtil.ok();
    }


    /**
     * 返回商品细节
     * @param id
     * @return
     */
    public Object detail(Integer id) {
        JindouyunGoods goods = goodsService.findById(id);
        List<JindouyunGoodsProduct> products = productService.queryByGid(id);
        List<JindouyunGoodsSpecification> specifications = specificationService.queryByGid(id);
        List<JindouyunGoodsAttribute> attributes = attributeService.queryByGid(id);

        Integer categoryId = goods.getCategoryId();
        JindouyunCategory category = categoryService.findById(categoryId);
        Integer[] categoryIds = new Integer[]{};
        if (category != null) {
            Integer parentCategoryId = category.getPid();
            categoryIds = new Integer[]{parentCategoryId, categoryId};
        }

        Map<String, Object> data = new HashMap<>();
        data.put("goods", goods);
        data.put("specifications", specifications);
        data.put("products", products);
        data.put("attributes", attributes);
        data.put("categoryIds", categoryIds);

        return ResponseUtil.ok(data);
    }

}
