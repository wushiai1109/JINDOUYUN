package com.jindouyun.wx.service;

import com.jindouyun.db.dao.JindouyunUserMapper;
import com.jindouyun.db.domain.JindouyunUser;
import com.jindouyun.db.domain.JindouyunUserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName JindouyunUserService
 * @Description 鉴权服务
 * @Author Bruce
 * @Date 2020/7/22 7:11 下午
 */
@Service
@Transactional
public class JindouyunUserService {

    @Autowired
    JindouyunUserMapper userMapper;

    public List<JindouyunUser> queryByUsername(String username) {
        JindouyunUserExample userExample = new JindouyunUserExample();
        userExample.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return userMapper.selectByExample(userExample);
    }

    public int updateById(JindouyunUser user) {
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateByPrimaryKeySelective(user);
    }

    public JindouyunUser queryByOid(String openId) {
        JindouyunUserExample userExample = new JindouyunUserExample();
        userExample.or().andWeixinOpenidEqualTo(openId).andDeletedEqualTo(false);
        return userMapper.selectOneByExample(userExample);
    }

    public void add(JindouyunUser user) {
        user.setAddTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insertSelective(user);
    }

    public List<JindouyunUser> queryByMobile(String mobile) {
        JindouyunUserExample userExample = new JindouyunUserExample();
        userExample.or().andMobileEqualTo(mobile).andDeletedEqualTo(false);
        return userMapper.selectByExample(userExample);
    }


    public List<JindouyunUser> queryByOpenid(String openId) {
        JindouyunUserExample userExample = new JindouyunUserExample();
        userExample.or().andWeixinOpenidEqualTo(openId).andDeletedEqualTo(false);
        return userMapper.selectByExample(userExample);
    }


    public JindouyunUser findById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }


}
