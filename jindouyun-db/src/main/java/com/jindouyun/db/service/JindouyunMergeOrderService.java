package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunBrandMapper;
import com.jindouyun.db.dao.JindouyunMergeOrderMapper;
import com.jindouyun.db.dao.JindouyunOrderSplitMapper;
import com.jindouyun.db.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.jindouyun.common.util.CharUtil.getRandomNum;


/**
 * @ClassName JindouyunMergeOrderService
 * @Description
 * @Author Bruce
 * @Date 2020/8/10 5:18 下午
 */
@Service
public class JindouyunMergeOrderService {


    @Resource
    private JindouyunBrandService brandService;
    @Resource
    private JindouyunMergeOrderMapper mergeOrderMapper;
    @Resource
    private JindouyunOrderSplitService orderSplitService;
    @Resource
    private JindouyunExpressOrderService expressOrderService;
    @Resource
    private JindouyunAddressService addressService;

    public MergeExpressInfo queryMergeExpressInfoById(Integer id){
        JindouyunMergeOrder mergeOrder = selectByPrimaryKey(id);
        MergeExpressInfo mergeExpressInfo = null;
        if(mergeOrder != null){
            mergeExpressInfo = new MergeExpressInfo();
            mergeExpressInfo.setId(id);
            mergeExpressInfo.setOrderSn(mergeOrder.getOrderSn());
            mergeExpressInfo.setMessage(mergeOrder.getMessage());
            mergeExpressInfo.setAllPrice(mergeOrder.getAllPrice());
            mergeExpressInfo.setNum(mergeOrder.getNum());
            mergeExpressInfo.setType(mergeOrder.getType());
            mergeExpressInfo.setRelease(mergeOrder.getRelease());
            mergeExpressInfo.setStatus(mergeOrder.getStatus());
            mergeExpressInfo.setReleaseTime(mergeOrder.getReleaseTime());
            mergeExpressInfo.setReceiveTime(mergeOrder.getReceiveTime());
            mergeExpressInfo.setPickupTime(mergeOrder.getPickupTime());
            mergeExpressInfo.setPickupTime(mergeOrder.getArriveTime());
            List<JindouyunExpressOrder> expressOrders = expressOrderService.queryByMergeId(id);
            String address = null;
            List<Short> deliveryRange = null;
            if(expressOrders != null && expressOrders.size()>0){
                deliveryRange = new ArrayList<>();
                address = expressOrders.get(0).getAddress();
                for (JindouyunExpressOrder expressOrder:expressOrders) {
                    deliveryRange.add(expressOrder.getBuilding());
                }
            }
            mergeExpressInfo.setAddress(address);
            mergeExpressInfo.setDeliveryRange(deliveryRange);
            mergeExpressInfo.setSplitOrder(expressOrders);
        }
        return mergeExpressInfo;
    }

    public MergeInfo queryMergeInfoById(Integer id){
        JindouyunMergeOrder mergeOrder = selectByPrimaryKey(id);
        MergeInfo mergeInfo = null;
        if(mergeOrder != null){
            mergeInfo = new MergeInfo();
            mergeInfo.setId(id);
            mergeInfo.setOrderSn(mergeOrder.getOrderSn());
            mergeInfo.setMessage(mergeOrder.getMessage());
            mergeInfo.setAllPrice(mergeOrder.getAllPrice());
            mergeInfo.setNum(mergeOrder.getNum());
            mergeInfo.setType(mergeOrder.getType());
            mergeInfo.setRelease(mergeOrder.getRelease());
            mergeInfo.setStatus(mergeOrder.getStatus());
            mergeInfo.setReleaseTime(mergeOrder.getReleaseTime());
            mergeInfo.setReceiveTime(mergeOrder.getReceiveTime());
            mergeInfo.setPickupTime(mergeOrder.getPickupTime());
            mergeInfo.setPickupTime(mergeOrder.getArriveTime());
            List<JindouyunOrderSplit> orderSplits = orderSplitService.queryByMergeId(id);
            String address = null;
            List<Integer> deliveryRange = null;
            if(orderSplits != null && orderSplits.size() >0){
                deliveryRange = new ArrayList<>();
                JindouyunBrand brand = brandService.findById(orderSplits.get(0).getBrandId());
                if (brand != null){
                    address = addressService.queryById(brand.getAdderssId()).getAddressDetail();
                }
                for (JindouyunOrderSplit orderSplit:orderSplits) {
                    deliveryRange.add(orderSplit.getBrandId());
                }
            }
            mergeInfo.setAddress(address);
            mergeInfo.setDeliveryRange(deliveryRange);
            mergeInfo.setSplitOrder(orderSplits);
        }
        return mergeInfo;
    }

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    public Integer delete(Integer id){
        return mergeOrderMapper.logicalDeleteByPrimaryKey(id);
    }

    /**
     * 更新价格 和 数量
     * @param id
     * @param allPrice
     * @param num
     * @return
     */
    public int updateAllPriceAndNum(Integer id, BigDecimal allPrice, Short num){
        JindouyunMergeOrder mergeOrder = new JindouyunMergeOrder();
        mergeOrder.setAllPrice(allPrice);
        mergeOrder.setNum(num);
        mergeOrder.setUpdateTime(LocalDateTime.now());
        JindouyunMergeOrderExample example = new JindouyunMergeOrderExample();
        example.or().andIdEqualTo(id);
        return mergeOrderMapper.updateByExampleSelective(mergeOrder,example);
    }

    /**
     * 根据adminId 和 orderSn 查询
     * @param adminId
     * @param orderSn
     * @return
     */
    public JindouyunMergeOrder queryByAdminIdAndOrderSn(Integer adminId,String orderSn){
        JindouyunMergeOrderExample example = new JindouyunMergeOrderExample();
        example.or().andAdminIdEqualTo(adminId).andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return mergeOrderMapper.selectOneByExample(example);
    }

    /**
     * 查询 orderSn和userId 同时满足的数量
     *
     * @param adminId
     * @param orderSn
     * @return
     */
    public int countByOrderSn(Integer adminId, String orderSn) {
        JindouyunMergeOrderExample example = new JindouyunMergeOrderExample();
        example.or().andAdminIdEqualTo(adminId).andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return (int) mergeOrderMapper.countByExample(example);
    }

    // TODO 这里应该产生一个唯一的订单，但是实际上这里仍然存在两个订单相同的可能性
    public String generateOrderSn(Integer adminId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = df.format(LocalDate.now());
        String orderSn = now + getRandomNum(6);
        while (countByOrderSn(adminId, orderSn) != 0) {
            orderSn = now + getRandomNum(6);
        }
        return orderSn;
    }

    /**
     * 添加
     * @param mergeOrder
     * @return
     */
    public int add(JindouyunMergeOrder mergeOrder){
        mergeOrder.setAddTime(LocalDateTime.now());
        mergeOrder.setUpdateTime(LocalDateTime.now());
        mergeOrder.setDeleted(false);
        return mergeOrderMapper.insert(mergeOrder);
    }

    public JindouyunMergeOrder selectByPrimaryKey(Integer orderId) {
        return mergeOrderMapper.selectByPrimaryKey(orderId);
    }

    public int updateOrderStatus(JindouyunMergeOrder mergeOrder) {
        mergeOrder.setUpdateTime(LocalDateTime.now());
        return mergeOrderMapper.updateByPrimaryKeySelective(mergeOrder);
    }


}
