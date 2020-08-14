package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunBrandOrderMapper;
import com.jindouyun.db.domain.JindouyunBrandOrder;
import com.jindouyun.db.domain.JindouyunBrandOrderExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @className: JindouyunBrandOrderService
 * @description:
 * @author: ZSZ
 * @date: 2020/8/13 14:48
 */
@Service
public class JindouyunBrandOrderService {

    @Resource
    private JindouyunBrandOrderMapper brandOrderMapper;

    /**
     * 根据mergeId查询
     * @param mergeId
     * @return
     */
    public JindouyunBrandOrder queryByMergeId(Integer mergeId){
        JindouyunBrandOrderExample example = new JindouyunBrandOrderExample();
        example.or().andOrderIdEqualTo(mergeId).andDeletedEqualTo(false);
        return brandOrderMapper.selectOneByExample(example);
    }

    /**
     * 添加 brandOrder
     * @param brandOrder
     */
    public void add(JindouyunBrandOrder brandOrder){
        brandOrder.setAddTime(LocalDateTime.now());
        brandOrder.setUpdateTime(LocalDateTime.now());
        brandOrder.setDeleted(false);
        brandOrderMapper.insertSelective(brandOrder);
    }
}
