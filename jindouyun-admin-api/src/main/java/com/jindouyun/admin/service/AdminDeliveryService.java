package com.jindouyun.admin.service;

import com.jindouyun.db.service.JindouyunDeliveryStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
