package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunDeliveryStaffMapper;
import com.jindouyun.db.domain.JindouyunDeliveryStaff;
import com.jindouyun.db.domain.JindouyunDeliveryStaffExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @className: JindouyunDeliveryStaff
 * @description:
 * @author: ZSZ
 * @date: 2020/8/4 21:20
 */
@Service
public class JindouyunDeliveryStaffService {

    @Resource
    JindouyunDeliveryStaffMapper deliveryStaffMapper;

    /**
     * 更新用户状态
     * @param id
     * @param today_status
     * @param work_status
     */
    public void updateStaffStatus(Integer id,short today_status,short work_status){
        JindouyunDeliveryStaff staff = new JindouyunDeliveryStaff();
        staff.setTodayStatus(today_status);
        staff.setWorkStatus(work_status);
        JindouyunDeliveryStaffExample example = new JindouyunDeliveryStaffExample();
        example.or().andUserIdEqualTo(id);
        deliveryStaffMapper.updateByExampleSelective(staff,example);
    }
}
