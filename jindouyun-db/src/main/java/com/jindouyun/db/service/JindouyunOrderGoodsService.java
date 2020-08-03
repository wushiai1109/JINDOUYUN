package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunOrderGoodsMapper;
import com.jindouyun.db.domain.JindouyunOrder;
import com.jindouyun.db.domain.JindouyunOrderGoods;
import com.jindouyun.db.domain.JindouyunOrderGoodsExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunOrderGoodsService {
    @Resource
    private JindouyunOrderGoodsMapper orderGoodsMapper;

    /**
     * 添加
     * @param orderGoods
     * @return
     */
    public int add(JindouyunOrderGoods orderGoods) {
        orderGoods.setAddTime(LocalDateTime.now());
        orderGoods.setUpdateTime(LocalDateTime.now());
        return orderGoodsMapper.insertSelective(orderGoods);
    }

    /**
     * 查询该订单中的商品 根据 orderId
     * @param orderId
     * @return
     */
    public List<JindouyunOrderGoods> queryByOid(Integer orderId) {
        JindouyunOrderGoodsExample example = new JindouyunOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        return orderGoodsMapper.selectByExample(example);
    }

    /**
     * 查询订单商品列表
     * @param orderId
     * @param goodsId
     * @return
     */
    public List<JindouyunOrderGoods> findByOidAndGid(Integer orderId, Integer goodsId) {
        JindouyunOrderGoodsExample example = new JindouyunOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return orderGoodsMapper.selectByExample(example);
    }

    public JindouyunOrderGoods findById(Integer id) {
        return orderGoodsMapper.selectByPrimaryKey(id);
    }

    public void updateById(JindouyunOrderGoods orderGoods) {
        orderGoods.setUpdateTime(LocalDateTime.now());
        orderGoodsMapper.updateByPrimaryKeySelective(orderGoods);
    }

    /**
     * 获取 数量
     * @param orderId
     * @return
     */
    public Short getComments(Integer orderId) {
        JindouyunOrderGoodsExample example = new JindouyunOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        long count = orderGoodsMapper.countByExample(example);
        return (short) count;
    }

    /**
     * 检查是否存在 该订单商品 goodsId
     * @param goodsId
     * @return
     */
    public boolean checkExist(Integer goodsId) {
        JindouyunOrderGoodsExample example = new JindouyunOrderGoodsExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return orderGoodsMapper.countByExample(example) != 0;
    }
}
