package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunCommentMapper;
import com.jindouyun.db.domain.JindouyunComment;
import com.jindouyun.db.domain.JindouyunCommentExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunCommentService {
    @Resource
    private JindouyunCommentMapper commentMapper;

    /**
     * 查询商品评论
     * @param id
     * @param offset
     * @param limit
     * @return
     */
    public List<JindouyunComment> queryGoodsByGid(Integer id, int offset, int limit) {
        JindouyunCommentExample example = new JindouyunCommentExample();
        example.setOrderByClause(JindouyunComment.Column.addTime.desc());
        example.or().andValueIdEqualTo(id).andTypeEqualTo((byte) 0).andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return commentMapper.selectByExample(example);
    }

    /**
     * 条件查询
     * @param type 评论类型，如果type=0，则是商品评论
     * @param valueId 如果type=0，则是商品评论
     * @param showType 评论是否有图片 0：没有 1：有
     * @param offset
     * @param limit
     * @return
     */
    public List<JindouyunComment> query(Byte type, Integer valueId, Integer showType, Integer offset, Integer limit) {
        JindouyunCommentExample example = new JindouyunCommentExample();
        example.setOrderByClause(JindouyunComment.Column.addTime.desc());
        if (showType == 0) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andDeletedEqualTo(false);
        } else if (showType == 1) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andHasPictureEqualTo(true).andDeletedEqualTo(false);
        } else {
            throw new RuntimeException("showType不支持");
        }
        PageHelper.startPage(offset, limit);
        return commentMapper.selectByExample(example);
    }

    /**
     * 查询评论数量
     * @param type
     * @param valueId
     * @param showType
     * @return
     */
    public int count(Byte type, Integer valueId, Integer showType) {
        JindouyunCommentExample example = new JindouyunCommentExample();
        if (showType == 0) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andDeletedEqualTo(false);
        } else if (showType == 1) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andHasPictureEqualTo(true).andDeletedEqualTo(false);
        } else {
            throw new RuntimeException("showType不支持");
        }
        return (int) commentMapper.countByExample(example);
    }

    /**
     * 添加评论
     * @param comment
     * @return
     */
    public int save(JindouyunComment comment) {
        comment.setAddTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        return commentMapper.insertSelective(comment);
    }

    /**
     * 条件查询 userId or valueId
     * @param userId
     * @param valueId 如果type=0，则是商品评论
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunComment> querySelective(String userId, String valueId, Integer page, Integer size, String sort, String order) {
        JindouyunCommentExample example = new JindouyunCommentExample();
        JindouyunCommentExample.Criteria criteria = example.createCriteria();

        // type=2 是订单商品回复，这里过滤
        criteria.andTypeNotEqualTo((byte) 2);

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(valueId)) {
            criteria.andValueIdEqualTo(Integer.valueOf(valueId)).andTypeEqualTo((byte) 0);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return commentMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        commentMapper.logicalDeleteByPrimaryKey(id);
    }

    /**
     * 回复
     * @param id
     * @return
     */
//    public String queryReply(Integer id) {
//        JindouyunCommentExample example = new JindouyunCommentExample();
//        example.or().andTypeEqualTo((byte) 2).andValueIdEqualTo(id);
//        List<JindouyunComment> commentReply = commentMapper.selectByExampleSelective(example, JindouyunComment.Column.content);
//        // 目前业务只支持回复一次
//        if (commentReply.size() == 1) {
//            return commentReply.get(0).getContent();
//        }
//        return null;
//    }

    public JindouyunComment findById(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }
}
