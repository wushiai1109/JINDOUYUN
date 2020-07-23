package com.jindouyun.wx.service;

import com.jindouyun.core.util.RegexUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.dao.JindouyunAddressMapper;
import com.jindouyun.db.domain.JindouyunAddress;
import com.jindouyun.db.domain.JindouyunAddressExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    public Object validate(JindouyunAddress address) {
        String name = address.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }

        // 测试收货手机号码是否正确
        String mobile = address.getTel();
        if (StringUtils.isEmpty(mobile)) {
            return ResponseUtil.badArgument();
        }
        if (!RegexUtil.isMobileExact(mobile)) {
            return ResponseUtil.badArgument();
        }

        String province = address.getProvince();
        if (StringUtils.isEmpty(province)) {
            return ResponseUtil.badArgument();
        }

        String city = address.getCity();
        if (StringUtils.isEmpty(city)) {
            return ResponseUtil.badArgument();
        }

        String county = address.getCounty();
        if (StringUtils.isEmpty(county)) {
            return ResponseUtil.badArgument();
        }

        String areaCode = address.getAreaCode();
        if (StringUtils.isEmpty(areaCode)) {
            return ResponseUtil.badArgument();
        }

        String detailedAddress = address.getAddressDetail();
        if (StringUtils.isEmpty(detailedAddress)) {
            return ResponseUtil.badArgument();
        }

        Boolean isDefault = address.getIsDefault();
        if (isDefault == null) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    public JindouyunAddress findDefault(Integer userId) {
        JindouyunAddressExample example = new JindouyunAddressExample();
        example.or().andUserIdEqualTo(userId).andIsDefaultEqualTo(true).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }
}
