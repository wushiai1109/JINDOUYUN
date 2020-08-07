package com.jindouyun.common.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserInfo
 * @Description
 * @Author Bruce
 * @Date 2020/7/22 7:25 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    //用户昵称或网络名称
    private String nickName;
    //用户头像图片
    private String avatarUrl;
    private String country;
    private String province;
    private String city;
    private String language;
    private Byte gender;

}
