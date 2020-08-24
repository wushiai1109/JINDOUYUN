package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunOrderSplitMapper;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.domain.JindouyunOrderSplit.Column;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @className: JindouyunOrderSplitService
 * @description:
 * @author: ZSZ
 * @date: 2020/8/9 20:10
 */
@Service
public class JindouyunOrderSplitService {

    Column[] setMergeIdNullColumns = new JindouyunOrderSplit.Column[]{Column.mergeId, Column.updateTime};

    @Resource
    private JindouyunOrderSplitMapper splitMapper;

    @Autowired
    private JindouyunBrandService brandService;

    @Autowired
    private JindouyunOrderGoodsService orderGoodsService;

    public List<JindouyunOrderSplit> queryIncomesDetail(Integer brandId,LocalDateTime startTime,LocalDateTime endTime,List<Short> orderStatusArray){
        JindouyunOrderSplitExample example = new JindouyunOrderSplitExample();
        example.or().andBrandIdEqualTo(brandId).andAddTimeBetween(startTime,endTime).andOrderStatusIn(orderStatusArray).andDeletedEqualTo(false);
        return splitMapper.selectByExample(example);
    }


    public int updateStatusByMergeId(Integer mergeId,Short status){
        JindouyunOrderSplit orderSplit = new JindouyunOrderSplit();
        orderSplit.setOrderStatus(status);
        orderSplit.setUpdateTime(LocalDateTime.now());
        JindouyunOrderSplitExample example = new JindouyunOrderSplitExample();
        example.or().andMergeIdEqualTo(mergeId).andDeletedEqualTo(false);
        return splitMapper.updateByExampleSelective(orderSplit,example);
    }

    public int setOrderStatus(Integer splitOrderId,Short status){
        JindouyunOrderSplit orderSplit = new JindouyunOrderSplit();
        orderSplit.setId(splitOrderId);
        orderSplit.setOrderStatus(status);
        orderSplit.setUpdateTime(LocalDateTime.now());
        return splitMapper.updateByPrimaryKeySelective(orderSplit);
    }


    /**
     *
     * @param splitOrderId
     * @return
     */
    public OrderSplitVO queryOrderSplitVO(Integer splitOrderId){
        JindouyunOrderSplit orderSplit = queryById(splitOrderId);
        OrderSplitVO splitVO = null;
        if(orderSplit != null){
            BrandVo brandVo = brandService.findBrandVoById(orderSplit.getBrandId());
            List<JindouyunOrderGoods> orderGoodsList = orderGoodsService.queryBySplitOrderId(splitOrderId);
            splitVO = new OrderSplitVO(brandVo,orderSplit,orderGoodsList);
        }
        return splitVO;
    }

    /**
     * 根据MergeId查询
     * @param id
     * @return
     */
    public List<JindouyunOrderSplit> queryByMergeId(Integer id){
        JindouyunOrderSplitExample example = new JindouyunOrderSplitExample();
        example.or().andMergeIdEqualTo(id).andDeletedEqualTo(false);
        return splitMapper.selectByExample(example);
    }

    public List<JindouyunOrderSplit> queryByOid(Integer id){
        JindouyunOrderSplitExample example = new JindouyunOrderSplitExample();
        example.or().andOrderIdEqualTo(id).andDeletedEqualTo(false);
        return splitMapper.selectByExample(example);
    }

    public int setMergeIdNull(Integer mergeId) {
        JindouyunOrderSplit orderSplit = new JindouyunOrderSplit();
        orderSplit.setMergeId(-1);
        orderSplit.setUpdateTime(LocalDateTime.now());
        JindouyunOrderSplitExample example = new JindouyunOrderSplitExample();
        example.or().andMergeIdEqualTo(mergeId).andOrderStatusIn(new ArrayList<>(){{add((short)201);add((short)202);add((short)203);add((short)301);}});
        return splitMapper.updateByExampleSelective(orderSplit, example);
    }

    /**
     * 添加mergeId
     *
     * @param orderSplitId
     * @param mergeId
     * @return
     */
    public int updateMergeId(Integer orderSplitId, Integer mergeId) {
        JindouyunOrderSplit orderSplit = new JindouyunOrderSplit();
        orderSplit.setMergeId(mergeId);
        orderSplit.setUpdateTime(LocalDateTime.now());
        JindouyunOrderSplitExample example = new JindouyunOrderSplitExample();
        example.or().andIdEqualTo(orderSplitId);
        return splitMapper.updateByExampleSelective(orderSplit, example);
    }

    public JindouyunOrderSplit queryById(Integer id) {
        return splitMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据合单id和商家id查询
     * @param mergeId
     * @param brandId
     * @return
     */
    public List<JindouyunOrderSplit> queryByMergeIdAndBrandId(Integer mergeId,Integer brandId){
        JindouyunOrderSplitExample example = new JindouyunOrderSplitExample();
        example.or().andMergeIdEqualTo(mergeId).andBrandIdEqualTo(brandId).andDeletedEqualTo(false);
        return splitMapper.selectByExample(example);
    }

    /**
     * 根据 mergerId brandId orderId 以及 orderStatus 查找
     *
     * @param mergerId
     * @param brandId
     * @param orderStatusArray
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunOrderSplit> querySelective(Byte type, Integer mergerId, Integer brandId, List<Short> orderStatusArray, List<Integer> orderIdArray, Integer page, Integer limit, String sort, String order) {
        JindouyunOrderSplitExample example = new JindouyunOrderSplitExample();
        JindouyunOrderSplitExample.Criteria criteria = example.createCriteria();

        //商品
        if (type != null && type == 0) {
            if (brandId != null && brandId != 0) {
                return new ArrayList<>();
            }
            criteria.andBrandIdEqualTo(0);
            //外卖
        } else if (type != null && type == 1) {

            if (brandId != null) {
                criteria.andBrandIdEqualTo(brandId);
            } else {
                criteria.andBrandIdNotEqualTo(0);
            }
        } else if(type == null && brandId != null){
            criteria.andBrandIdEqualTo(brandId);
        }
        if (mergerId != null) {
            criteria.andMergeIdEqualTo(mergerId);
        }

        if (orderIdArray != null) {
            criteria.andOrderIdIn(orderIdArray);
        }

        if (orderStatusArray != null) {
            criteria.andOrderStatusIn(orderStatusArray);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);

        return splitMapper.selectByExample(example);
    }

    public int add(JindouyunOrderSplit orderSplit){
        orderSplit.setAddTime(LocalDateTime.now());
        orderSplit.setUpdateTime(LocalDateTime.now());
        orderSplit.setDeleted(false);
        return splitMapper.insertSelective(orderSplit);
    }


}
