package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunFootprintMapper;
import com.jindouyun.db.domain.JindouyunFootprint;
import com.jindouyun.db.domain.JindouyunFootprintExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName JindouyunFootprintService
 * @Description
 * @Author Bruce
 * @Date 2020/7/24 3:32 下午
 */
@Service
@Transactional
public class JindouyunFootprintService {
    
    @Autowired
    private JindouyunFootprintMapper footprintMapper;
    
    public JindouyunFootprint findById(Integer footprintId) {
        return footprintMapper.selectByPrimaryKey(footprintId);
    }

    public void deleteById(Integer footprintId) {
        footprintMapper.logicalDeleteByPrimaryKey(footprintId);
    }


    public List<JindouyunFootprint> queryByAddTime(Integer userId, Integer page, Integer limit) {
        JindouyunFootprintExample example = new JindouyunFootprintExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        example.setOrderByClause(JindouyunFootprint.Column.addTime.desc());
        PageHelper.startPage(page, limit);
        return footprintMapper.selectByExample(example);
    }
}
