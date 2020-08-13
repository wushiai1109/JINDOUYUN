package com.jindouyun.db.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jindouyun.db.dao.JindouyunDeliveryStaffMapper;
import com.jindouyun.db.dao.JindouyunUserMapper;
import com.jindouyun.db.domain.JindouyunDeliveryStaff;
import com.jindouyun.db.domain.JindouyunDeliveryStaffExample;
import com.jindouyun.db.domain.StaffVO;
import com.jindouyun.db.domain.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    JindouyunUserService userService;

    public List<StaffVO> queryStaffVO (Integer id, Short status, Integer page, Integer limit, String sort, String order){
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
        }
        pageInfo.setList(staffVOs);
        return (List<StaffVO>) pageInfo;
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
}
