package com.jindouyun.wx.service;

import com.jindouyun.db.dao.JindouyunCategoryMapper;
import com.jindouyun.db.domain.JindouyunCategory;
import com.jindouyun.db.domain.JindouyunCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName JindouyunCategoryService
 * @Description
 * @Author Bruce
 * @Date 2020/7/21 8:18 下午
 */
@Service
@Transactional
public class JindouyunCategoryService {

    @Autowired
    private JindouyunCategoryMapper jindouyunCategoryMapper;

    public List<JindouyunCategory> queryChannel() {
        JindouyunCategoryExample jindouyunCategoryExample = new JindouyunCategoryExample();
        jindouyunCategoryExample.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return jindouyunCategoryMapper.selectByExample(jindouyunCategoryExample);
    }
}
