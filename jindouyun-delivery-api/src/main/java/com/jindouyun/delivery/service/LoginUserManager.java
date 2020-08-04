package com.jindouyun.delivery.service;

import com.jindouyun.delivery.dto.DeliveryInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @className: LoginUserManager
 * @description: 登陆用户的manager
 * @author: ZSZ
 * @date: 2020/8/4 19:16
 */
@Component
public class LoginUserManager {

    public static final Map<Integer, DeliveryInfo> deliveryInfoMap = new HashMap<>();

}
