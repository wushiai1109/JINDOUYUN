package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunRegisteBrandMapper;
import com.jindouyun.db.domain.JindouyunRegisteBrand;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @className: JindouyunRegisterBrandService
 * @description:
 * @author: ZSZ
 * @date: 2020/8/5 20:41
 */
@Service
public class JindouyunRegisterBrandService {

    @Resource
    private JindouyunRegisteBrandMapper registeBrandMapper;

    public void add(JindouyunRegisteBrand register){
        register.setAddTime(LocalDateTime.now());
        register.setUpdateTime(LocalDateTime.now());
        registeBrandMapper.insertSelective(register);
    }

}
