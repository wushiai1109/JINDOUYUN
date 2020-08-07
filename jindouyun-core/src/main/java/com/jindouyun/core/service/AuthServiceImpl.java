package com.jindouyun.core.service;

import com.jindouyun.common.domain.UserInfo;
import com.jindouyun.common.service.CaptchaCodeManager;
import com.jindouyun.common.util.CharUtil;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.common.util.RegexUtil;
import com.jindouyun.common.util.bcrypt.BCryptPasswordEncoder;
import com.jindouyun.core.notify.NotifyService;
import com.jindouyun.core.notify.NotifyType;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunUser;
import com.jindouyun.db.service.JindouyunUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.jindouyun.common.constant.WxResponseCode.*;

/**
 * @className: AuthServiceImpl
 * @description: 权限认证
 * @author: ZSZ
 * @date: 2020/8/6 19:41
 */
public class AuthServiceImpl implements IAuthService{

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private JindouyunUserService userService;


    @Override
    public Object login(String username, String password) {
        //由其子类具体实现
        return null;
    }

    @Override
    public Object loginByWeixin(String code, UserInfo userInfo) {
        //由其子类具体实现
        return null;
    }

    @Override
    public Object register(String username, String password, String mobile, String code, JindouyunUser user, HttpServletRequest request){
        //小程序第一次登陆时自动注册
        return null;
    }

    @Override
    public Object captcha(String mobile) {

        if (StringUtils.isEmpty(mobile)) {
            return ResponseUtil.badArgument();
        }
        if (!RegexUtil.isMobileExact(mobile)) {
            return ResponseUtil.badArgumentValue();
        }

        if (!notifyService.isSmsEnable()) {
            return ResponseUtil.fail(AUTH_CAPTCHA_UNSUPPORT, "小程序后台验证码服务不支持");
        }
        String code = CharUtil.getRandomNum(6);
        boolean successful = CaptchaCodeManager.addToCache(mobile, code);
        if (!successful) {
            return ResponseUtil.fail(AUTH_CAPTCHA_FREQUENCY, "验证码未超时1分钟，不能发送");
        }
        notifyService.notifySmsTemplate(mobile, NotifyType.CAPTCHA, new String[]{code});

        return ResponseUtil.ok();
    }

    @Override
    public Object reset(String password, String mobile, String code) {
        if (mobile == null || code == null || password == null) {
            return ResponseUtil.badArgument();
        }

        //判断验证码是否正确
        String cacheCode = CaptchaCodeManager.getCachedCaptcha(mobile);
        if (cacheCode == null || cacheCode.isEmpty() || !cacheCode.equals(code)) {
            return ResponseUtil.fail(AUTH_CAPTCHA_UNMATCH, "验证码错误");
        }

        List<JindouyunUser> userList = userService.queryByMobile(mobile);
        JindouyunUser user = null;
        if (userList.size() > 1) {
            return ResponseUtil.serious();
        } else if (userList.size() == 0) {
            return ResponseUtil.fail(AUTH_MOBILE_UNREGISTERED, "手机号未注册");
        } else {
            user = userList.get(0);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);

        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok();
    }

    @Override
    public Object resetPhone(Integer userId, String password, String mobile, String code) {

        if (mobile == null || code == null || password == null) {
            return ResponseUtil.badArgument();
        }

        //判断验证码是否正确
        String cacheCode = CaptchaCodeManager.getCachedCaptcha(mobile);
        if (cacheCode == null || cacheCode.isEmpty() || !cacheCode.equals(code)) {
            return ResponseUtil.fail(AUTH_CAPTCHA_UNMATCH, "验证码错误");
        }

        List<JindouyunUser> userList = userService.queryByMobile(mobile);
        JindouyunUser user = null;
        if (userList.size() > 1) {
            return ResponseUtil.fail(AUTH_MOBILE_REGISTERED, "手机号已注册");
        }
        user = userService.findById(userId);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            return ResponseUtil.fail(AUTH_INVALID_ACCOUNT, "账号密码不对");
        }

        user.setMobile(mobile);
        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok();
    }

    @Override
    public Object profile(Integer userId, Byte gender, String avatar, String nickname) {

        JindouyunUser user = userService.findById(userId);
        if (!StringUtils.isEmpty(avatar)) {
            user.setAvatar(avatar);
        }
        if (gender != null) {
            user.setGender(gender);
        }
        if (!StringUtils.isEmpty(nickname)) {
            user.setNickname(nickname);
        }

        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok();
    }

    @Override
    public Object bindPhone(Integer userId, String body) {
        //由其子类具体实现
        return null;
    }

    @Override
    public Object logout(Integer userId) {
        //由其子类具体实现
        return null;
    }

    @Override
    public Object info(Integer userId) {
        //由其子类具体实现
        return null;
    }
}
