package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunDeliveryStaffMapper;
import com.jindouyun.db.domain.JindouyunDeliveryStaff;
import com.jindouyun.db.domain.JindouyunDeliveryStaffExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @className: JindouyunDeliveryService
 * @description: 骑手
 * @author: ZSZ
 * @date: 2020/8/4 16:10
 */
@Service
public class JindouyunDeliveryService {

    @Resource
    private JindouyunDeliveryStaffMapper deliveryMapper;

    public JindouyunDeliveryStaff queryByUserId(int userId) {
        JindouyunDeliveryStaffExample example = new JindouyunDeliveryStaffExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return deliveryMapper.selectOneByExample(example);
    }

}
