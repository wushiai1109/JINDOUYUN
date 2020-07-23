package com.jindouyun.wx.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunCommentMapper;
import com.jindouyun.db.domain.JindouyunComment;
import com.jindouyun.db.domain.JindouyunCommentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName JindouyunCommentService
 * @Description
 * @Author Bruce
 * @Date 2020/7/23 8:31 下午
 */
@Service
@Transactional
public class JindouyunCommentService {

    @Autowired
    private JindouyunCommentMapper commentMapper;

    public int save(JindouyunComment comment) {
        comment.setAddTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        return commentMapper.insertSelective(comment);
    }

    public int count(Byte type, Integer valueId, int i) {
        JindouyunCommentExample example = new JindouyunCommentExample();
//        if (showType == 0) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andDeletedEqualTo(false);
//        } else if (showType == 1) {
//            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andHasPictureEqualTo(true).andDeletedEqualTo(false);
//        } else {
//            throw new RuntimeException("showType不支持");
//        }
        return (int) commentMapper.countByExample(example);
    }

    public List<JindouyunComment> query(Byte type, Integer valueId, Integer showType, Integer page, Integer limit) {
        JindouyunCommentExample example = new JindouyunCommentExample();
        example.setOrderByClause(JindouyunComment.Column.addTime.desc());
//        if (showType == 0) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andDeletedEqualTo(false);
//        } else if (showType == 1) {
//            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andHasPictureEqualTo(true).andDeletedEqualTo(false);
//        } else {
//            throw new RuntimeException("showType不支持");
//        }
        PageHelper.startPage(page, limit);
        return commentMapper.selectByExample(example);
    }

    public String queryReply(Integer id) {
        JindouyunCommentExample example = new JindouyunCommentExample();
        example.or().andTypeEqualTo((byte) 2).andValueIdEqualTo(id);
        List<JindouyunComment> commentReply = commentMapper.selectByExampleSelective(example, JindouyunComment.Column.content);
        // 目前业务只支持回复一次
        if (commentReply.size() == 1) {
            return commentReply.get(0).getContent();
        }
        return null;
    }
}
