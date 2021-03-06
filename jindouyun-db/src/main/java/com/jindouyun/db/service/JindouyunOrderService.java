package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunOrderGoodsMapper;
import com.jindouyun.db.dao.JindouyunOrderMapper;
import com.jindouyun.db.dao.OrderMapper;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.domain.JindouyunOrder;
import com.jindouyun.db.domain.JindouyunOrder.Column;
import com.jindouyun.db.util.OrderUtil;
import org.apache.ibatis.ognl.ObjectElementsAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.jindouyun.common.util.CharUtil.getRandomNum;

@Service
public class JindouyunOrderService {
    @Resource
    private JindouyunOrderMapper jindouyunOrderMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private JindouyunOrderGoodsMapper orderGoodsMapper;

    private Column[] allColumns = Column.values();
    /**
     * 添加订单
     *
     * @param order
     * @return
     */
    public int add(JindouyunOrder order) {
        order.setAddTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return jindouyunOrderMapper.insertSelective(order);
    }

    /**
     * 用户订单数量
     *
     * @param userId
     * @return
     */
    public int count(Integer userId) {
        JindouyunOrderExample example = new JindouyunOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return (int) jindouyunOrderMapper.countByExample(example);
    }


    public JindouyunOrder findById(Integer orderId) {
        return jindouyunOrderMapper.selectByPrimaryKey(orderId);
    }


    /**
     * 查询 orderSn和userId 同时满足的数量
     *
     * @param userId
     * @param orderSn
     * @return
     */
    public int countByOrderSn(Integer userId, String orderSn) {
        JindouyunOrderExample example = new JindouyunOrderExample();
        example.or().andUserIdEqualTo(userId).andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return (int) jindouyunOrderMapper.countByExample(example);
    }

    // TODO 这里应该产生一个唯一的订单，但是实际上这里仍然存在两个订单相同的可能性
    public String generateOrderSn(Integer userId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = df.format(LocalDate.now());
        String orderSn = now + getRandomNum(6);
        while (countByOrderSn(userId, orderSn) != 0) {
            orderSn = now + getRandomNum(6);
        }
        return orderSn;
    }

    /**
     * 查询用户订单 根据订单状态
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunOrder> queryByOrderStatus(Integer userId, List<Short> orderStatus, Integer page, Integer limit, String sort, String order) {
        return queryGoodsOrderSelective(userId,null,null,null,null,null,null,null,orderStatus,page,limit,sort,order,allColumns);
    }

    /**
     * 条件查询 userId 或orderSn 或orderStatusArray
     *
     * @param userId
     * @param orderSn
     * @param orderStatusArray
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunOrder> querySelective(Integer userId, String orderSn, List<Short> orderStatusArray, Integer page, Integer limit, String sort, String order) {
        return queryGoodsOrderSelective(userId,orderSn,null,null,null,null,null,null,orderStatusArray,
                page,limit,sort,order,allColumns);
    }

    public List<JindouyunOrder> queryGoodsOrderIdsSelective(Integer userId, String orderSn, String name, String mobile,
                                                         Short building, String address, LocalDateTime startTime, LocalDateTime endTime,
                                                         List<Short> orderStatusArray, Integer page,
                                                         Integer limit, String sort, String order){
        return queryGoodsOrderSelective(userId, orderSn, name, mobile, building, address, startTime, endTime, orderStatusArray, page, limit, sort, order, new Column[]{Column.id});
    }

    /**
     * 查询
     * @param userId
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
    public List<JindouyunOrder> queryGoodsOrderSelective(Integer userId, String orderSn, String name, String mobile,
                                                         Short building, String address, LocalDateTime startTime, LocalDateTime endTime,
                                                         List<Short> orderStatusArray, Integer page,
                                                         Integer limit, String sort, String order, Column[] columns){
        JindouyunOrderExample example = new JindouyunOrderExample();
        JindouyunOrderExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(orderSn)){
            criteria.andOrderSnEqualTo(orderSn);
        }
        if (!StringUtils.isEmpty(name)){
            criteria.andConsigneeEqualTo(name);
        }
        if (!StringUtils.isEmpty(mobile)){
            criteria.andMobileEqualTo(mobile);
        }
        if (building != null){
            criteria.andBuildingEqualTo(building);
        }
        if (!StringUtils.isEmpty(address)){
            criteria.andAddressEqualTo("%" + address + "%");
        }
        if (startTime != null && endTime !=null){
            criteria.andAddTimeBetween(startTime,endTime);
        }
        if (orderStatusArray != null && orderStatusArray.size() != 0) {
            criteria.andOrderStatusIn(orderStatusArray);
        }

        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        if(page!= null && limit != null){
            PageHelper.startPage(page,limit);
        }

        return jindouyunOrderMapper.selectByExampleSelective(example,columns);
    }

    /**
     * 更新
     *
     * @param order
     * @return
     */
    public int updateWithOptimisticLocker(JindouyunOrder order) {
        LocalDateTime preUpdateTime = order.getUpdateTime();
        order.setUpdateTime(LocalDateTime.now());
        return orderMapper.updateWithOptimisticLocker(preUpdateTime, order);
    }

    public void deleteById(Integer id) {
        jindouyunOrderMapper.logicalDeleteByPrimaryKey(id);
    }

    /**
     * 所有订单数量
     *
     * @return
     */
    public int count() {
        JindouyunOrderExample example = new JindouyunOrderExample();
        example.or().andDeletedEqualTo(false);
        return (int) jindouyunOrderMapper.countByExample(example);
    }

    /**
     * 查询未支付订单
     *
     * @param minutes
     * @return
     */
    public List<JindouyunOrder> queryUnpaid(int minutes) {
        JindouyunOrderExample example = new JindouyunOrderExample();
        example.or().andOrderStatusEqualTo(OrderUtil.STATUS_CREATE).andDeletedEqualTo(false);
        return jindouyunOrderMapper.selectByExample(example);
    }

    /**
     * 查询未确认的订单
     *
     * @param days
     * @return
     */
    public List<JindouyunOrder> queryUnconfirm(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.minusDays(days);
        JindouyunOrderExample example = new JindouyunOrderExample();
        example.or().andOrderStatusEqualTo(OrderUtil.STATUS_SHIP).andPayTimeLessThan(expired).andDeletedEqualTo(false);
        return jindouyunOrderMapper.selectByExample(example);
    }

    public JindouyunOrder findBySn(String orderSn) {
        JindouyunOrderExample example = new JindouyunOrderExample();
        example.or().andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return jindouyunOrderMapper.selectOneByExample(example);
    }

    /**
     * 根据userId 查询订单详情
     *
     * @param userId
     * @return
     */
    public Map<Object, Object> orderInfo(Integer userId) {
        JindouyunOrderExample example = new JindouyunOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        List<JindouyunOrder> orders = jindouyunOrderMapper.selectByExampleSelective(example, JindouyunOrder.Column.orderStatus, JindouyunOrder.Column.comments);

        int unpaid = 0;
        int unship = 0;
        int unrecv = 0;
        int uncomment = 0;
        for (JindouyunOrder order : orders) {
            if (OrderUtil.isCreateStatus(order)) {
                unpaid++;
            } else if (OrderUtil.isPayStatus(order)) {
                unship++;
            } else if (OrderUtil.isShipStatus(order)) {
                unrecv++;
            } else if (OrderUtil.isConfirmStatus(order) || OrderUtil.isAutoConfirmStatus(order)) {
                uncomment += order.getComments();
            } else {
                // do nothing
            }
        }

        Map<Object, Object> orderInfo = new HashMap<Object, Object>();
        //已下单，未支付
        orderInfo.put("unpaid", unpaid);
        //支付完成，未发货
        orderInfo.put("unship", unship);
        //已发货，未收到
        orderInfo.put("unrecv", unrecv);
        //已收到，未评价
        orderInfo.put("uncomment", uncomment);
        return orderInfo;

    }

    //查询待评价商品列表
    public List<JindouyunOrder> queryComment(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.minusDays(days);
        JindouyunOrderExample example = new JindouyunOrderExample();
        example.or().andCommentsGreaterThan((short) 0).andConfirmTimeLessThan(expired).andDeletedEqualTo(false);
        return jindouyunOrderMapper.selectByExample(example);
    }

}
