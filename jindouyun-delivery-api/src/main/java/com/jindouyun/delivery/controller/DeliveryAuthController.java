package com.jindouyun.delivery.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.common.domain.UserInfo;
import com.jindouyun.common.domain.WxLoginInfo;
import com.jindouyun.common.service.UserTokenManager;
import com.jindouyun.common.util.*;
import com.jindouyun.common.util.bcrypt.BCryptPasswordEncoder;
import com.jindouyun.core.service.AuthServiceImpl;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunDeliveryStaff;
import com.jindouyun.db.domain.JindouyunRegisteDeliveries;
import com.jindouyun.db.domain.JindouyunUser;
import com.jindouyun.db.service.JindouyunDeliveryService;
import com.jindouyun.db.service.JindouyunRegiteDeliveryService;
import com.jindouyun.db.service.JindouyunUserService;
import com.jindouyun.delivery.dto.DeliveryInfo;
import com.jindouyun.delivery.service.LoginUserManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jindouyun.common.constant.WxResponseCode.*;


/**
 * 鉴权服务
 */
@RestController
@RequestMapping("/delivery/auth")
@Validated
public class DeliveryAuthController extends AuthServiceImpl {
    private final Log logger = LogFactory.getLog(DeliveryAuthController.class);

    @Autowired
    private JindouyunUserService userService;

    @Autowired
    private JindouyunDeliveryService deliveryService;

    @Autowired
    private JindouyunRegiteDeliveryService registerDeliveryService;

    @Autowired
    @Qualifier("deliveryWxMaService")
    private WxMaService wxService;


    /**
     * 账号登录
     *
     * @param body    请求内容，{ username: xxx, password: xxx }
     * @param request 请求对象
     * @return 登录结果
     */
    @PostMapping("login")
    public Object login(@RequestBody String body, HttpServletRequest request) {
        String username = JacksonUtil.parseString(body, "username");
        String password = JacksonUtil.parseString(body, "password");
        if (username == null || password == null) {
            return ResponseUtil.badArgument();
        }

        List<JindouyunUser> userList = userService.queryByUsername(username);
        JindouyunUser user = null;
        if (userList.size() > 1) {
            return ResponseUtil.serious();
        } else if (userList.size() == 0) {
            return ResponseUtil.fail(AUTH_INVALID_ACCOUNT, "账号不存在");
        } else {
            user = userList.get(0);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            return ResponseUtil.fail(AUTH_INVALID_ACCOUNT, "账号密码不对");
        }

        // 更新登录情况
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(IpUtil.getIpAddr(request));
        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        //deliveryInfo
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setUsername(user.getUsername());
        deliveryInfo.setNickname(user.getNickname());
        deliveryInfo.setAvatar(user.getAvatar());
        deliveryInfo.setGender(user.getGender());
        deliveryInfo.setBirthday(user.getBirthday());
        deliveryInfo.setMobile(user.getMobile());

        //判断是否已认证
        JindouyunDeliveryStaff deliveryStaff = deliveryService.queryByUserId(user.getId());
        if(deliveryInfo != null){
            deliveryInfo.setAuth(true);
        }
        deliveryInfo.setDeliveryStaff(deliveryStaff);

        // token
        String token = UserTokenManager.generateToken(user.getId());

        //加入用户缓存
        LoginUserManager.deliveryInfoMap.put(user.getId(),deliveryInfo);

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("deliveryInfo", deliveryInfo);
        return ResponseUtil.ok(result);
    }

    /**
     * 微信登录
     *
     * @param wxLoginInfo 请求内容，{ code: xxx, userInfo: xxx }
     * @param request     请求对象
     * @return 登录结果
     */
    @PostMapping("login_by_weixin")
    public Object loginByWeixin(@RequestBody WxLoginInfo wxLoginInfo, HttpServletRequest request) {
        String code = wxLoginInfo.getCode();
        UserInfo userInfo = wxLoginInfo.getUserInfo();
        if (code == null || userInfo == null) {
            return ResponseUtil.badArgument();
        }

        String sessionKey = null;
        String openId = null;
        try {
            WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(code);
            sessionKey = result.getSessionKey();
            openId = result.getOpenid();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sessionKey == null || openId == null) {
            return ResponseUtil.fail();
        }

        JindouyunUser user = userService.queryByOid(openId);
        //第一次登陆自动注册
        if (user == null) {
            user = new JindouyunUser();
            user.setUsername(CharUtil.getRandomString(32));
            user.setPassword(CharUtil.getRandomString(32));
            user.setWeixinOpenid(openId);
            user.setAvatar(userInfo.getAvatarUrl());
            user.setNickname(userInfo.getNickName());
            user.setGender(userInfo.getGender());
            user.setStatus((byte) 0);
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setSessionKey(sessionKey);

            userService.add(user);

        } else {
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setSessionKey(sessionKey);
            if (userService.updateById(user) == 0) {
                return ResponseUtil.updatedDataFailed();
            }
        }

        user = userService.queryByOid(openId);
        //deliveryInfo
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setUsername(user.getUsername());
        deliveryInfo.setNickname(user.getNickname());
        deliveryInfo.setAvatar(user.getAvatar());
        deliveryInfo.setGender(user.getGender());
        deliveryInfo.setBirthday(user.getBirthday());
        deliveryInfo.setMobile(user.getMobile());

        //判断是否已认证
        JindouyunDeliveryStaff deliveryStaff = deliveryService.queryByUserId(user.getId());
        if(deliveryInfo != null){
            deliveryInfo.setAuth(true);
        }
        deliveryInfo.setDeliveryStaff(deliveryStaff);

        // token
        String token = UserTokenManager.generateToken(user.getId());

        //加入缓存
        LoginUserManager.deliveryInfoMap.put(user.getId(),deliveryInfo);

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("deliveryInfo", deliveryInfo);
        return ResponseUtil.ok(result);
    }

    /**
     * 注册成为骑手
     * @return
     */
    @PostMapping("register")
    public Object register(@LoginUser Integer userId, @RequestParam(name = "message",required = false) String message){
        JindouyunRegisteDeliveries registerDeliveries = new JindouyunRegisteDeliveries();
        registerDeliveries.setUserId(userId);
        registerDeliveries.setMessage(message);
        registerDeliveryService.add(registerDeliveries);
        return ResponseUtil.ok();
    }


    /**
     * 请求验证码
     * <p>
     * 这里需要一定机制防止短信验证码被滥用
     *
     * @param body 手机号码 { mobile: xxx}
     * @return
     */
    @PostMapping("captcha")
    public Object captcha(@RequestBody String body) {
        String phoneNumber = JacksonUtil.parseString(body, "mobile");
        Object result = super.captcha(phoneNumber);
        return result;
    }

    /**
     * 账号密码重置
     *
     * @param body    请求内容
     *                {
     *                password: xxx,
     *                mobile: xxx
     *                code: xxx
     *                }
     *                其中code是手机验证码，目前还不支持手机短信验证码
     * @return 登录结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    @PostMapping("reset")
    public Object reset(@RequestBody String body) {
        String password = JacksonUtil.parseString(body, "password");
        String mobile = JacksonUtil.parseString(body, "mobile");
        String code = JacksonUtil.parseString(body, "code");

        Object result = super.reset(password,mobile,code);
        return result;
    }

    /**
     * 账号手机号码重置
     *
     * @param body    请求内容
     *                {
     *                password: xxx,
     *                mobile: xxx
     *                code: xxx
     *                }
     *                其中code是手机验证码，目前还不支持手机短信验证码
     * @return 登录结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    @PostMapping("resetPhone")
    public Object resetPhone(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        String password = JacksonUtil.parseString(body, "password");
        String mobile = JacksonUtil.parseString(body, "mobile");
        String code = JacksonUtil.parseString(body, "code");

        Object result = super.resetPhone(userId,password,mobile,code);

        return result;
    }


    /**
     * 账号信息更新
     *
     * @param body    请求内容
     *                {
     *                avatar: xxx,
     *                gender: xxx
     *                nickname: xxx
     *                }
     * @return 登录结果
     */
    @PostMapping("profile")
    public Object profile(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        String avatar = JacksonUtil.parseString(body, "avatar");
        Byte gender = JacksonUtil.parseByte(body, "gender");
        String nickname = JacksonUtil.parseString(body, "nickname");

        Object result = super.profile(userId,gender,avatar,nickname);

        return result;
    }

    /**
     * 微信手机号码绑定
     *
     * @param userId
     * @param body
     * @return
     */
    @PostMapping("bindPhone")
    public Object bindPhone(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        JindouyunUser user = userService.findById(userId);
        String encryptedData = JacksonUtil.parseString(body, "encryptedData");
        String iv = JacksonUtil.parseString(body, "iv");
        WxMaPhoneNumberInfo phoneNumberInfo = this.wxService.getUserService().getPhoneNoInfo(user.getSessionKey(), encryptedData, iv);
        String phone = phoneNumberInfo.getPhoneNumber();
        user.setMobile(phone);
        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }


    @PostMapping("logout")
    public Object logout(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        //移除缓存
        LoginUserManager.deliveryInfoMap.remove(userId);
        return ResponseUtil.ok();
    }

    @GetMapping("info")
    public Object info(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        return ResponseUtil.ok(LoginUserManager.deliveryInfoMap.get(userId));
    }
}
