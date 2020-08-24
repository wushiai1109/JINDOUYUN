package com.jindouyun.merchant.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.jindouyun.merchant.dto.BrandInfo;
import com.jindouyun.merchant.dto.MerchantInfo;
import com.jindouyun.merchant.service.MerchantUserManager;
import com.jindouyun.common.annotation.LoginUser;
import com.jindouyun.common.domain.UserInfo;
import com.jindouyun.common.domain.WxLoginInfo;
import com.jindouyun.common.service.UserTokenManager;
import com.jindouyun.common.util.CharUtil;
import com.jindouyun.common.util.IpUtil;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.core.service.AuthServiceImpl;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.common.util.bcrypt.BCryptPasswordEncoder;
import com.jindouyun.db.domain.*;
import com.jindouyun.db.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jindouyun.common.constant.WxResponseCode.*;

/**
 * 鉴权服务
 */
@RestController
@RequestMapping("/merchant/auth")
@Validated
public class MerchantAuthController extends AuthServiceImpl {
    private final Log logger = LogFactory.getLog(MerchantAuthController.class);

    @Autowired
    private JindouyunUserService userService;

    @Autowired
    private JindouyunAddressService addressService;

    @Autowired
    private JindouyunBrandService brandService;

    @Autowired
    private JindouyunRegisterBrandService registerBrandService;

    @Autowired
    @Qualifier("merchantWxMaService")
    private WxMaService wxService;


    /**
     * 账号登录
     *
     * @param body    请求内容，{ username: xxx, password: xxx }
     * @param request 请求对象
     * @return 登录结果
     */
    @PostMapping("login")
    @Transactional
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

        //MerchantInfo
        MerchantInfo merchantInfo = new MerchantInfo();
        //设置UserInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatarUrl(user.getAvatar());
        userInfo.setGender(user.getGender());
        userInfo.setNickName(user.getNickname());
        merchantInfo.setUserInfo(userInfo);

        //判断是否已认证
        JindouyunBrand brand = brandService.findByUserId(user.getId());
        BrandInfo brandInfo = new BrandInfo();
        if(brand != null){
            //设置Address
            JindouyunAddress address = addressService.queryById(brand.getAdderssId());
            if(address!=null){
                userInfo.setCountry(address.getCounty());
                userInfo.setProvince(address.getProvince());
                userInfo.setCity(address.getCity());
            }
            merchantInfo.setAddress(address);

            brandInfo.setId(brand.getId());
            brandInfo.setName(brand.getName());
            brandInfo.setDesc(brand.getDesc());
            brandInfo.setPicUrl(brand.getPicUrl());
            brandInfo.setDelivery_price(brand.getDeliveryPrice());
            brandInfo.setStart_time(brand.getStartTime());
            brandInfo.setEnd_time(brand.getEndTime());
//            brandInfo.setToday_order(brand.getTodayOrder());
//            brandInfo.setToday_turnover(brand.getTodayTurnover());
            double averagePrice = brand.getTotalOrder()== 0 ? 0:brand.getTotalTurnover().doubleValue()/brand.getTotalOrder();
            brandInfo.setAverage_price(new BigDecimal(averagePrice));
            //设置认证
            merchantInfo.setAuth(true);

        }

        //判断是否已申请认证
        JindouyunRegisteBrand registeBrand = registerBrandService.queryByUid(user.getId());
        if(registeBrand != null){
            merchantInfo.setApply(true);
        }

       merchantInfo.setBrandInfo(brandInfo);
        // token
        String token = UserTokenManager.generateToken(user.getId());

        //加入用户缓存
        MerchantUserManager.merchantInfoMap.put(user.getId(),merchantInfo);

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("merchant", merchantInfo);
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
    @Transactional
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

        //MerchantInfo
        MerchantInfo merchantInfo = new MerchantInfo();
//        //设置UserInfo
//        UserInfo userInfo = new UserInfo();
//        userInfo.setAvatarUrl(user.getAvatar());
//        userInfo.setGender(user.getGender());
//        userInfo.setNickName(user.getNickname());
        merchantInfo.setUserInfo(userInfo);

        //判断是否已认证
        JindouyunBrand brand = brandService.findByUserId(user.getId());
        BrandInfo brandInfo = new BrandInfo();
        if(brand != null){
            //设置Address
            JindouyunAddress address = addressService.queryById(brand.getAdderssId());
            if(address!=null){
                userInfo.setCountry(address.getCounty());
                userInfo.setProvince(address.getProvince());
                userInfo.setCity(address.getCity());
            }
            merchantInfo.setAddress(address);

            brandInfo.setId(brand.getId());
            brandInfo.setName(brand.getName());
            brandInfo.setNotice(brand.getNotice());
            brandInfo.setDesc(brand.getDesc());
            brandInfo.setPicUrl(brand.getPicUrl());
            brandInfo.setDelivery_price(brand.getDeliveryPrice());
            brandInfo.setStart_time(brand.getStartTime());
            brandInfo.setEnd_time(brand.getEndTime());
//            brandInfo.setToday_order(brand.getTodayOrder());
//            brandInfo.setToday_turnover(brand.getTodayTurnover());
            double averagePrice = brand.getTotalOrder()== 0 ? 0:brand.getTotalTurnover().doubleValue()/brand.getTotalOrder();
            brandInfo.setAverage_price(new BigDecimal(averagePrice));
            //设置认证
            merchantInfo.setAuth(true);
        }

        //判断是否已申请认证
        JindouyunRegisteBrand registeBrand = registerBrandService.queryByUid(user.getId());
        if(registeBrand != null){
            merchantInfo.setApply(true);
        }

        merchantInfo.setBrandInfo(brandInfo);

        // token
        String token = UserTokenManager.generateToken(user.getId());

        //加入用户缓存
        MerchantUserManager.merchantInfoMap.put(user.getId(),merchantInfo);

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("merchant", merchantInfo);
        return ResponseUtil.ok(result);
    }

    /**
     * 注册成为商家
     * @return
     */
    @PostMapping("/register")
    @Transactional
    public Object register(@LoginUser Integer userId, @RequestBody String body){
        String name = JacksonUtil.parseString(body,"name");
        String desc = JacksonUtil.parseString(body,"desc");
        String picUrl = JacksonUtil.parseString(body,"picUrl");
        String addressDetail = JacksonUtil.parseString(body,"address");
        String phone = JacksonUtil.parseString(body,"phone");
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(addressDetail) || StringUtils.isEmpty(phone)){
            ResponseUtil.badArgument();
        }
        JindouyunRegisteBrand registerBrand = new JindouyunRegisteBrand();
        registerBrand.setBrandid(userId);
        registerBrand.setName(name);
        registerBrand.setDesc(desc);
        registerBrand.setPicUrl(picUrl);
        JindouyunAddress address = new JindouyunAddress();
        address.setUserId(userId);
        address.setAddressDetail(addressDetail);
        address.setName(name);
        address.setTel(phone);
        if ( addressService.add(address) == 0){
            System.err.println("register - 地址添加失败");
            return ResponseUtil.fail();
        }
        List<JindouyunAddress> addressList = addressService.queryByUid(userId);
        if(addressList == null || addressList.size() == 0){
            System.err.println("register - 地址添加失败");
            return ResponseUtil.fail();
        }
        registerBrand.setAdderssId(addressList.get(0).getId());
        registerBrandService.add(registerBrand);
        MerchantInfo merchantInfo = MerchantUserManager.merchantInfoMap.get(userId);
        merchantInfo.setApply(true);
        MerchantUserManager.merchantInfoMap.put(userId,merchantInfo);
        return ResponseUtil.ok();
    }


//    /**
//     * 请求验证码
//     * <p>
//     * 这里需要一定机制防止短信验证码被滥用
//     *
//     * @param body 手机号码 { mobile: xxx}
//     * @return
//     */
//    @Override
//    @PostMapping("captcha")
//    public Object captcha(@RequestBody String body) {
//        String phoneNumber = JacksonUtil.parseString(body, "mobile");
//        Object result = super.captcha(phoneNumber);
//        return result;
//    }

//    /**
//     * 账号密码重置
//     *
//     * @param body    请求内容
//     *                {
//     *                password: xxx,
//     *                mobile: xxx
//     *                code: xxx
//     *                }
//     *                其中code是手机验证码，目前还不支持手机短信验证码
//     * @return 登录结果
//     * 成功则 { errno: 0, errmsg: '成功' }
//     * 失败则 { errno: XXX, errmsg: XXX }
//     */
//    @PostMapping("reset")
//    public Object reset(@RequestBody String body) {
//        String password = JacksonUtil.parseString(body, "password");
//        String mobile = JacksonUtil.parseString(body, "mobile");
//        String code = JacksonUtil.parseString(body, "code");
//
//        Object result = super.reset(password,mobile,code);
//        return result;
//    }

//    /**
//     * 账号手机号码重置
//     *
//     * @param body    请求内容
//     *                {
//     *                password: xxx,
//     *                mobile: xxx
//     *                code: xxx
//     *                }
//     *                其中code是手机验证码，目前还不支持手机短信验证码
//     * @return 登录结果
//     * 成功则 { errno: 0, errmsg: '成功' }
//     * 失败则 { errno: XXX, errmsg: XXX }
//     */
//    @PostMapping("resetPhone")
//    public Object resetPhone(@LoginUser Integer userId, @RequestBody String body) {
//        if (userId == null) {
//            return ResponseUtil.unlogin();
//        }
//        String password = JacksonUtil.parseString(body, "password");
//        String mobile = JacksonUtil.parseString(body, "mobile");
//        String code = JacksonUtil.parseString(body, "code");
//
//        Object result = super.resetPhone(userId,password,mobile,code);
//
//        return result;
//    }


//    /**
//     * 账号信息更新
//     *
//     * @param body    请求内容
//     *                {
//     *                avatar: xxx,
//     *                gender: xxx
//     *                nickname: xxx
//     *                }
//     * @return 登录结果
//     */
//    @PostMapping("profile")
//    public Object profile(@LoginUser Integer userId, @RequestBody String body) {
//        if (userId == null) {
//            return ResponseUtil.unlogin();
//        }
//        String avatar = JacksonUtil.parseString(body, "avatar");
//        Byte gender = JacksonUtil.parseByte(body, "gender");
//        String nickname = JacksonUtil.parseString(body, "nickname");
//
//        Map<String, Object> result = (Map<String, Object>) super.profile(userId,gender,avatar,nickname);
//        if((Integer) result.get("errno") == 0){
//            UserInfo userInfo = MerchantUserManager.merchantInfoMap.get(userId).getUserInfo();
//            userInfo.setAvatarUrl(avatar);
//            userInfo.setNickName(nickname);
//            userInfo.setGender(gender);
//            MerchantUserManager.merchantInfoMap.get(userId).setUserInfo(userInfo);
//        }
//
//        return result;
//    }

//    /**
//     * 微信手机号码绑定
//     *
//     * @param userId
//     * @param body
//     * @return
//     */
//    @Override
//    @PostMapping("bindPhone")
//    public Object bindPhone(Integer userId, @RequestBody String body) {
//    	if (userId == null) {
//            return ResponseUtil.unlogin();
//        }
//    	JindouyunUser user = userService.findById(userId);
//        String encryptedData = JacksonUtil.parseString(body, "encryptedData");
//        String iv = JacksonUtil.parseString(body, "iv");
//        WxMaPhoneNumberInfo phoneNumberInfo = this.wxService.getUserService().getPhoneNoInfo(user.getSessionKey(), encryptedData, iv);
//        String phone = phoneNumberInfo.getPhoneNumber();
//        user.setMobile(phone);
//        if (userService.updateById(user) == 0) {
//            return ResponseUtil.updatedDataFailed();
//        }
//        return ResponseUtil.ok();
//    }

    @Override
    @PostMapping("logout")
    public Object logout(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        //移除缓存
        MerchantUserManager.merchantInfoMap.remove(userId);
        return ResponseUtil.ok();
    }

    @Override
    @GetMapping("info")
    public Object info(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        return ResponseUtil.ok(MerchantUserManager.merchantInfoMap.get(userId));
    }

}
