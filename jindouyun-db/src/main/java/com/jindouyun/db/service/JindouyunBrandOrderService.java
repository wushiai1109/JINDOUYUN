package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jindouyun.db.dao.JindouyunBrandOrderMapper;
import com.jindouyun.db.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: JindouyunBrandOrderService
 * @description:
 * @author: ZSZ
 * @date: 2020/8/13 14:48
 */
@Service
public class JindouyunBrandOrderService {

    @Resource
    private JindouyunBrandOrderMapper brandOrderMapper;

    @Autowired
    private JindouyunMergeOrderService mergeOrderService;

    public int updateStatusByMergeId(Integer mergeId,Short status){
        JindouyunBrandOrder brandOrder = new JindouyunBrandOrder();
        brandOrder.setStatus(status);
        brandOrder.setUpdateTime(LocalDateTime.now());
        JindouyunBrandOrderExample example = new JindouyunBrandOrderExample();
        example.or().andOrderIdEqualTo(mergeId).andDeletedEqualTo(false);
        return brandOrderMapper.updateByExampleSelective(brandOrder,example);
    }

    /**
     * 查询合单信息
     * @param orderStatusList
     * @param brandId
     * @param mergeId
     * @param date
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public Map<String, Object> queryMergeInfoList(List<Short> orderStatusList, Integer brandId, Integer mergeId, LocalDateTime date, Integer page, Integer limit, String sort, String order){
        List<JindouyunBrandOrder> brandOrders = queryBrandOrder(orderStatusList,brandId,mergeId, date, page, limit, sort, order);
        PageInfo pageInfo = new PageInfo(brandOrders);
        List<MergeInfo> mergeList = new ArrayList<>();
        for (JindouyunBrandOrder brandOrder:brandOrders) {
            MergeInfo mergeInfo = mergeOrderService.queryMergeInfoById(brandOrder.getOrderId());
            mergeInfo.setBrandOrder(brandOrder);
            mergeList.add(mergeInfo);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("page",pageInfo.getPageNum());
        map.put("limit",pageInfo.getPageSize());
        map.put("total",pageInfo.getTotal());
        map.put("pages",pageInfo.getPages());
        map.put("mergeInfo",mergeList);

        return map;
    }

    public List<JindouyunBrandOrder> queryBrandOrder(List<Short> orderStatusArray,Integer brandId, Integer mergeId, LocalDateTime date, Integer page, Integer limit, String sort, String order){
        JindouyunBrandOrderExample example = new JindouyunBrandOrderExample();
        JindouyunBrandOrderExample.Criteria criteria = example.createCriteria();

        if(orderStatusArray != null){
            criteria.andStatusIn(orderStatusArray);
        }

        if (brandId != null){
            criteria.andBrandIdEqualTo(brandId);
        }

        if (date != null){
            LocalDateTime startTime = LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),0,0,0);
            LocalDateTime endTime = startTime.plusDays(1);
            criteria.andAddTimeBetween(startTime,endTime);
        }

        if (mergeId != null){
            criteria.andOrderIdEqualTo(mergeId);
        }

        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page,limit);

        return brandOrderMapper.selectByExample(example);

    }


    /**
     * 根据mergeId查询
     * @param mergeId
     * @return
     */
    public JindouyunBrandOrder queryByMergeId(Integer mergeId){
        JindouyunBrandOrderExample example = new JindouyunBrandOrderExample();
        example.or().andOrderIdEqualTo(mergeId).andDeletedEqualTo(false);
        return brandOrderMapper.selectOneByExample(example);
    }

    /**
     * 添加 brandOrder
     * @param brandOrder
     */
    public void add(JindouyunBrandOrder brandOrder){
        brandOrder.setAddTime(LocalDateTime.now());
        brandOrder.setUpdateTime(LocalDateTime.now());
        brandOrder.setDeleted(false);
        brandOrderMapper.insertSelective(brandOrder);
    }
}
