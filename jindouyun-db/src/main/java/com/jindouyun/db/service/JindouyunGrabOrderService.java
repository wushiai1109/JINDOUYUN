package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jindouyun.common.constant.MergeOrderConstant;
import com.jindouyun.db.dao.JindouyunGrabOrderMapper;
import com.jindouyun.db.domain.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JindouyunGrabOrderService
 * @Description
 * @Author Bruce
 * @Date 2020/8/10 2:44 下午
 */
@Service
@Transactional
public class JindouyunGrabOrderService {

    @Resource
    private JindouyunGrabOrderMapper grabOrderMapper;

    @Resource
    private JindouyunMergeOrderService mergeOrderService;

    /**
     * 查询合单信息
     * @param orderStatusList
     * @param type
     * @param deliveryId
     * @param mergeId
     * @param force
     * @param date
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public Map<String, Object> queryMergeInfoList(List<Byte> orderStatusList, Byte type, Integer deliveryId, Integer mergeId, Boolean force, LocalDateTime date, Integer page, Integer limit, String sort, String order){
        if(deliveryId == null) {
            deliveryId = -1;
        }
        List<JindouyunGrabOrder> grabOrders = queryGrabOrder(deliveryId, mergeId, force, date, page, limit, sort, order);
        PageInfo pageInfo = new PageInfo(grabOrders);
        List<Object> mergeList = new ArrayList<>();
        for (JindouyunGrabOrder grabOrder:grabOrders) {
            JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(grabOrder.getOrderId());
            if (mergeOrder == null) continue;
            if(orderStatusList != null){
                Boolean flag = false;
                for (Byte status:orderStatusList) {
                    if( status == mergeOrder.getStatus()){
                        flag = true;
                        break;
                    }
                }
                if(!flag) continue;
            }
            if (type != null && mergeOrder.getType()!=type){
                continue;
            }

            Byte nowType = mergeOrder.getType();
            if (nowType == 0 || nowType == 1){
                MergeInfo mergeInfo = mergeOrderService.queryMergeInfoById(grabOrder.getOrderId());
                mergeList.add(mergeInfo);
            }else if(nowType == 2){
                MergeExpressInfo mergeExpressInfo = mergeOrderService.queryMergeExpressInfoById(grabOrder.getOrderId());
                mergeList.add(mergeExpressInfo);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("page",pageInfo.getPageNum());
        map.put("limit",pageInfo.getPageSize());
        map.put("total",pageInfo.getTotal());
        map.put("pages",pageInfo.getPages());
        map.put("mergeInfo",mergeList);

        return map;
    }


    public List<JindouyunGrabOrder> queryGrabOrder(Integer deliveryId, Integer mergeId, Boolean force,LocalDateTime date, Integer page, Integer limit, String sort, String order){
        JindouyunGrabOrderExample example = new JindouyunGrabOrderExample();
        JindouyunGrabOrderExample.Criteria criteria = example.createCriteria();

        if (deliveryId != null){
            criteria.andDeliveryIdEqualTo(deliveryId);
        }
        if (mergeId != null){
            criteria.andOrderIdEqualTo(mergeId);
        }
        if (date != null){
            LocalDateTime startTime = LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),0,0,0);
            LocalDateTime endTime = startTime.plusDays(1);
            criteria.andAddTimeBetween(startTime,endTime);
        }
        if ( force != null){
            criteria.andForceEqualTo(force);
        }

        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page,limit);

        return grabOrderMapper.selectByExample(example);

    }

    public int updateDeliveryId(Integer grabOrderId, Integer deliveryId){
        JindouyunGrabOrder grabOrder = new JindouyunGrabOrder();
        grabOrder.setId(grabOrderId);
        grabOrder.setDeliveryId(deliveryId);
        grabOrder.setUpdateTime(LocalDateTime.now());
        return grabOrderMapper.updateByPrimaryKeySelective(grabOrder);
    }



    /**
     * 根据合单id查询
     * @param mergeId
     * @return
     */
    public JindouyunGrabOrder queryByMergeId(Integer mergeId){
        JindouyunGrabOrderExample example = new JindouyunGrabOrderExample();
        example.or().andOrderIdEqualTo(mergeId).andDeletedEqualTo(false);
        return grabOrderMapper.selectOneByExample(example);
    }


    /**
     * 添加
     * @param grabOrder
     */
    public void add(JindouyunGrabOrder grabOrder){
        grabOrder.setAddTime(LocalDateTime.now());
        grabOrder.setUpdateTime(LocalDateTime.now());
        grabOrder.setDeleted(false);
        grabOrderMapper.insertSelective(grabOrder);
    }

//    public Object insertOneByGrabOrder(JindouyunGrabOrder jindouyunGrabOrder) {
//        JindouyunGrabOrderExample example = new JindouyunGrabOrderExample();
//        example.or().andOrderIdEqualTo(jindouyunGrabOrder.getOrderId()).andDeletedEqualTo(false);
//        List<JindouyunGrabOrder> jindouyunGrabOrders = grabOrderMapper.selectByExample(example);
//
//        //更新接单时间
//        mergeOrderService.updateOrderStatusAndReceiveTime(jindouyunGrabOrder.getOrderId(), MergeOrderConstant.MERGE_ORDER_REVEIVE,LocalDateTime.now());
//
//        if (jindouyunGrabOrders.size() != 0){
//            return false;
//        }
//        return grabOrderMapper.insertSelective(jindouyunGrabOrder);
//    }

    public List<JindouyunGrabOrder> selectAllOrder(Integer userId) {
        JindouyunGrabOrderExample example = new JindouyunGrabOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return grabOrderMapper.selectByExample(example);
    }
}
