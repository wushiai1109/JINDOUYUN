package com.jindouyun.db.domain;

import com.jindouyun.db.domain.BrandVo;
import com.jindouyun.db.domain.JindouyunOrderGoods;
import com.jindouyun.db.domain.JindouyunOrderSplit;

import java.math.BigDecimal;
import java.util.List;

/**
 * @className: OrderSplitVO
 * @description:
 * @author: ZSZ
 * @date: 2020/8/12 14:03
 */
public class OrderSplitVO {

    private BrandVo brandVo;
    private JindouyunOrderSplit orderSplit;
    private List<JindouyunOrderGoods> orderGoods;
    private BigDecimal deliveryPrice;

    public OrderSplitVO() {
    }

    public OrderSplitVO(BrandVo brandVo, JindouyunOrderSplit orderSplit, List<JindouyunOrderGoods> orderGoods) {
        this.brandVo = brandVo;
        this.orderSplit = orderSplit;
        this.orderGoods = orderGoods;
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public BrandVo getBrandVo() {
        return brandVo;
    }

    public void setBrandVo(BrandVo brandVo) {
        this.brandVo = brandVo;
    }

    public JindouyunOrderSplit getOrderSplit() {
        return orderSplit;
    }

    public void setOrderSplit(JindouyunOrderSplit orderSplit) {
        this.orderSplit = orderSplit;
    }

    public List<JindouyunOrderGoods> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(List<JindouyunOrderGoods> orderGoods) {
        this.orderGoods = orderGoods;
    }
}
