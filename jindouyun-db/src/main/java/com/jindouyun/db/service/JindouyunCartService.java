package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunCartMapper;
import com.jindouyun.db.domain.JindouyunCart;
import com.jindouyun.db.domain.JindouyunCartExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName JindouyunCartService
 * @Description
 * @Author Bruce
 * @Date 2020/7/23 1:33 下午
 */
@Service
@Transactional
public class JindouyunCartService {

    @Autowired
    private JindouyunCartMapper cartMapper;


    public List<JindouyunCart> queryByUid(Integer userId) {
        JindouyunCartExample cartExample = new JindouyunCartExample();
        cartExample.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.selectByExample(cartExample);
    }

    public void deleteById(Integer id) {
        cartMapper.logicalDeleteByPrimaryKey(id);
    }

    public JindouyunCart queryExist(Integer goodsId, Integer productId, Integer userId) {
        JindouyunCartExample cartExample = new JindouyunCartExample();
        cartExample.or().andGoodsIdEqualTo(goodsId).andProductIdEqualTo(productId).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.selectOneByExample(cartExample);
    }

    public void add(JindouyunCart cart) {
        cart.setAddTime(LocalDateTime.now());
        cart.setUpdateTime(LocalDateTime.now());
        cartMapper.insertSelective(cart);
    }

    public int updateById(JindouyunCart existCart) {
        existCart.setUpdateTime(LocalDateTime.now());
        return cartMapper.updateByPrimaryKeySelective(existCart);
    }

    public JindouyunCart findById(Integer id) {
        return cartMapper.selectByPrimaryKey(id);
    }

    public int updateCheck(Integer userId, List<Integer> productIds, Boolean isChecked) {
        JindouyunCartExample example = new JindouyunCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(productIds).andDeletedEqualTo(false);
        JindouyunCart cart = new JindouyunCart();
        cart.setChecked(isChecked);
        cart.setUpdateTime(LocalDateTime.now());
        return cartMapper.updateByExampleSelective(cart, example);
    }

    public int delete(List<Integer> productIds, Integer userId) {
        JindouyunCartExample example = new JindouyunCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(productIds);
        return cartMapper.logicalDeleteByExample(example);
    }

    public List<JindouyunCart> queryByUidAndChecked(Integer userId) {
        JindouyunCartExample example = new JindouyunCartExample();
        example.or().andUserIdEqualTo(userId).andCheckedEqualTo(true).andDeletedEqualTo(false);
        return cartMapper.selectByExample(example);
    }
}
