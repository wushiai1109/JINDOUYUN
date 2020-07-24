package com.jindouyun.wx.service;

import com.jindouyun.db.domain.JindouyunUser;
import com.jindouyun.db.service.JindouyunUserService;
import com.jindouyun.wx.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @ClassName UserInfoService
 * @Description
 * @Author Bruce
 * @Date 2020/7/23 8:31 下午
 */
@Service
@Transactional
public class UserInfoService {

    @Autowired
    JindouyunUserService userService;
    
    public UserInfo getInfo(Integer userId) {
        JindouyunUser user = userService.findById(userId);
        Assert.state(user != null, "用户不存在");
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());
        userInfo.setAvatarUrl(user.getAvatar());
        return userInfo;
    }
}
