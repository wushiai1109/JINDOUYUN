package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunInviteMapper;
import com.jindouyun.db.domain.JindouyunInvite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @ClassName JindouyunInviteService
 * @Description
 * @Author Bruce
 * @Date 2020/8/4 2:13 下午
 */
@Service
@Transactional
public class JindouyunInviteService {

    @Autowired
    private JindouyunInviteMapper inviteMapper;

    public void submit(JindouyunInvite invite) {
        invite.setAddTime(LocalDateTime.now());
        invite.setUpdateTime(LocalDateTime.now());
        inviteMapper.insertSelective(invite);
    }
}
