package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunDeliveryStaffMapper;
import com.jindouyun.db.dao.JindouyunUserMapper;
import com.jindouyun.db.domain.JindouyunDeliveryStaff;
import com.jindouyun.db.domain.JindouyunDeliveryStaffExample;
import com.jindouyun.db.domain.JindouyunUser;
import com.jindouyun.db.domain.JindouyunUserExample;
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

    @Resource
    private JindouyunUserMapper userMapper;

    public JindouyunDeliveryStaff queryByUserId(int userId) {
        JindouyunDeliveryStaffExample example = new JindouyunDeliveryStaffExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return deliveryMapper.selectOneByExample(example);
    }

    public Object modifyStatus(Integer userId, Short todayStatus) {

        JindouyunDeliveryStaffExample example = new JindouyunDeliveryStaffExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);

        JindouyunDeliveryStaff jindouyunDeliveryStaff = deliveryMapper.selectOneByExample(example);
        jindouyunDeliveryStaff.setTodayStatus(todayStatus);

        return deliveryMapper.updateByExample(jindouyunDeliveryStaff, example);
    }

    public Object modifyType(Integer userId, Byte workType) {
        JindouyunDeliveryStaffExample example = new JindouyunDeliveryStaffExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);

        JindouyunDeliveryStaff jindouyunDeliveryStaff = deliveryMapper.selectOneByExample(example);
        jindouyunDeliveryStaff.setWorkType(workType);

        return deliveryMapper.updateByExample(jindouyunDeliveryStaff, example);
    }

    public Object modifyInfo(Integer userId, String userName, String mobile) {
        JindouyunUserExample example = new JindouyunUserExample();
        example.or().andIdEqualTo(userId).andDeletedEqualTo(false);

        JindouyunUser jindouyunUser = userMapper.selectOneByExample(example);
        jindouyunUser.setUsername(userName);
        jindouyunUser.setMobile(mobile);

        return userMapper.updateByExample(jindouyunUser, example);
    }
}
