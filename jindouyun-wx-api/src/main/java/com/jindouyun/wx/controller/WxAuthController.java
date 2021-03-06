package com.jindouyun.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.jindouyun.core.notify.NotifyService;
import com.jindouyun.core.notify.NotifyType;
import com.jindouyun.common.util.*;
import com.jindouyun.common.util.bcrypt.BCryptPasswordEncoder;
import com.jindouyun.core.service.AuthServiceImpl;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunUser;
import com.jindouyun.db.service.CouponAssignService;
import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.common.domain.UserInfo;
import com.jindouyun.common.domain.WxLoginInfo;
import com.jindouyun.common.service.CaptchaCodeManager;
import com.jindouyun.db.service.JindouyunUserService;
import com.jindouyun.common.service.UserTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jindouyun.common.constant.WxResponseCode.*;


/**
 * @ClassName WxAuthController
 * @Description 鉴权服务
 * @Author Bruce
 * @Date 2020/7/22 7:07 下午
 */
@RestController
@RequestMapping("/wx/auth")
@CrossOrigin(origins = "*",maxAge = 3600)
public class WxAuthController extends AuthServiceImpl {

    @Autowired
    private JindouyunUserService userService;

    @Autowired
    @Qualifier("wxMaService")
//    @Resource
    private WxMaService wxMaService;


    @Autowired
    private CouponAssignService couponAssignService;

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

//        System.out.println(user);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, user.getPassword())) {
//            System.out.println(password);
//            System.out.println(user.getPassword());
            return ResponseUtil.fail(AUTH_INVALID_ACCOUNT, "账号密码不对");
        }

        // 更新登录情况
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(IpUtil.getIpAddr(request));
        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        // userInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(username);
        userInfo.setAvatarUrl(user.getAvatar());

        // token
        String token = UserTokenManager.generateToken(user.getId());
        Map<Object, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", userInfo);
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
            WxMaJscode2SessionResult result = this.wxMaService.getUserService().getSessionInfo(code);
            sessionKey = result.getSessionKey();
            openId = result.getOpenid();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sessionKey == null || openId == null) {
            return ResponseUtil.fail();
        }

        JindouyunUser user = userService.queryByOid(openId);
        if (user == null) {
            user = new JindouyunUser();
            user.setUsername(openId);
            user.setPassword(openId);
            user.setWeixinOpenid(openId);
            user.setAvatar(userInfo.getAvatarUrl());
            user.setNickname(userInfo.getNickName());
            user.setGender(userInfo.getGender());
            user.setStatus((byte) 0);
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setSessionKey(sessionKey);

            userService.add(user);

            // 新用户发送注册优惠券
            couponAssignService.assignForRegister(user.getId());
        } else {
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setSessionKey(sessionKey);
            if (userService.updateById(user) == 0) {
                return ResponseUtil.updatedDataFailed();
            }
        }

        // token
        String token = UserTokenManager.generateToken(user.getId());

        Map<Object, Object> result = new HashMap<>();
        result.put("token", token);
//        System.out.println(token);
        result.put("userInfo", userInfo);
        return ResponseUtil.ok(result);
    }

//    /**
//     * 账号注册
//     *
//     * @param body    请求内容
//     *                {
//     *                username: xxx,
//     *                password: xxx,
//     *                mobile: xxx
//     *                code: xxx
//     *                }
//     *                其中code是手机验证码，目前还不支持手机短信验证码
//     * @param request 请求对象
//     * @return 登录结果
//     * 成功则
//     * {
//     * errno: 0,
//     * errmsg: '成功',
//     * data:
//     * {
//     * token: xxx,
//     * tokenExpire: xxx,
//     * userInfo: xxx
//     * }
//     * }
//     * 失败则 { errno: XXX, errmsg: XXX }
//     */
//    @PostMapping("register")
//    public Object register(@RequestBody String body, HttpServletRequest request) {
//        String username = JacksonUtil.parseString(body, "username");
//        String password = JacksonUtil.parseString(body, "password");
//        String mobile = JacksonUtil.parseString(body, "mobile");
//        String code = JacksonUtil.parseString(body, "code");
//        // 如果是小程序注册，则必须非空
//        // 其他情况，可以为空
//        String wxCode = JacksonUtil.parseString(body, "wxCode");
//
//        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)) {
//            return ResponseUtil.badArgument();
//        }
//
//        List<JindouyunUser> userList = userService.queryByUsername(username);
//        if (userList.size() > 0) {
//            return ResponseUtil.fail(AUTH_NAME_REGISTERED, "用户名已注册");
//        }
//
//        userList = userService.queryByMobile(mobile);
//        if (userList.size() > 0) {
//            return ResponseUtil.fail(AUTH_MOBILE_REGISTERED, "手机号已注册");
//        }
//        if (!RegexUtil.isMobileExact(mobile)) {
//            return ResponseUtil.fail(AUTH_INVALID_MOBILE, "手机号格式不正确");
//        }
//        //判断验证码是否正确
//        String cacheCode = CaptchaCodeManager.getCachedCaptcha(mobile);
//        if (cacheCode == null || cacheCode.isEmpty() || !cacheCode.equals(code)) {
//            return ResponseUtil.fail(AUTH_CAPTCHA_UNMATCH, "验证码错误");
//        }
//
//        String openId = "";
//        // 非空，则是小程序注册
//        // 继续验证openid
//        if (!StringUtils.isEmpty(wxCode)) {
//            try {
//                WxMaJscode2SessionResult result = this.wxMaService.getUserService().getSessionInfo(wxCode);
//                openId = result.getOpenid();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return ResponseUtil.fail(AUTH_OPENID_UNACCESS, "openid 获取失败");
//            }
//            userList = userService.queryByOpenid(openId);
//            if (userList.size() > 1) {
//                return ResponseUtil.serious();
//            }
//            if (userList.size() == 1) {
//                JindouyunUser checkUser = userList.get(0);
//                String checkUsername = checkUser.getUsername();
//                String checkPassword = checkUser.getPassword();
//                if (!checkUsername.equals(openId) || !checkPassword.equals(openId)) {
//                    return ResponseUtil.fail(AUTH_OPENID_BINDED, "openid已绑定账号");
//                }
//            }
//        }
//
//        JindouyunUser user = null;
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encodedPassword = encoder.encode(password);
//        user = new JindouyunUser();
//        user.setUsername(username);
//        user.setPassword(encodedPassword);
//        user.setMobile(mobile);
//        user.setWeixinOpenid(openId);
//        user.setAvatar("https://yanxuan.nosdn.127.net/80841d741d7fa3073e0ae27bf487339f.jpg?imageView&quality=90&thumbnail=64x64");
//        user.setNickname(username);
//        user.setGender((byte) 0);
//        user.setStatus((byte) 0);
//        user.setLastLoginTime(LocalDateTime.now());
//        user.setLastLoginIp(IpUtil.getIpAddr(request));
//        userService.add(user);
//
//        // 给新用户发送注册优惠券
//        couponAssignService.assignForRegister(user.getId());
//
//        // userInfo
//        UserInfo userInfo = new UserInfo();
//        userInfo.setNickName(username);
//        userInfo.setAvatarUrl(user.getAvatar());
//
//        // token
//        String token = UserTokenManager.generateToken(user.getId());
//
//        Map<Object, Object> result = new HashMap<Object, Object>();
//        result.put("token", token);
//        result.put("userInfo", userInfo);
//        return ResponseUtil.ok(result);
//    }

    /**
     * 请求验证码
     * <p>
     * 这里需要一定机制防止短信验证码被滥用
     *
     * @param body 手机号码 { mobile: xxx}
     * @return
     */
    @Override
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
    @Override
    @PostMapping("bindPhone")
    public Object bindPhone(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        JindouyunUser user = userService.findById(userId);
        String encryptedData = JacksonUtil.parseString(body, "encryptedData");
        String iv = JacksonUtil.parseString(body, "iv");
        WxMaPhoneNumberInfo phoneNumberInfo = this.wxMaService.getUserService().getPhoneNoInfo(user.getSessionKey(), encryptedData, iv);
        String phone = phoneNumberInfo.getPhoneNumber();
        user.setMobile(phone);
        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }

    @Override
    @PostMapping("logout")
    public Object logout(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        return ResponseUtil.ok();
    }

    @Override
    @GetMapping("info")
    public Object info(@LoginUser Integer userId) {
//        System.out.println(userId);
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        JindouyunUser user = userService.findById(userId);
        Map<Object, Object> data = new HashMap<>();
        data.put("nickName", user.getNickname());
        data.put("avatar", user.getAvatar());
        data.put("gender", user.getGender());
        data.put("mobile", user.getMobile());

        return ResponseUtil.ok(data);
    }


}