package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunAddressMapper;
import com.jindouyun.db.domain.JindouyunAddress;
import com.jindouyun.db.domain.JindouyunAddressExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunAddressService {
    @Resource
    private JindouyunAddressMapper addressMapper;

    public JindouyunAddress queryById(Integer id){
        return addressMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过用户id查询地址列表
     * @param uid
     * @return
     */
    public List<JindouyunAddress> queryByUid(Integer uid) {
        JindouyunAddressExample example = new JindouyunAddressExample();
        example.or().andUserIdEqualTo(uid).andDeletedEqualTo(false);
        return addressMapper.selectByExample(example);
    }

    /**
     * 查询地址
     * @param userId
     * @param id
     * @return
     */
    public JindouyunAddress query(Integer userId, Integer id) {
        JindouyunAddressExample example = new JindouyunAddressExample();
        example.or().andIdEqualTo(id).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }

    /**
     * 添加地址
     * @param address
     * @return
     */
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

    /**
     * 查找用户默认地址
     * @param userId
     * @return
     */
    public JindouyunAddress findDefault(Integer userId) {
        JindouyunAddressExample example = new JindouyunAddressExample();
        example.or().andUserIdEqualTo(userId).andIsDefaultEqualTo(true).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }

    /**
     * 重置用户的默认地址
     * @param userId
     */
    public void resetDefault(Integer userId) {
        JindouyunAddress address = new JindouyunAddress();
        address.setIsDefault(false);
        address.setUpdateTime(LocalDateTime.now());
        JindouyunAddressExample example = new JindouyunAddressExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        addressMapper.updateByExampleSelective(address, example);
    }

    /**
     * 根据用户id 或 姓名 或电话 或楼栋 查找地址列表
     * @param userId
     * @param name
     * @param building
     * @param phone
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunAddress> querySelective(Integer userId, String name, Short building, String phone, Integer page, Integer limit, String sort, String order) {
        JindouyunAddressExample example = new JindouyunAddressExample();
        JindouyunAddressExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (building != null){
            criteria.andBuildingEqualTo(building);
        }
        if (phone != null){
            criteria.andTelEqualTo(phone);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return addressMapper.selectByExample(example);
    }
}
