package com.jindouyun.admin.service;

import com.jindouyun.db.domain.JindouyunClockin;
import com.jindouyun.db.domain.JindouyunDeliveryStaff;
import com.jindouyun.db.domain.StaffVO;
import com.jindouyun.db.service.JindouyunDeliveryStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @className: AdminDeliveryService
 * @description:
 * @author: ZSZ
 * @date: 2020/8/13 17:20
 */
@Service
public class AdminDeliveryService {

    @Autowired
    private JindouyunDeliveryStaffService staffService;

    /**
     * 查询所有配送员
     * @param deliveryId
     * @param todayStatus
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public Object list(Integer deliveryId,Short todayStatus,Integer page,Integer limit,String sort,String order){
        return staffService.queryStaffVO(deliveryId,todayStatus,page,limit,sort,order);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    public Object info(Integer id){
        StaffVO staffVO = staffService.queryStaffVOById(id);
        if(staffVO == null){
            System.err.println("配送员详情 - id错误");
        }
//        List<JindouyunClockin> workTimes =
        return null;
    }

    /**
     *
     * 强制派单
     * @param mergeId
     * @return
     */
    @Transactional
    public Object forceOrder(Integer mergeId){
        return null;
    }

    
}
