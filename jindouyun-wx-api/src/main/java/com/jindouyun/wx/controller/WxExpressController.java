package com.jindouyun.wx.controller;

import com.jindouyun.core.system.SystemConfig;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.service.JindouyunAddressService;
import com.jindouyun.db.service.JindouyunOrderService;
import com.jindouyun.db.util.OrderUtil;
import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.db.service.JindouyunExpressOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WxExpressController
 * @Description 快递
 * @Author Bruce
 * @Date 2020/8/4 3:27 下午
 */
@RestController
@RequestMapping("/wx/express")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WxExpressController {

    @Autowired
    private JindouyunAddressService addressService;

    @Autowired
    private JindouyunOrderService orderService;

    @Autowired
    private JindouyunExpressOrderService expressOrderService;

    /**
     * 提交订单
     *
     * @param userId 用户ID
     * @param body   快递信息
     * @return 提交订单操作结果
     */
    @PostMapping("submit")
    public Object submit(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        if (body == null) {
            return ResponseUtil.badArgument();
        }

        Integer addressId = JacksonUtil.parseInteger(body, "addressId");//地址id
        String expressType = JacksonUtil.parseString(body, "expressType");//快递类型
        Integer isweight = JacksonUtil.parseInteger(body, "isweight");//0：不是重件，1：重件
        String message = JacksonUtil.parseString(body, "message");//备注
        String pikeupCode = JacksonUtil.parseString(body, "pikeupCode");//取货码
        String consignee = JacksonUtil.parseString(body, "consignee");//收件人
        String mobile = JacksonUtil.parseString(body, "mobile");//电话
        String deliveryTime = JacksonUtil.parseString(body, "deliveryTime");//派送时间

        // 收货地址
        JindouyunAddress checkedAddress = addressService.query(userId, addressId);
        if (checkedAddress == null) {
            return ResponseUtil.badArgument();
        }

        // 根据订单商品总价计算运费，满10则1运费，否则2元；
        BigDecimal actualPrice = new BigDecimal(0.00);
        if (isweight == 0){
            actualPrice = SystemConfig.getFalseIsweight();
        }else {
            actualPrice = SystemConfig.getTrueIsweight();
        }

        Integer orderId = null;
        JindouyunExpressOrder expressOrder = new JindouyunExpressOrder();

        expressOrder.setUserId(userId);
        expressOrder.setExpressType(expressType);
        expressOrder.setIsweight(isweight);
        expressOrder.setMessage(message);
        expressOrder.setPikeupCode(pikeupCode);
        expressOrder.setConsignee(consignee);
        expressOrder.setMobile(mobile);
        expressOrder.setDeliveryTime(deliveryTime);
        expressOrder.setAddress(checkedAddress.getAddressDetail());
        expressOrder.setBuilding(checkedAddress.getBuilding());
        expressOrder.setActualPrice(actualPrice);
        expressOrder.setOrderSn(orderService.generateOrderSn(userId));
        expressOrder.setOrderStatus(OrderUtil.STATUS_CREATE);

        // 添加订单表项
        expressOrderService.add(expressOrder);
        orderId = expressOrder.getId();

        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        return ResponseUtil.ok(data);
    }

}
