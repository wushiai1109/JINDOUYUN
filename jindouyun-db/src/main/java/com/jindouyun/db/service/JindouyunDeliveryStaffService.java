package com.jindouyun.db.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jindouyun.db.dao.JindouyunDeliveryStaffMapper;
import com.jindouyun.db.dao.JindouyunUserMapper;
import com.jindouyun.db.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private JindouyunUserMapper userMapper;

    @Resource
    JindouyunUserService userService;

    public List<JindouyunDeliveryStaff> all(){
        JindouyunDeliveryStaffExample example = new JindouyunDeliveryStaffExample();
        example.or().andDeletedEqualTo(false);
        return deliveryStaffMapper.selectByExample(example);
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public JindouyunDeliveryStaff queryById(Integer id){
        return deliveryStaffMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据id 查询staffVO
     * @param id
     * @return
     */
    public StaffVO queryStaffVOById(Integer id){
        List<StaffVO> list = (List<StaffVO>)queryStaffVO(id,null,1,10,null,null).get("staffList");
        return list.get(0);
    }

    public Map<String, Object> queryStaffVO (Integer id, Short status, Integer page, Integer limit, String sort, String order){
        List<StaffVO> staffVOs = new ArrayList<>();
        List<JindouyunDeliveryStaff> allStaff = queryAll(id, status, page, limit, sort, order);
        PageInfo pageInfo = new PageInfo(allStaff);
        for (JindouyunDeliveryStaff staff:allStaff) {
            StaffVO staffVO = new StaffVO();
            staffVO.setId(staff.getId());
            staffVO.setTodayStatus(staff.getTodayStatus());
            staffVO.setWorkStatus(staff.getWorkType());
            staffVO.setWorkType(staff.getWorkType());
            UserVo user = userService.findUserVoById(staff.getUserId());
            staffVO.setUser(user);
            staffVOs.add(staffVO);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("page",pageInfo.getPageNum());
        map.put("limit",pageInfo.getPageSize());
        map.put("total",pageInfo.getTotal());
        map.put("pages",pageInfo.getPages());
        map.put("staffList",staffVOs);
        return map;
    }

    public List<JindouyunDeliveryStaff> queryAll(Integer id,Short status,Integer page,Integer limit,String sort,String order){
        JindouyunDeliveryStaffExample example = new JindouyunDeliveryStaffExample();
        JindouyunDeliveryStaffExample.Criteria criteria = example.createCriteria();

        if (id != null){
            criteria.andIdEqualTo(id);
        }
        if (status != null){
            criteria.andTodayStatusEqualTo(status);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }
        PageHelper.startPage(page,limit);

        return deliveryStaffMapper.selectByExample(example);
    }

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

    public JindouyunDeliveryStaff queryByUserId(int userId) {
        JindouyunDeliveryStaffExample example = new JindouyunDeliveryStaffExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return deliveryStaffMapper.selectOneByExample(example);
    }

    public Object modifyStatus(Integer userId, Short todayStatus) {

        JindouyunDeliveryStaffExample example = new JindouyunDeliveryStaffExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);

        JindouyunDeliveryStaff jindouyunDeliveryStaff = deliveryStaffMapper.selectOneByExample(example);
        jindouyunDeliveryStaff.setTodayStatus(todayStatus);

        return deliveryStaffMapper.updateByExample(jindouyunDeliveryStaff, example);
    }

    public Object modifyType(Integer userId, Byte workType) {
        JindouyunDeliveryStaffExample example = new JindouyunDeliveryStaffExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);

        JindouyunDeliveryStaff jindouyunDeliveryStaff = deliveryStaffMapper.selectOneByExample(example);
        jindouyunDeliveryStaff.setWorkType(workType);

        return deliveryStaffMapper.updateByExample(jindouyunDeliveryStaff, example);
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
