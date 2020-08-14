package com.jindouyun.db.service;

import com.jindouyun.common.constant.MergeOrderConstant;
import com.jindouyun.db.dao.JindouyunGrabOrderMapper;
import com.jindouyun.db.domain.JindouyunDeliveriesPerformanceExample;
import com.jindouyun.db.domain.JindouyunGrabOrder;
import com.jindouyun.db.domain.JindouyunGrabOrderExample;
import com.jindouyun.db.domain.JindouyunMergeOrder;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName JindouyunGrabOrderService
 * @Description
 * @Author Bruce
 * @Date 2020/8/10 2:44 下午
 */
@Service
@Transactional
public class JindouyunGrabOrderService {

    @Resource
    private JindouyunGrabOrderMapper grabOrderMapper;

    @Resource
    private JindouyunMergeOrderService mergeOrderService;

    public int updateDeliveryId(Integer grabOrderId, Integer deliveryId){
        JindouyunGrabOrder grabOrder = new JindouyunGrabOrder();
        grabOrder.setId(grabOrderId);
        grabOrder.setDeliveryId(deliveryId);
        grabOrder.setUpdateTime(LocalDateTime.now());
        return grabOrderMapper.updateByPrimaryKeySelective(grabOrder);
    }

    /**
     * 根据合单id查询
     * @param mergeId
     * @return
     */
    public JindouyunGrabOrder queryByMergeId(Integer mergeId){
        JindouyunGrabOrderExample example = new JindouyunGrabOrderExample();
        example.or().andOrderIdEqualTo(mergeId).andDeletedEqualTo(false);
        return grabOrderMapper.selectOneByExample(example);
    }


    /**
     * 添加
     * @param grabOrder
     */
    public void add(JindouyunGrabOrder grabOrder){
        grabOrder.setAddTime(LocalDateTime.now());
        grabOrder.setUpdateTime(LocalDateTime.now());
        grabOrder.setDeleted(false);
        grabOrderMapper.insertSelective(grabOrder);
    }

//    public Object insertOneByGrabOrder(JindouyunGrabOrder jindouyunGrabOrder) {
//        JindouyunGrabOrderExample example = new JindouyunGrabOrderExample();
//        example.or().andOrderIdEqualTo(jindouyunGrabOrder.getOrderId()).andDeletedEqualTo(false);
//        List<JindouyunGrabOrder> jindouyunGrabOrders = grabOrderMapper.selectByExample(example);
//
//        //更新接单时间
//        mergeOrderService.updateOrderStatusAndReceiveTime(jindouyunGrabOrder.getOrderId(), MergeOrderConstant.MERGE_ORDER_REVEIVE,LocalDateTime.now());
//
//        if (jindouyunGrabOrders.size() != 0){
//            return false;
//        }
//        return grabOrderMapper.insertSelective(jindouyunGrabOrder);
//    }

    public List<JindouyunGrabOrder> selectAllOrder(Integer userId) {
        JindouyunGrabOrderExample example = new JindouyunGrabOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return grabOrderMapper.selectByExample(example);
    }
}
