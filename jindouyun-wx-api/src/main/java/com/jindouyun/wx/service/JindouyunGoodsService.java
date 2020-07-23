package com.jindouyun.wx.service;

import com.jindouyun.db.dao.JindouyunGoodsMapper;
import com.jindouyun.db.domain.JindouyunGoods;
import com.jindouyun.db.domain.JindouyunGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName JindouyunGoodsService
 * @Description
 * @Author Bruce
 * @Date 2020/7/21 8:03 下午
 */
@Service
@Transactional
public class JindouyunGoodsService {

    @Autowired
    private JindouyunGoodsMapper goodsMapper;


    /**
     * selectOneByExample与selectOneByExampleWithBLOBs:
     * 获取某个商品信息,包含完整信息
     * 1、两个方法的返回的resultMap 不同
     * selectByExample  方法返回：BaseResultMap。
     * selectByExampleWithBLOBs  方法返回：ResultMapWithBLOBs。
     * ResultMapWithBLOBs 定义时，继承了BaseResultMap，并且自己特殊的字段，该字段通常是longvarchar类型。
     *
     * 2、使用场景不同
     * 若检索大字段时，则需要使用selectByExampleWithBLOBs  ，一般情况则使用selectByExample  即可。
     *
     * @param goodsId
     * @return
     */
    public JindouyunGoods findById(Integer goodsId) {
        JindouyunGoodsExample goodsExample = new JindouyunGoodsExample();
        goodsExample.or().andIdEqualTo(goodsId).andDeletedEqualTo(false);
        return goodsMapper.selectOneByExample(goodsExample);
    }
}
