package com.jindouyun.wx.service;

import com.jindouyun.db.dao.JindouyunGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName JindouyunGoodsService
 * @Description
 * @Author Bruce
 * @Date 2020/7/21 8:03 下午
 */
@Service
@Transactional
public class JindouyunGoodsService {

    @Autowired
    private JindouyunGoodsMapper jindouyunGoodsMapper;



}
