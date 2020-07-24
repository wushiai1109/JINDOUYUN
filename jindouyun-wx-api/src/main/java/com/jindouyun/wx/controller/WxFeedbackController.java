package com.jindouyun.wx.controller;

import com.jindouyun.core.util.RegexUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunFeedback;
import com.jindouyun.db.domain.JindouyunUser;
import com.jindouyun.wx.annotation.LoginUser;
import com.jindouyun.wx.service.JindouyunFeedbackService;
import com.jindouyun.wx.service.JindouyunUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName WxFeedbackController
 * @Description 意见反馈服务
 * @Author Bruce
 * @Date 2020/7/24 3:12 下午
 */
@RestController
@RequestMapping("/wx/feedback")
@CrossOrigin(origins = "*",maxAge = 3600)
public class WxFeedbackController {
    @Autowired
    private JindouyunFeedbackService feedbackService;
    @Autowired
    private JindouyunUserService userService;

    private Object validate(JindouyunFeedback feedback) {
        String content = feedback.getContent();
        if (StringUtils.isEmpty(content)) {
            return ResponseUtil.badArgument();
        }

        String type = feedback.getFeedType();
        if (StringUtils.isEmpty(type)) {
            return ResponseUtil.badArgument();
        }

        Boolean hasPicture = feedback.getHasPicture();
        if (hasPicture == null || !hasPicture) {
            feedback.setPicUrls(new String[0]);
        }

        // 测试手机号码是否正确
        String mobile = feedback.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return ResponseUtil.badArgument();
        }
        if (!RegexUtil.isMobileExact(mobile)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    /**
     * 添加意见反馈
     *
     * @param userId   用户ID
     * @param feedback 意见反馈
     * @return 操作结果
     */
    @PostMapping("submit")
    public Object submit(@LoginUser Integer userId, @RequestBody JindouyunFeedback feedback) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Object error = validate(feedback);
        if (error != null) {
            return error;
        }

        JindouyunUser user = userService.findById(userId);
        String username = user.getUsername();
        feedback.setId(null);
        feedback.setUserId(userId);
        feedback.setUsername(username);
        //状态默认是0，1表示状态已发生变化
        feedback.setStatus(1);
        feedbackService.add(feedback);

        return ResponseUtil.ok();
    }
}
