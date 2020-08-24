package com.jindouyun.merchant.controller;

import com.github.pagehelper.PageInfo;
import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.common.constant.MergeOrderConstant;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.common.validator.Sort;
import com.jindouyun.core.system.SystemConfig;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.service.*;
import com.jindouyun.merchant.dto.BrandInfo;
import com.jindouyun.merchant.service.MerchantUserManager;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jindouyun.common.constant.MergeOrderConstant.MERGE_ORDER_MERCHANT_RECEIVE;
import static com.jindouyun.common.constant.WxResponseCode.*;
import static com.jindouyun.db.util.OrderUtil.*;

/**
 * @className: MerchantBrandOrderController
 * @description:
 * @author: ZSZ
 * @date: 2020/8/18 13:26
 */
@RestController
@RequestMapping("/merchant/order")
@Validated
public class MerchantBrandOrderController {

    @Autowired
    private JindouyunOrderSplitService orderSplitService;

    @Autowired
    private JindouyunBrandOrderService brandOrderService;

    @Autowired
    private JindouyunGrabOrderService grabOrderService;

    @Autowired
    private JindouyunDeliveryStaffService deliveryStaffService;

    @Autowired
    private JindouyunMergeOrderService mergeOrderService;

    @Autowired
    private JindouyunOrderGoodsService goodsService;

    @GetMapping("/detail")
    public Object detail(@LoginUser Integer userId, @NotNull Integer splitOrderId){
        if( userId == null){
            return ResponseUtil.unlogin();
        }
        OrderSplitVO splitVO = orderSplitService.queryOrderSplitVO(splitOrderId);
        BigDecimal freightPrice = new BigDecimal(1.00);
        if (splitVO.getOrderSplit().getGoodsPrice().compareTo(SystemConfig.getFreightLimit()) < 0) {
            freightPrice = SystemConfig.getFreight();
        }
        splitVO.setDeliveryPrice(freightPrice);
        return ResponseUtil.ok(splitVO);
    }

    @GetMapping("/list")
    public Object list(@LoginUser Integer userId,
                       @RequestParam(required = false) List<Short> orderStatusList,
                       @RequestParam("brandId") Integer brandId, Integer mergeId,
                       @RequestParam(defaultValue = "") LocalDateTime date,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @RequestParam(defaultValue = "desc") String order){
        if( userId == null){
            return ResponseUtil.unlogin();
        }
        if (orderStatusList == null ){
            orderStatusList = new ArrayList<>(){{add((short)21);add((short) 22);}};
        }

        Map result = brandOrderService.queryMergeInfoList(orderStatusList, brandId, mergeId, date, page, limit, sort, order);
        Map<Integer,List<JindouyunOrderGoods>> goodsMap = new HashMap<>();
        for (MergeInfo mergeInfo:(List<MergeInfo>)result.get("mergeInfo")) {
            for (JindouyunOrderSplit orderSplit:mergeInfo.getSplitOrder()) {
                List<JindouyunOrderGoods> goods = goodsService.queryBySplitOrderId(orderSplit.getId());
                goodsMap.put(orderSplit.getId(),goods);
            }
        }
        result.put("goodsList",goodsMap);
        return ResponseUtil.ok(result);
    }

    @GetMapping("/completed")
    public Object completed(@LoginUser Integer userId,@RequestParam(defaultValue = "true") Boolean completed,
                            @RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer limit,
                            @Sort @RequestParam(defaultValue = "add_time") String sort,
                            @RequestParam(defaultValue = "desc") String order){
        if(userId == null){
            return ResponseUtil.unlogin();
        }
        BrandInfo brandInfo = MerchantUserManager.merchantInfoMap.get(userId).getBrandInfo();
        List<Short> orderStatusArray = new ArrayList<>();
        if(completed){
            orderStatusArray.add(STATUS_SHIP);
            orderStatusArray.add(STATUS_RECEIVE);
            orderStatusArray.add(STATUS_ARRIVED);
        }else{
            orderStatusArray.add(STATUS_CANCEL);
        }

        List<JindouyunOrderSplit> orderSplits = orderSplitService.querySelective(null,null,brandInfo.getId(),orderStatusArray,null,page,limit,sort,order);
        PageInfo<JindouyunOrderSplit> pageInfo = new PageInfo<>(orderSplits);
        List<Map<String,Object>> orderSplitInfos = new ArrayList<>();
        for (JindouyunOrderSplit orderSplit:orderSplits) {
            Map<String,Object> map = new HashMap<>();
            List<JindouyunOrderGoods> goods = goodsService.queryBySplitOrderId(orderSplit.getId());
            map.put("orderSplit",orderSplit);
            map.put("goods",goods);
            orderSplitInfos.add(map);
        }
        Map<String,Object> result = new HashMap();
        result.put("page",pageInfo.getPageNum());
        result.put("limit",pageInfo.getPageSize());
        result.put("total",pageInfo.getTotal());
        result.put("pages",pageInfo.getPages());
        result.put("orderSplits",orderSplitInfos);
        return ResponseUtil.ok(result);
    }

    /**
     *
     * @param userId
     * @param mergeId
     * @return
     */
    @GetMapping("/queryDelivery")
    public Object queryDelivery(@LoginUser Integer userId,Integer mergeId){
        if(userId == null){
            return ResponseUtil.unlogin();
        }
        if(mergeId == null){
            return ResponseUtil.badArgument();
        }
        JindouyunGrabOrder grabOrder = grabOrderService.queryByMergeId(mergeId);
        StaffVO staffVO = null;
        if(grabOrder != null){
            staffVO = deliveryStaffService.queryStaffVOById(grabOrder.getDeliveryId());
        }
        return ResponseUtil.ok(staffVO);
    }

    @PostMapping("/receive")
    @Transactional
    public Object receive(@LoginUser Integer userId,@RequestBody String body){
        if (userId == null){
            return ResponseUtil.unlogin();
        }
        Integer mergeId = JacksonUtil.parseInteger(body,"mergeId");
        if (mergeId == null){
            return ResponseUtil.badArgument();
        }
        JindouyunMergeOrder mergeOrder = mergeOrderService.selectByPrimaryKey(mergeId);
        if(mergeOrder == null){
            return ResponseUtil.badArgument();
        }
        //22 更新合单状态
        mergeOrder.setStatus(MERGE_ORDER_MERCHANT_RECEIVE);
        if(mergeOrderService.updateOrderStatus(mergeOrder) == 0){
            return ResponseUtil.fail();
        }
        //更新商家订单
        if(brandOrderService.updateStatusByMergeId(mergeId,(short)MERGE_ORDER_MERCHANT_RECEIVE)==0){
            return ResponseUtil.fail();
        }
        // 更新子订单状态
        if(orderSplitService.updateStatusByMergeId(mergeId,STATUS_MERCHANT_RECEIVE) == 0){
            return ResponseUtil.fail();
        }

        return ResponseUtil.ok(mergeOrder);
    }

    @PostMapping("/cancel")
    public Object cancel(@LoginUser Integer userId,@RequestBody String body){
        if (userId == null){
            return ResponseUtil.unlogin();
        }
        Integer splitOrderId = JacksonUtil.parseInteger(body,"splitOrderId");
        if(splitOrderId == null){
            return ResponseUtil.badArgument();
        }
        JindouyunOrderSplit orderSplit = orderSplitService.queryById(splitOrderId);
        //只有订单状态在21,22时才可以取消
        if(orderSplit.getOrderStatus() != 201 && orderSplit.getOrderStatus() != 202){
            return ResponseUtil.fail(ORDER_CANCEL_FAIL,"配送中，订单不可取消");
        }
        if(orderSplitService.setOrderStatus(splitOrderId,STATUS_CANCEL)==0){
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok();
    }

}
