package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunGoodsMapper;
import com.jindouyun.db.domain.JindouyunGoods;
import com.jindouyun.db.domain.JindouyunGoods.Column;
import com.jindouyun.db.domain.JindouyunGoodsExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JindouyunGoodsService {
    Column[] columns = new Column[]{Column.id, Column.name, Column.brief, Column.picUrl, Column.originPrice, Column.nowPrice};
    @Resource
    private JindouyunGoodsMapper goodsMapper;

    /**
     * 获取热卖商品
     *
     * @param offset
     * @param limit
     * @return
     */
//    public List<JindouyunGoods> queryByHot(int offset, int limit) {
//        JindouyunGoodsExample example = new JindouyunGoodsExample();
//        example.or().andIsHotEqualTo(true).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
//        example.setOrderByClause("add_time desc");
//        PageHelper.startPage(offset, limit);
//
//        return goodsMapper.selectByExampleSelective(example, columns);
//    }

    /**
     * 获取新品上市
     *
     * @param offset
     * @param limit
     * @return
     */
//    public List<JindouyunGoods> queryByNew(int offset, int limit) {
//        JindouyunGoodsExample example = new JindouyunGoodsExample();
//        example.or().andIsNewEqualTo(true).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
//        example.setOrderByClause("add_time desc");
//        PageHelper.startPage(offset, limit);
//
//        return goodsMapper.selectByExampleSelective(example, columns);
//    }

    /**
     * 获取分类下的商品
     *
     * @param catList
     * @param offset
     * @param limit
     * @return
     */
    public List<JindouyunGoods> queryByCategory(List<Integer> catList, int offset, int limit) {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        example.or().andCategoryIdIn(catList).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        example.setOrderByClause("add_time  desc");
        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }


    /**
     * 获取分类下的商品
     *
     * @param catId
     * @param offset
     * @param limit
     * @return
     */
    public List<JindouyunGoods> queryByCategory(Integer catId, int offset, int limit) {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        example.or().andCategoryIdEqualTo(catId).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        example.setOrderByClause("add_time desc");
        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }


    /**
     * 条件查询 catId or brandId or keywords
     * @param catId
     * @param brandId
     * @param keywords
     * @param isHot
     * @param isNew
     * @param offset
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunGoods> querySelective(Integer catId, Integer brandId, String keywords, Boolean isHot, Boolean isNew, Integer offset, Integer limit, String sort, String order) {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        JindouyunGoodsExample.Criteria criteria1 = example.or();
        JindouyunGoodsExample.Criteria criteria2 = example.or();

        if (!StringUtils.isEmpty(catId) && catId != 0) {
            criteria1.andCategoryIdEqualTo(catId);
            criteria2.andCategoryIdEqualTo(catId);
        }
        if (!StringUtils.isEmpty(brandId)) {
            criteria1.andBrandIdEqualTo(brandId);
            criteria2.andBrandIdEqualTo(brandId);
        }
        if (!StringUtils.isEmpty(keywords)) {
            criteria1.andKeywordsLike("%" + keywords + "%");
            criteria2.andNameLike("%" + keywords + "%");
        }
        criteria1.andIsOnSaleEqualTo(true);
        criteria2.andIsOnSaleEqualTo(true);
        criteria1.andDeletedEqualTo(false);
        criteria2.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }

    /**
     * 条件查询 goodsId or goodsSn or name
     * @param goodsId
     * @param goodsSn
     * @param name
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunGoods> querySelective(Integer goodsId, String goodsSn, String name, Integer page, Integer size, String sort, String order) {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        JindouyunGoodsExample.Criteria criteria = example.createCriteria();

        if (goodsId != null) {
            criteria.andIdEqualTo(goodsId);
        }
        if (!StringUtils.isEmpty(goodsSn)) {
            criteria.andGoodsSnEqualTo(goodsSn);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return goodsMapper.selectByExampleWithBLOBs(example);
    }

    /**
     * 获取某个商品信息,包含完整信息
     *
     * @param id
     * @return
     */
    public JindouyunGoods findById(Integer id) {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        return goodsMapper.selectOneByExampleWithBLOBs(example);
    }

    /**
     * 获取某个商品信息，仅展示相关内容
     *
     * @param id
     * @return
     */
    public JindouyunGoods findByIdVO(Integer id) {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        example.or().andIdEqualTo(id).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return goodsMapper.selectOneByExampleSelective(example, columns);
    }


    /**
     * 获取所有在售物品总数
     *
     * @return
     */
    public Integer queryOnSale() {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        example.or().andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return (int) goodsMapper.countByExample(example);
    }

    public int updateById(JindouyunGoods goods) {
        goods.setUpdateTime(LocalDateTime.now());
        return goodsMapper.updateByPrimaryKeySelective(goods);
    }

    public void deleteById(Integer id) {
        goodsMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(JindouyunGoods goods) {
        goods.setAddTime(LocalDateTime.now());
        goods.setUpdateTime(LocalDateTime.now());
        goodsMapper.insertSelective(goods);
    }

    /**
     * 获取所有物品总数，包括在售的和下架的，但是不包括已删除的商品
     *
     * @return
     */
    public int count() {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        example.or().andDeletedEqualTo(false);
        return (int) goodsMapper.countByExample(example);
    }

    /**
     * 条件查询 查询类id 通过brandId or keywords
     * @param brandId
     * @param keywords
     * @param isHot
     * @param isNew
     * @return
     */
    public List<Integer> getCatIds(Integer brandId, String keywords, Boolean isHot, Boolean isNew) {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        JindouyunGoodsExample.Criteria criteria1 = example.or();
        JindouyunGoodsExample.Criteria criteria2 = example.or();

        if (!StringUtils.isEmpty(brandId)) {
            criteria1.andBrandIdEqualTo(brandId);
            criteria2.andBrandIdEqualTo(brandId);
        }
        if (!StringUtils.isEmpty(keywords)) {
            criteria1.andKeywordsLike("%" + keywords + "%");
            criteria2.andNameLike("%" + keywords + "%");
        }
        criteria1.andIsOnSaleEqualTo(true);
        criteria2.andIsOnSaleEqualTo(true);
        criteria1.andDeletedEqualTo(false);
        criteria2.andDeletedEqualTo(false);

        List<JindouyunGoods> goodsList = goodsMapper.selectByExampleSelective(example, Column.categoryId);
        List<Integer> cats = new ArrayList<Integer>();
        for (JindouyunGoods goods : goodsList) {
            cats.add(goods.getCategoryId());
        }
        return cats;
    }

    /**
     * name 商品是否存在
     * @param name
     * @return
     */
    public boolean checkExistByName(String name) {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        example.or().andNameEqualTo(name).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return goodsMapper.countByExample(example) != 0;
    }

    public List<JindouyunGoods> queryByIds(Integer[] ids) {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        example.or().andIdIn(Arrays.asList(ids)).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return goodsMapper.selectByExampleSelective(example, columns);
    }

    /**
     *
     * @param keyword
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunGoods> searchList(String keyword, Integer page, Integer limit, String sort, String order) {
        JindouyunGoodsExample example = new JindouyunGoodsExample();
        JindouyunGoodsExample.Criteria criteria1 = example.or();
        JindouyunGoodsExample.Criteria criteria2 = example.or();

        if (!StringUtils.isEmpty(keyword)) {
            criteria1.andKeywordsLike("%" + keyword + "%");
            criteria2.andNameLike("%" + keyword + "%");
        }
        criteria1.andIsOnSaleEqualTo(true);
        criteria2.andIsOnSaleEqualTo(true);
        criteria1.andDeletedEqualTo(false);
        criteria2.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }
}
