package com.jindouyun.wx.service;


import com.jindouyun.db.dao.JindouyunAdMapper;
import com.jindouyun.db.domain.JindouyunAd;
import com.jindouyun.db.domain.JindouyunAdExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName JindouyunAdService
 * @Description
 * @Author Bruce
 * @Date 2020/7/21 5:26 下午
 */
@Service
@Transactional
public class JindouyunAdService {

    @Autowired
    private JindouyunAdMapper jindouyunAdMapper;


    public List<JindouyunAd> queryIndex() {
        JindouyunAdExample jindouyunAdExample = new JindouyunAdExample();
        jindouyunAdExample.or().andPositionEqualTo((byte) 1).andDeletedEqualTo(false).andEnabledEqualTo(true);
        return jindouyunAdMapper.selectByExample(jindouyunAdExample);
    }

}
