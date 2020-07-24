package com.jindouyun.admin.model.dto;


import com.jindouyun.db.domain.JindouyunGoods;
import com.jindouyun.db.domain.JindouyunGoodsProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsAllinone {
    JindouyunGoods goods;
    JindouyunGoodsProduct[] products;

}
