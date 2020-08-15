package com.jindouyun.merchant.service;

import com.jindouyun.merchant.dto.MerchantInfo;
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
public class MerchantUserManager {

    public static final Map<Integer, MerchantInfo> merchantInfoMap = new HashMap<>();

}
