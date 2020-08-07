package com.jindouyun.core.service;

import com.jindouyun.common.domain.UserInfo;
import com.jindouyun.db.domain.JindouyunUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @className: IAuthService
 * @description: 权限服务
 * @author: ZSZ
 * @date: 2020/8/6 19:25
 */
public interface IAuthService {

    /**
     * 账号登录
     *
     * @param username
     * @param password
     * @return 登录结果
     */
    Object login(String username, String password);

    /**
     * 微信登录
     *
     * @param code
     * @param userInfo
     * @return 登录结果
     */
    Object loginByWeixin(String code, UserInfo userInfo);



    /**
     * 账号注册
     *
     * @param username
     * @param password
     * @param mobile
     * @param code      手机验证码，目前还不支持手机短信验证码
     * @return 登录结果
     *          成功则{
     *                  errno: 0,
     *                  errmsg: '成功',
     *                  data:{
     *                          token: xxx,
     *                          tokenExpire: xxx,
     *                          userInfo: xxx
     *                      }
     *              }
     *          失败则 { errno: XXX, errmsg: XXX }
     */
    Object register(String username, String password, String mobile, String code, JindouyunUser user, HttpServletRequest request);

    /**
     * 请求验证码
     *
     * 这里需要一定机制防止短信验证码被滥用
     *
     * @param mobile
     *
     * @return
     */
    Object captcha(String mobile);

    /**
     * 账号密码重置
     *
     * @param password
     * @param mobile
     * @param code      手机验证码，目前还不支持手机短信验证码
     * @return 登录结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    Object reset(String password,String mobile, String code);

    /**
     * 账号手机号码重置
     *
     * @param userId
     * @param password
     * @param mobile
     * @param code          手机验证码，目前还不支持手机短信验证码
     * @return 登录结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    Object resetPhone(Integer userId,String password,String mobile,String code);


    /**
     * 账号信息更新
     *
     * @param userId
     * @param gender
     * @param avatar
     * @param nickname
     * @return 登录结果
     */
    Object profile(Integer userId,Byte gender, String avatar,String nickname);

    /**
     * 微信手机号码绑定
     *
     * @param userId
     * @param body
     * @return
     */
    Object bindPhone(Integer userId,String body);

    /**
     * 退出登录
     * @param userId
     * @return
     */
    Object logout(Integer userId);

    /**
     * 获取详情
     * @param userId
     * @return
     */
    Object info(Integer userId);

}
