package com.jindouyun.admin.util;

import com.jindouyun.common.util.RegexUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.*;
import org.springframework.util.StringUtils;

import static com.jindouyun.admin.util.AdminResponseCode.ADMIN_INVALID_NAME;
import static com.jindouyun.admin.util.AdminResponseCode.ADMIN_INVALID_PASSWORD;

/**
 * @className: ValidateUtil
 * @description: 校验
 * @author: ZSZ
 * @date: 2020/8/7 14:57
 */
public class ValidateUtil {

    /**
     * 验证广告是否合法
     *      合法广告：
     *          1. name不为空
     *          2. content不为空
     * @param ad
     * @return
     */
    public static Object validate(JindouyunAd ad) {
        String name = ad.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        String content = ad.getContent();
        if (StringUtils.isEmpty(content)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    /**
     * 验证管理员信息
     *  1. name不为空
     *  2. 名称符合要求
     *  3. 密码符合要求
     * @param admin
     * @return
     */
    public static Object validate(JindouyunAdmin admin) {
        String name = admin.getUsername();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        if (!RegexUtil.isUsername(name)) {
            return ResponseUtil.fail(ADMIN_INVALID_NAME, "管理员名称不符合规定");
        }
        String password = admin.getPassword();
        if (StringUtils.isEmpty(password) || password.length() < 6) {
            return ResponseUtil.fail(ADMIN_INVALID_PASSWORD, "管理员密码长度不能小于6");
        }
        return null;
    }

    /**
     * 校验商家信息
     * @param brand
     * @return
     */
    public static Object validate(JindouyunBrand brand) {
        String name = brand.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }

        String desc = brand.getDesc();
        if (StringUtils.isEmpty(desc)) {
            return ResponseUtil.badArgument();
        }

//        BigDecimal price = brand.getFloorPrice();
//        if (price == null) {
//            return ResponseUtil.badArgument();
//        }
        return null;
    }


    /**
     * 校验 目录
     * @param category
     * @return
     */
    public static Object validate(JindouyunCategory category) {
        String name = category.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }

        String level = category.getLevel();
        if (StringUtils.isEmpty(level)) {
            return ResponseUtil.badArgument();
        }
        if (!level.equals("L1") && !level.equals("L2")) {
            return ResponseUtil.badArgumentValue();
        }

        Integer pid = category.getPid();
        if (level.equals("L2") && (pid == null)) {
            return ResponseUtil.badArgument();
        }

        return null;
    }

    /**
     * 优惠券校验
     * @param coupon
     * @return
     */
    public static Object validate(JindouyunCoupon coupon) {
        String name = coupon.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    /**
     * issue 验证
     * @param issue
     * @return
     */
    public static Object validate(JindouyunIssue issue) {
        String question = issue.getQuestion();
        if (StringUtils.isEmpty(question)) {
            return ResponseUtil.badArgument();
        }
        String answer = issue.getAnswer();
        if (StringUtils.isEmpty(answer)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    /**
     * 角色 校验
     * @param role
     * @return
     */
    public static Object validate(JindouyunRole role) {
        String name = role.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }

        return null;
    }

}
