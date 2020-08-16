package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunExpressOrderMapper;
import com.jindouyun.db.domain.JindouyunExpressOrder;
import com.jindouyun.db.domain.JindouyunExpressOrder.Column;
import com.jindouyun.db.domain.JindouyunExpressOrderExample;
import com.jindouyun.db.domain.JindouyunOrderSplitExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.print.DocFlavor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @className: JindouyunExpressOrderService
 * @description:
 * @author: ZSZ
 * @date: 2020/8/10 17:41
 */
@Service
public class JindouyunExpressOrderService {

    Column[] columns = new Column[]{Column.id, Column.userId, Column.expressType, Column.orderSn,
            Column.orderStatus, Column.consignee, Column.mobile, Column.address,
            Column.building, Column.message, Column.actualPrice,
            Column.deliveryTime, Column.isweight};

    @Resource
    private JindouyunExpressOrderMapper expressOrderMapper;

    /**
     * 根据mergeId 查询
     * @param mergeId
     * @return
     */
    public List<JindouyunExpressOrder> queryByMergeId(Integer mergeId){
        JindouyunExpressOrderExample example = new JindouyunExpressOrderExample();
        example.or().andMergeIdEqualTo(mergeId).andDeletedEqualTo(false);
        return expressOrderMapper.selectByExample(example);
    }

    /**
     * 将Merge设为-1
     *
     * @param mergeId
     * @return
     */
    public int setMergeIdNull(Integer mergeId) {
        JindouyunExpressOrder expressOrder = new JindouyunExpressOrder();
        expressOrder.setMergeId(-1);
        expressOrder.setUpdateTime(LocalDateTime.now());
        JindouyunExpressOrderExample example = new JindouyunExpressOrderExample();
        example.or().andMergeIdEqualTo(mergeId).andOrderStatusIn(new ArrayList<>() {{
            add((short) 301);
        }});
        return expressOrderMapper.updateByExampleSelective(expressOrder, example);
    }

    /**
     * 更新 mergeId
     *
     * @param id
     * @param mergeId
     * @return
     */
    public int updateMergeId(Integer id, Integer mergeId) {
        JindouyunExpressOrder expressOrder = new JindouyunExpressOrder();
        expressOrder.setMergeId(mergeId);
        expressOrder.setUpdateTime(LocalDateTime.now());
        JindouyunExpressOrderExample example = new JindouyunExpressOrderExample();
        example.or().andIdEqualTo(id);
        return expressOrderMapper.updateByExampleSelective(expressOrder, example);
    }

    /**
     * 查找 by id
     *
     * @param id
     * @return
     */
    public JindouyunExpressOrder queryById(Integer id) {
        return expressOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 返回order VO
     *
     * @param userId
     * @param mergeId
     * @param orderSn
     * @param name
     * @param mobile
     * @param building
     * @param address
     * @param startTime
     * @param endTime
     * @param orderStatusArray
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunExpressOrder> queryCommonOrderSelective(Integer userId, Integer mergeId, String orderSn, String name,
                                                                 String mobile, Short building, String address, LocalDateTime startTime,
                                                                 LocalDateTime endTime, List<Short> orderStatusArray, Integer page,
                                                                 Integer limit, String sort, String order) {
        return querySelective(userId, mergeId, orderSn, name, mobile, building, address,
                startTime, endTime, orderStatusArray, page, limit, sort, order, columns);
    }

    /**
     * @param userId
     * @param mergeId
     * @param orderSn
     * @param name
     * @param mobile
     * @param building
     * @param address
     * @param startTime
     * @param endTime
     * @param orderStatusArray
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunExpressOrder> querySelective(Integer userId, Integer mergeId, String orderSn, String name,
                                                      String mobile, Short building, String address, LocalDateTime startTime,
                                                      LocalDateTime endTime, List<Short> orderStatusArray, Integer page,
                                                      Integer limit, String sort, String order, Column[] columns) {
        JindouyunExpressOrderExample example = new JindouyunExpressOrderExample();
        JindouyunExpressOrderExample.Criteria criteria = example.createCriteria();
        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (mergeId != null) {
            criteria.andMergeIdEqualTo(mergeId);
        }
        if (!StringUtils.isEmpty(orderSn)) {
            criteria.andOrderSnEqualTo(orderSn);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andConsigneeEqualTo(name);
        }
        if (!StringUtils.isEmpty(mobile)) {
            criteria.andMobileEqualTo(mobile);
        }
        if (building != null) {
            criteria.andBuildingEqualTo(building);
        }
        if (!StringUtils.isEmpty(address)) {
            criteria.andAddressEqualTo("%" + address + "%");
        }
        if (startTime != null && endTime != null) {
            criteria.andAddTimeBetween(startTime, endTime);
        }
        if (orderStatusArray != null && orderStatusArray.size() != 0) {
            criteria.andOrderStatusIn(orderStatusArray);
        }

        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);

        return expressOrderMapper.selectByExampleSelective(example, columns);
    }

    /**
     * 添加订单
     *
     * @param order
     * @return
     */
    public int add(JindouyunExpressOrder order) {
        order.setAddTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setDeleted(false);
        return expressOrderMapper.insertSelective(order);
    }

}
