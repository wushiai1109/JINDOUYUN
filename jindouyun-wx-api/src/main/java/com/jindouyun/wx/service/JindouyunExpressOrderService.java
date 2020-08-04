package com.jindouyun.wx.service;

import com.jindouyun.db.dao.JindouyunExpressOrderMapper;
import com.jindouyun.db.dao.JindouyunOrderGoodsMapper;
import com.jindouyun.db.dao.JindouyunOrderMapper;
import com.jindouyun.db.dao.OrderMapper;
import com.jindouyun.db.domain.JindouyunExpressOrder;
import com.jindouyun.db.domain.JindouyunOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @ClassName JindouyunExpressOrderService
 * @Description
 * @Author Bruce
 * @Date 2020/8/4 4:51 下午
 */
@Service
@Transactional
public class JindouyunExpressOrderService {

    @Resource
    private JindouyunExpressOrderMapper expressOrderMapper;

    /**
     * 添加订单
     *
     * @param order
     * @return
     */
    public int add(JindouyunExpressOrder order) {
        order.setAddTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return expressOrderMapper.insertSelective(order);
    }

}
