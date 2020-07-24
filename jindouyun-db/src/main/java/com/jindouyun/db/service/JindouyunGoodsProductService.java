package com.jindouyun.db.service;

import com.jindouyun.db.dao.GoodsProductMapper;
import com.jindouyun.db.dao.JindouyunGoodsProductMapper;
import com.jindouyun.db.domain.JindouyunGoodsProduct;
import com.jindouyun.db.domain.JindouyunGoodsProductExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunGoodsProductService {
    @Resource
    private JindouyunGoodsProductMapper jindouyunGoodsProductMapper;
    @Resource
    /**
     * 库存相关
     */
    private GoodsProductMapper goodsProductMapper;

    /**
     * 通过goodId 查询所有规格
     * @param gid
     * @return
     */
    public List<JindouyunGoodsProduct> queryByGid(Integer gid) {
        JindouyunGoodsProductExample example = new JindouyunGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid).andDeletedEqualTo(false);
        return jindouyunGoodsProductMapper.selectByExample(example);
    }

    public JindouyunGoodsProduct findById(Integer id) {
        return jindouyunGoodsProductMapper.selectByPrimaryKey(id);
    }

    public void deleteById(Integer id) {
        jindouyunGoodsProductMapper.logicalDeleteByPrimaryKey(id);
    }

    /**
     * 添加
     * @param goodsProduct
     */
    public void add(JindouyunGoodsProduct goodsProduct) {
        goodsProduct.setAddTime(LocalDateTime.now());
        goodsProduct.setUpdateTime(LocalDateTime.now());
        jindouyunGoodsProductMapper.insertSelective(goodsProduct);
    }

    /**
     * 查询数量
     * @return
     */
    public int count() {
        JindouyunGoodsProductExample example = new JindouyunGoodsProductExample();
        example.or().andDeletedEqualTo(false);
        return (int) jindouyunGoodsProductMapper.countByExample(example);
    }

    /**
     * 根据 goodId 删除规格
     * @param gid
     */
    public void deleteByGid(Integer gid) {
        JindouyunGoodsProductExample example = new JindouyunGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid);
        jindouyunGoodsProductMapper.logicalDeleteByExample(example);
    }

    /**
     * 增加 该规格商品的库存
     * @param id
     * @param num
     * @return
     */
    public int addStock(Integer id, Short num){
        return goodsProductMapper.addStock(id, num);
    }

    /**
     * 减少 该规格商品的库存
     * @param id
     * @param num
     * @return
     */
    public int reduceStock(Integer id, Short num){
        return goodsProductMapper.reduceStock(id, num);
    }


    public void updateById(JindouyunGoodsProduct product) {
        product.setUpdateTime(LocalDateTime.now());
        jindouyunGoodsProductMapper.updateByPrimaryKeySelective(product);
    }
}