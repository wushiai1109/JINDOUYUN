package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunRegisteBrandMapper;
import com.jindouyun.db.domain.JindouyunRegisteBrand;
import com.jindouyun.db.domain.JindouyunRegisteBrandExample;
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

//    public JindouyunRegisteBrand queryByUid(Integer Id){
//        JindouyunRegisteBrandExample example = new JindouyunRegisteBrandExample();
//        example.or()
//    }

    public JindouyunRegisteBrand queryById(Integer id){
        return registeBrandMapper.selectByPrimaryKey(id);
    }

    public void add(JindouyunRegisteBrand register){
        register.setAddTime(LocalDateTime.now());
        register.setUpdateTime(LocalDateTime.now());
        register.setDeleted(false);
        registeBrandMapper.insertSelective(register);
    }

}
