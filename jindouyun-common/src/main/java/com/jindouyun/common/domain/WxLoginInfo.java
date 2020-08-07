package com.jindouyun.common.domain;

import com.jindouyun.common.domain.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName WxLoginInfo
 * @Description
 * @Author Bruce
 * @Date 2020/7/22 7:28 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxLoginInfo {
    private String code;
    private UserInfo userInfo;
}
