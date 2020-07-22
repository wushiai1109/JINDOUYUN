package com.jindouyun.wx.service;

import com.jindouyun.wx.util.JwtHelper;

/**
 * @ClassName UserTokenManager
 * @Description 维护用户token
 * @Author Bruce
 * @Date 2020/7/21 3:36 下午
 */
public class UserTokenManager {

    public static String generateToken(Integer id) {
        JwtHelper jwtHelper = new JwtHelper();
        return jwtHelper.createToken(id);
    }

    public static Integer getUserId(String token) {
        JwtHelper jwtHelper = new JwtHelper();
        Integer userId = jwtHelper.verifyTokenAndGetUserId(token);
        if (userId == null || userId == 0) {
            return null;
        }
        return userId;
    }

}
