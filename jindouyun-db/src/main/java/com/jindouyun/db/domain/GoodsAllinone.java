package com.jindouyun.db.domain;


import com.jindouyun.db.domain.JindouyunGoods;
import com.jindouyun.db.domain.JindouyunGoodsAttribute;
import com.jindouyun.db.domain.JindouyunGoodsProduct;
import com.jindouyun.db.domain.JindouyunGoodsSpecification;

public class GoodsAllinone {
    JindouyunGoods goods;
    JindouyunGoodsSpecification[] specifications;
    JindouyunGoodsAttribute[] attributes;
    JindouyunGoodsProduct[] products;

    public JindouyunGoods getGoods() {
        return goods;
    }

    public void setGoods(JindouyunGoods goods) {
        this.goods = goods;
    }

    public JindouyunGoodsSpecification[] getSpecifications() {
        return specifications;
    }

    public void setSpecifications(JindouyunGoodsSpecification[] specifications) {
        this.specifications = specifications;
    }

    public JindouyunGoodsAttribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(JindouyunGoodsAttribute[] attributes) {
        this.attributes = attributes;
    }

    public JindouyunGoodsProduct[] getProducts() {
        return products;
    }

    public void setProducts(JindouyunGoodsProduct[] products) {
        this.products = products;
    }
}
