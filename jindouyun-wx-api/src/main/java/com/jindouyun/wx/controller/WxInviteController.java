package com.jindouyun.wx.controller;

import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.dao.JindouyunInviteMapper;
import com.jindouyun.db.domain.JindouyunInvite;
import com.jindouyun.db.service.JindouyunInviteService;
import com.jindouyun.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName WxInviteController
 * @Description
 * @Author Bruce
 * @Date 2020/8/4 2:12 下午
 */
@RestController
@RequestMapping("/wx/invite")
@CrossOrigin(origins = "*",maxAge = 3600)
public class WxInviteController {

    @Autowired
    private JindouyunInviteService inviteService;

    @Autowired
    private JindouyunInviteMapper inviteMapper;

    /**
     * 填写邀请码
     * @param userId 用户ID
     * @param invite 邀请码账号： { inviteId: xxx }
     * @return
     */
    @PostMapping("/submit")
    public Object submit(@LoginUser Integer userId, @RequestBody JindouyunInvite invite) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        if (invite == null) {
            return ResponseUtil.badArgument();
        }
        invite.setInvitedUserId(userId);



        inviteService.submit(invite);
        return ResponseUtil.ok();

    }

}
