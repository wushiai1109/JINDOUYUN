package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunMergeOrderMapper;
import com.jindouyun.db.domain.JindouyunExpressOrder;
import com.jindouyun.db.domain.JindouyunMergeOrder;
import com.jindouyun.db.domain.JindouyunMergeOrderExample;
import com.jindouyun.db.domain.JindouyunOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.jindouyun.common.util.CharUtil.getRandomNum;


/**
 * @ClassName JindouyunMergeOrderService
 * @Description
 * @Author Bruce
 * @Date 2020/8/10 5:18 下午
 */
@Service
@Transactional
public class JindouyunMergeOrderService {

    @Autowired
    private JindouyunMergeOrderMapper mergeOrderMapper;

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    public Integer delete(Integer id){
        return mergeOrderMapper.logicalDeleteByPrimaryKey(id);
    }

    /**
     * 更新 release
     * @param id
     * @param adminId
     * @return
     */
    public Integer updateRelease(Integer id,Integer adminId){
        JindouyunMergeOrder mergeOrder = new JindouyunMergeOrder();
        mergeOrder.setAdminId(adminId);
        mergeOrder.setRelease((byte) 1);
        mergeOrder.setUpdateTime(LocalDateTime.now());
        return mergeOrderMapper.updateByPrimaryKeySelective(mergeOrder);
    }

    /**
     * 更新价格 和 数量
     * @param id
     * @param allPrice
     * @param num
     * @return
     */
    public int updateAllPriceAndNum(Integer id, BigDecimal allPrice, Short num){
        JindouyunMergeOrder mergeOrder = new JindouyunMergeOrder();
        mergeOrder.setAllPrice(allPrice);
        mergeOrder.setNum(num);
        mergeOrder.setUpdateTime(LocalDateTime.now());
        JindouyunMergeOrderExample example = new JindouyunMergeOrderExample();
        example.or().andIdEqualTo(id);
        return mergeOrderMapper.updateByExampleSelective(mergeOrder,example);
    }

    /**
     * 根据adminId 和 orderSn 查询
     * @param adminId
     * @param orderSn
     * @return
     */
    public JindouyunMergeOrder queryByAdminIdAndOrderSn(Integer adminId,String orderSn){
        JindouyunMergeOrderExample example = new JindouyunMergeOrderExample();
        example.or().andAdminIdEqualTo(adminId).andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return mergeOrderMapper.selectOneByExample(example);
    }

    /**
     * 查询 orderSn和userId 同时满足的数量
     *
     * @param adminId
     * @param orderSn
     * @return
     */
    public int countByOrderSn(Integer adminId, String orderSn) {
        JindouyunMergeOrderExample example = new JindouyunMergeOrderExample();
        example.or().andAdminIdEqualTo(adminId).andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return (int) mergeOrderMapper.countByExample(example);
    }

    // TODO 这里应该产生一个唯一的订单，但是实际上这里仍然存在两个订单相同的可能性
    public String generateOrderSn(Integer adminId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = df.format(LocalDate.now());
        String orderSn = now + getRandomNum(6);
        while (countByOrderSn(adminId, orderSn) != 0) {
            orderSn = now + getRandomNum(6);
        }
        return orderSn;
    }

    /**
     * 添加
     * @param mergeOrder
     * @return
     */
    public int add(JindouyunMergeOrder mergeOrder){
        mergeOrder.setAddTime(LocalDateTime.now());
        mergeOrder.setUpdateTime(LocalDateTime.now());
        return mergeOrderMapper.insert(mergeOrder);
    }

    public JindouyunMergeOrder selectByPrimaryKey(Integer orderId) {
        return mergeOrderMapper.selectByPrimaryKey(orderId);
    }

    public Object updateOrderStatus(JindouyunMergeOrder mergeOrder) {
        JindouyunMergeOrderExample example = new JindouyunMergeOrderExample();
        example.or().andIdEqualTo(mergeOrder.getId());
        return mergeOrderMapper.updateByExampleSelective(mergeOrder,example);
    }
}
