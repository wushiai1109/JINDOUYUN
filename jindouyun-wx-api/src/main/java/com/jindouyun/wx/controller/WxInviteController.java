package com.jindouyun.wx.controller;

import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.dao.JindouyunInviteMapper;
import com.jindouyun.db.domain.JindouyunInvite;
import com.jindouyun.db.domain.JindouyunInviteExample;
import com.jindouyun.db.service.JindouyunInviteService;
import com.jindouyun.common.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WxInviteController
 * @Description 邀请
 * @Author Bruce
 * @Date 2020/8/4 2:12 下午
 */
@RestController
@RequestMapping("/wx/invite")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WxInviteController {

    @Autowired
    private JindouyunInviteService inviteService;

    @Autowired
    private JindouyunInviteMapper inviteMapper;

    /**
     * 填写邀请码
     *
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
        if (userId.intValue() != invite.getInvitedUserId().intValue()){
            ResponseUtil.fail(500, "用户不能填写本人的邀请码");
        }
        invite.setInvitedUserId(userId);

        JindouyunInviteExample example = new JindouyunInviteExample();
        JindouyunInviteExample.Criteria criteria = example.createCriteria();

        criteria.andInvitedUserIdEqualTo(userId);
        criteria.andDeletedEqualTo(false);

        JindouyunInvite jindouyunInvite = inviteMapper.selectOneByExample(example);

        if (jindouyunInvite == null) {
            inviteService.submit(invite);
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail(500, "该账号已被邀请过");
    }

    /**
     * 查看自己的邀请码以及邀请人数
     *
     * @param userId 用户ID
     * @return
     */
    @GetMapping("/show")
    public Object show(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        JindouyunInviteExample example = new JindouyunInviteExample();
        JindouyunInviteExample.Criteria criteria = example.createCriteria();

        criteria.andInviteIdEqualTo(userId);
        criteria.andDeletedEqualTo(false);

        long count = inviteMapper.countByExample(example);

        Map<Object, Object> result = new HashMap<>();
        result.put("inviteUserId", userId);
        result.put("count", count);
        return ResponseUtil.ok(result);
    }

}
