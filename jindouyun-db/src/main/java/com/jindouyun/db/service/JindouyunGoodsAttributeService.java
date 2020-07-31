package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunGoodsAttributeMapper;
import com.jindouyun.db.domain.JindouyunGoodsAttribute;
import com.jindouyun.db.domain.JindouyunGoodsAttributeExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JindouyunGoodsAttributeService {
    @Resource
    private JindouyunGoodsAttributeMapper goodsAttributeMapper;

    public List<JindouyunGoodsAttribute> queryByGid(Integer goodsId) {
        JindouyunGoodsAttributeExample example = new JindouyunGoodsAttributeExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return goodsAttributeMapper.selectByExample(example);
    }

    public void add(JindouyunGoodsAttribute goodsAttribute) {
        goodsAttribute.setAddTime(LocalDateTime.now());
        goodsAttribute.setUpdateTime(LocalDateTime.now());
        goodsAttributeMapper.insertSelective(goodsAttribute);
    }

    public JindouyunGoodsAttribute findById(Integer id) {
        return goodsAttributeMapper.selectByPrimaryKey(id);
    }

    public void deleteByGid(Integer gid) {
        JindouyunGoodsAttributeExample example = new JindouyunGoodsAttributeExample();
        example.or().andGoodsIdEqualTo(gid);
        goodsAttributeMapper.logicalDeleteByExample(example);
    }

    public void deleteById(Integer id) {
        goodsAttributeMapper.logicalDeleteByPrimaryKey(id);
    }

    public void updateById(JindouyunGoodsAttribute attribute) {
        attribute.setUpdateTime(LocalDateTime.now());
        goodsAttributeMapper.updateByPrimaryKeySelective(attribute);
    }
}
