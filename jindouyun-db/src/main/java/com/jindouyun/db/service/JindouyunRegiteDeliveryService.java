package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunRegisteDeliveriesMapper;
import com.jindouyun.db.domain.JindouyunRegisteDeliveries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @className: JindouyunRegiteDeliveryService
 * @description: 骑手注册
 * @author: ZSZ
 * @date: 2020/8/4 19:40
 */
@Service
public class JindouyunRegiteDeliveryService {

    @Resource
    private JindouyunRegisteDeliveriesMapper registerDeliveriesMapper;

    public void add(JindouyunRegisteDeliveries register){
        register.setAddTime(LocalDateTime.now());
        register.setUpdateTime(LocalDateTime.now());
        registerDeliveriesMapper.insertSelective(register);
    }

}
