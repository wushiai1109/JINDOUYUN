package com.jindouyun.wx.service;

import com.jindouyun.db.service.JindouyunRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName GetRegionService
 * @Description
 * @Author Bruce
 * @Date 2020/7/22 9:10 上午
 */
@Service
@Transactional
public class GetRegionService {

    @Autowired
    private JindouyunRegionService regionService;



}
