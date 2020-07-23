package com.jindouyun.wx.service;

import com.jindouyun.db.dao.JindouyunGoodsProductMapper;
import com.jindouyun.db.domain.JindouyunGoodsProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName JindouyunGoodsProductService
 * @Description
 * @Author Bruce
 * @Date 2020/7/23 4:02 下午
 */
@Service
@Transactional
public class JindouyunGoodsProductService {

    @Autowired
    private JindouyunGoodsProductMapper goodsProductMapper;

    public JindouyunGoodsProduct findById(Integer productId) {
        return goodsProductMapper.selectByPrimaryKey(productId);
    }
}
