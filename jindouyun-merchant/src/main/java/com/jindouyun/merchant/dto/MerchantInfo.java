package com.jindouyun.merchant.dto;

import com.jindouyun.common.domain.UserInfo;
import com.jindouyun.db.domain.JindouyunAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: MerchantInfo
 * @description: 店家信息
 * @author: ZSZ
 * @date: 2020/8/5 17:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantInfo {

    private BrandInfo brandInfo;
    private UserInfo userInfo;
    private JindouyunAddress address;
    private boolean auth;
    private boolean apply;
}
