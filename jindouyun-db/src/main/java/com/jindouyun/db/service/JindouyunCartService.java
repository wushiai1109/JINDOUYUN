package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunCartMapper;
import com.jindouyun.db.domain.JindouyunCart;
import com.jindouyun.db.domain.JindouyunCartExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunCartService {
    @Resource
    private JindouyunCartMapper cartMapper;

    /**
     * 根据 goodsId 、 productId 和 userId 查询 该用户的购物车是否存在该商品
     * @param goodsId
     * @param productId
     * @param userId
     * @return
     */
    public JindouyunCart queryExist(Integer goodsId, Integer productId, Integer userId) {
        JindouyunCartExample example = new JindouyunCartExample();
        example.or().andGoodsIdEqualTo(goodsId).andProductIdEqualTo(productId).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.selectOneByExample(example);
    }

    /**
     * 添加商品到购物车
     * @param cart
     */
    public void add(JindouyunCart cart) {
        cart.setAddTime(LocalDateTime.now());
        cart.setUpdateTime(LocalDateTime.now());
        cartMapper.insertSelective(cart);
    }

    public int updateById(JindouyunCart cart) {
        cart.setUpdateTime(LocalDateTime.now());
        return cartMapper.updateByPrimaryKeySelective(cart);
    }

    /**
     * 查询某一用户的购物车列表
     * @param userId
     * @return
     */
    public List<JindouyunCart> queryByUid(int userId) {
        JindouyunCartExample example = new JindouyunCartExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.selectByExample(example);
    }

    /**
     * 询某一用户的购物车列表并且购物车中商品为选中状态
     * @param userId
     * @return
     */
    public List<JindouyunCart> queryByUidAndChecked(Integer userId) {
        JindouyunCartExample example = new JindouyunCartExample();
        example.or().andUserIdEqualTo(userId).andCheckedEqualTo(true).andDeletedEqualTo(false);
        return cartMapper.selectByExample(example);
    }

    /**
     * 删除购物车中商品列表
     * @param productIdList
     * @param userId
     * @return
     */
    public int delete(List<Integer> productIdList, int userId) {
        JindouyunCartExample example = new JindouyunCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(productIdList);
        return cartMapper.logicalDeleteByExample(example);
    }

    public JindouyunCart findById(Integer id) {
        return cartMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新选中状态
     * @param userId
     * @param idsList
     * @param checked
     * @return
     */
    public int updateCheck(Integer userId, List<Integer> idsList, Boolean checked) {
        JindouyunCartExample example = new JindouyunCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(idsList).andDeletedEqualTo(false);
        JindouyunCart cart = new JindouyunCart();
        cart.setChecked(checked);
        cart.setUpdateTime(LocalDateTime.now());
        return cartMapper.updateByExampleSelective(cart, example);
    }

    /**
     * 清空用户购物车商品
     * @param userId
     */
    public void clearGoods(Integer userId) {
        JindouyunCartExample example = new JindouyunCartExample();
        example.or().andUserIdEqualTo(userId).andCheckedEqualTo(true);
        JindouyunCart cart = new JindouyunCart();
        cart.setDeleted(true);
        cartMapper.updateByExampleSelective(cart, example);
    }

    /**
     * 条件查询
     * @param userId
     * @param goodsId
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunCart> querySelective(Integer userId, Integer goodsId, Integer page, Integer limit, String sort, String order) {
        JindouyunCartExample example = new JindouyunCartExample();
        JindouyunCartExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(goodsId);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return cartMapper.selectByExample(example);
    }

    /**
     * 删除某一用户的购物车
     * @param id
     */
    public void deleteById(Integer id) {
        cartMapper.logicalDeleteByPrimaryKey(id);
    }

    /**
     * 检查商品是否已选中
     * @param goodsId
     * @return
     */
    public boolean checkExist(Integer goodsId) {
        JindouyunCartExample example = new JindouyunCartExample();
        example.or().andGoodsIdEqualTo(goodsId).andCheckedEqualTo(true).andDeletedEqualTo(false);
        return cartMapper.countByExample(example) != 0;
    }

    /**
     * 更新购物车中商品的类型
     * @param id
     * @param goodsSn
     * @param goodsName
     * @param price
     * @param url
     */
    public void updateProduct(Integer id, String goodsSn, String goodsName, BigDecimal price, String url) {
        JindouyunCart cart = new JindouyunCart();
        cart.setPrice(price);
        cart.setPicUrl(url);
        cart.setGoodsSn(goodsSn);
        cart.setGoodsName(goodsName);
        JindouyunCartExample example = new JindouyunCartExample();
        example.or().andProductIdEqualTo(id);
        cartMapper.updateByExampleSelective(cart, example);
    }
}
