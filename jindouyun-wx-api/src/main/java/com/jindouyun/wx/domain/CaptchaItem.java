package com.jindouyun.wx.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @ClassName CaptchaItem
 * @Description 验证码实体类，用于缓存验证码发送
 * @Author Bruce
 * @Date 2020/7/23 12:58 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaItem {
    private String phoneNumber;
    private String code;
    private LocalDateTime expireTime;
}
