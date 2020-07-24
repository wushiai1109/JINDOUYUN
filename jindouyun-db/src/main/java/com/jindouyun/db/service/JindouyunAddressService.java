package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunAddressMapper;
import com.jindouyun.db.domain.JindouyunAddress;
import com.jindouyun.db.domain.JindouyunAddressExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName JindouyunAddressService
 * @Description
 * @Author Bruce
 * @Date 2020/7/22 9:35 上午
 */
@Service
@Transactional
public class JindouyunAddressService {

    @Autowired
    private JindouyunAddressMapper addressMapper;

    public List<JindouyunAddress> queryByUid(Integer userId) {
        JindouyunAddressExample addressExample = new JindouyunAddressExample();
        addressExample.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return addressMapper.selectByExample(addressExample);
    }

    public JindouyunAddress query(Integer userId, Integer id) {
        JindouyunAddressExample addressExample = new JindouyunAddressExample();
        addressExample.or().andIdEqualTo(userId).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(addressExample);
    }

    public void resetDefault(Integer userId) {
        JindouyunAddress address = new JindouyunAddress();
        address.setIsDefault(false);
        address.setUpdateTime(LocalDateTime.now());
        JindouyunAddressExample example = new JindouyunAddressExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        addressMapper.updateByExampleSelective(address, example);
    }

    public int add(JindouyunAddress address) {
        address.setAddTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.insertSelective(address);
    }

    public int update(JindouyunAddress address) {
        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.updateByPrimaryKeySelective(address);
    }

    public void delete(Integer id) {
        addressMapper.logicalDeleteByPrimaryKey(id);
    }

    public JindouyunAddress findDefault(Integer userId) {
        JindouyunAddressExample example = new JindouyunAddressExample();
        example.or().andUserIdEqualTo(userId).andIsDefaultEqualTo(true).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }
}
