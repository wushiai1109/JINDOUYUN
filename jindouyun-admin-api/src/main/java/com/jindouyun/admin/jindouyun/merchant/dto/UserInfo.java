package com.jindouyun.admin.jindouyun.merchant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String nickName;
    private String avatarUrl;
    private String country;
    private String province;
    private String city;
    private String language;
    private Byte gender;

}
