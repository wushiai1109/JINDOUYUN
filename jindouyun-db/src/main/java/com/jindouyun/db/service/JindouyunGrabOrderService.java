package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunGrabOrderMapper;
import com.jindouyun.db.domain.JindouyunDeliveriesPerformanceExample;
import com.jindouyun.db.domain.JindouyunGrabOrder;
import com.jindouyun.db.domain.JindouyunGrabOrderExample;
import com.jindouyun.db.domain.JindouyunMergeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private JindouyunGrabOrderMapper grabOrderMapper;

    @Autowired
    private JindouyunMergeOrderService mergeOrderService;


    public Object insertOneByGrabOrder(JindouyunGrabOrder jindouyunGrabOrder) {
        JindouyunGrabOrderExample example = new JindouyunGrabOrderExample();
        example.or().andOrderIdEqualTo(jindouyunGrabOrder.getOrderId()).andDeletedEqualTo(false);
        List<JindouyunGrabOrder> jindouyunGrabOrders = grabOrderMapper.selectByExample(example);

        //更新接单时间
        JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(jindouyunGrabOrder.getOrderId());
        mergeOrder.setReceiveTime(LocalDateTime.now());
        mergeOrderService.updateOrderStatus(mergeOrder);

        if (jindouyunGrabOrders.size() != 0){
            return false;
        }
        return grabOrderMapper.insertSelective(jindouyunGrabOrder);
    }

    public List<JindouyunGrabOrder> selectAllOrder(Integer userId) {
        JindouyunGrabOrderExample example = new JindouyunGrabOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return grabOrderMapper.selectByExample(example);
    }
}
