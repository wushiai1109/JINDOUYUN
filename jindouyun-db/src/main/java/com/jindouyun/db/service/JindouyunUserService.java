package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunUserMapper;
import com.jindouyun.db.domain.JindouyunUser;
import com.jindouyun.db.domain.JindouyunUserExample;
import com.jindouyun.db.domain.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunUserService {
    @Resource
    private JindouyunUserMapper userMapper;

    /**
     * 查找
     * @param userId
     * @return
     */
    public JindouyunUser findById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 根据 userId查找 返回UserVo
     * @param userId
     * @return
     */
    public UserVo findUserVoById(Integer userId) {
        JindouyunUser user = findById(userId);
        UserVo userVo = new UserVo();
        userVo.setNickname(user.getNickname());
        userVo.setAvatar(user.getAvatar());
        return userVo;
    }

    /**
     * 根据openId 查找
     * @param openId
     * @return
     */
    public JindouyunUser queryByOid(String openId) {
        JindouyunUserExample example = new JindouyunUserExample();
        example.or().andWeixinOpenidEqualTo(openId).andDeletedEqualTo(false);
        return userMapper.selectOneByExample(example);
    }

    public void add(JindouyunUser user) {
        user.setAddTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insertSelective(user);
    }

    public int updateById(JindouyunUser user) {
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 条件判断 username 或 mobile
     * @param username
     * @param mobile
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunUser> querySelective(String username, String mobile, Integer page, Integer size, String sort, String order) {
        JindouyunUserExample example = new JindouyunUserExample();
        JindouyunUserExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        if (!StringUtils.isEmpty(mobile)) {
            criteria.andMobileEqualTo(mobile);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return userMapper.selectByExample(example);
    }

    /**
     * 用户数
     * @return
     */
    public int count() {
        JindouyunUserExample example = new JindouyunUserExample();
        example.or().andDeletedEqualTo(false);

        return (int) userMapper.countByExample(example);
    }

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    public List<JindouyunUser> queryByUsername(String username) {
        JindouyunUserExample example = new JindouyunUserExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    /**
     * 验证是否存在 该用户 username
     * @param username
     * @return
     */
    public boolean checkByUsername(String username) {
        JindouyunUserExample example = new JindouyunUserExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return userMapper.countByExample(example) != 0;
    }

    /**
     * 通过手机号码查询
     * @param mobile
     * @return
     */
    public List<JindouyunUser> queryByMobile(String mobile) {
        JindouyunUserExample example = new JindouyunUserExample();
        example.or().andMobileEqualTo(mobile).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public List<JindouyunUser> queryByOpenid(String openid) {
        JindouyunUserExample example = new JindouyunUserExample();
        example.or().andWeixinOpenidEqualTo(openid).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }


    public void deleteById(Integer id) {
        userMapper.logicalDeleteByPrimaryKey(id);
    }
}
