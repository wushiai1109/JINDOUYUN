package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunOrderGoodsMapper;
import com.jindouyun.db.dao.JindouyunOrderMapper;
import com.jindouyun.db.dao.OrderMapper;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class JindouyunOrderService {
    @Resource
    private JindouyunOrderMapper jindouyunOrderMapper;
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private JindouyunOrderGoodsMapper orderGoodsMapper;

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

    private String getRandomNum(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
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
        JindouyunOrderExample example = new JindouyunOrderExample();
        example.setOrderByClause(JindouyunOrder.Column.addTime.desc());
        JindouyunOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        if (orderStatus != null) {
            criteria.andOrderStatusIn(orderStatus);
        }
        criteria.andDeletedEqualTo(false);
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return jindouyunOrderMapper.selectByExample(example);
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
        JindouyunOrderExample example = new JindouyunOrderExample();
        JindouyunOrderExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(orderSn)) {
            criteria.andOrderSnEqualTo(orderSn);
        }
        if (orderStatusArray != null && orderStatusArray.size() != 0) {
            criteria.andOrderStatusIn(orderStatusArray);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return jindouyunOrderMapper.selectByExample(example);
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

    /**
     * 订单搜索
     * @param userId
     * @param keyword
     * @return
     */
    public List<JindouyunOrder> find(Integer userId, String keyword) {
        JindouyunOrderGoodsExample example = new JindouyunOrderGoodsExample();
        JindouyunOrderGoodsExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(keyword)) {
            criteria.andGoodsNameLike("%" + keyword + "%");
        }
        criteria.andDeletedEqualTo(false);

        List<JindouyunOrderGoods> jindouyunOrderGoods = orderGoodsMapper.selectByExampleSelective(example);
        System.out.println(jindouyunOrderGoods);

        List<JindouyunOrder> jindouyunOrderList = new ArrayList<>();

        for (JindouyunOrderGoods orderGoods : jindouyunOrderGoods) {
            JindouyunOrder jindouyunOrder = jindouyunOrderMapper.selectByPrimaryKey(orderGoods.getOrderId());
//            if (jindouyunOrder.getUserId().intValue() == userId.intValue()){
//            System.out.println("jindouyunOrder.getUserId().intValue()"+jindouyunOrder.getUserId().intValue());
//            System.out.println("userId.intValue()"+userId.intValue());
//            System.out.println(jindouyunOrder.getUserId().intValue() == userId.intValue());
            if (jindouyunOrder.getUserId().intValue() == userId.intValue()){
                jindouyunOrderList.add(jindouyunOrder);
            }
        }
        return jindouyunOrderList;
    }
}
