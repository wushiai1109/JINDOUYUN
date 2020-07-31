package com.jindouyun.admin.service;

import com.jindouyun.admin.model.dto.GoodsAllinone;
import com.jindouyun.admin.model.vo.CatVo;
import com.jindouyun.core.qcode.QCodeService;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jindouyun.admin.util.AdminResponseCode.GOODS_NAME_EXIST;


@Service
public class AdminGoodsService {
    private final Log logger = LogFactory.getLog(AdminGoodsService.class);

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
    @Autowired
    private QCodeService qCodeService;


    /**
     * 条件查询 goodsId or goodsSn or name
     * @param goodsId
     * @param goodsSn
     * @param name
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public Object list(Integer goodsId, String goodsSn, String name,
                       Integer page, Integer limit, String sort, String order) {
        List<JindouyunGoods> goodsList = goodsService.querySelective(goodsId, goodsSn, name, page, limit, sort, order);
        return ResponseUtil.okList(goodsList);
    }

    /**
     * 验证商品
     * @param goodsAllinone
     * @return
     */
    private Object validate(GoodsAllinone goodsAllinone) {
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
        Integer brandId = goods.getBrandId();
        if (brandId != null && brandId != 0) {
            if (brandService.findById(brandId) == null) {
                return ResponseUtil.badArgumentValue();
            }
        }
        // 分类可以不设置，如果设置则需要验证分类存在
        Integer categoryId = goods.getCategoryId();
        if (categoryId != null && categoryId != 0) {
            if (categoryService.findById(categoryId) == null) {
                return ResponseUtil.badArgumentValue();
            }
        }

//        验证商品属性
        JindouyunGoodsAttribute[] attributes = goodsAllinone.getAttributes();
        for (JindouyunGoodsAttribute attribute : attributes) {
            String attr = attribute.getAttribute();
            if (StringUtils.isEmpty(attr)) {
                return ResponseUtil.badArgument();
            }
            String value = attribute.getValue();
            if (StringUtils.isEmpty(value)) {
                return ResponseUtil.badArgument();
            }
        }

        //验证商品Specification
        JindouyunGoodsSpecification[] specifications = goodsAllinone.getSpecifications();
        for (JindouyunGoodsSpecification specification : specifications) {
            String spec = specification.getSpecification();
            if (StringUtils.isEmpty(spec)) {
                return ResponseUtil.badArgument();
            }
            String value = specification.getValue();
            if (StringUtils.isEmpty(value)) {
                return ResponseUtil.badArgument();
            }
        }

        //验证商品规格
        JindouyunGoodsProduct[] products = goodsAllinone.getProducts();
        for (JindouyunGoodsProduct product : products) {
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
        }

        return null;
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
                specification.setSpecification(null);
                specification.setValue(null);
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
        Integer id = goods.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }

        Integer gid = goods.getId();
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
            return ResponseUtil.fail(GOODS_NAME_EXIST, "商品名已经存在");
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


    public Object list2() {
        // http://element-cn.eleme.io/#/zh-CN/component/cascader
        // 管理员设置“所属分类”
        List<JindouyunCategory> l1CatList = categoryService.queryL1();
        List<CatVo> categoryList = new ArrayList<>(l1CatList.size());

        for (JindouyunCategory l1 : l1CatList) {
            CatVo l1CatVo = new CatVo();
            l1CatVo.setValue(l1.getId());
            l1CatVo.setLabel(l1.getName());

            List<JindouyunCategory> l2CatList = categoryService.queryByPid(l1.getId());
            List<CatVo> children = new ArrayList<>(l2CatList.size());
            for (JindouyunCategory l2 : l2CatList) {
                CatVo l2CatVo = new CatVo();
                l2CatVo.setValue(l2.getId());
                l2CatVo.setLabel(l2.getName());
                children.add(l2CatVo);
            }
            l1CatVo.setChildren(children);

            categoryList.add(l1CatVo);
        }

        // http://element-cn.eleme.io/#/zh-CN/component/select
        // 管理员设置“所属品牌商”
        List<JindouyunBrand> list = brandService.all();
        List<Map<String, Object>> brandList = new ArrayList<>(l1CatList.size());
        for (JindouyunBrand brand : list) {
            Map<String, Object> b = new HashMap<>(2);
            b.put("value", brand.getId());
            b.put("label", brand.getName());
            brandList.add(b);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("categoryList", categoryList);
        data.put("brandList", brandList);
        return ResponseUtil.ok(data);
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
