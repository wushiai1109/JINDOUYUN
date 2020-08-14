package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunClockinMapper;
import com.jindouyun.db.domain.JindouyunClockin;
import com.jindouyun.db.domain.JindouyunClockinExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName JindouyunClockinService
 * @Description
 * @Author Bruce
 * @Date 2020/8/9 8:22 下午
 */
@Service
@Transactional
public class JindouyunClockinService {

    @Autowired
    private JindouyunClockinMapper clockinMapper;


    public List<JindouyunClockin> todayWork(Integer userId, LocalDateTime date) {
        JindouyunClockinExample clockinExample = new JindouyunClockinExample();
        LocalDateTime startTime = LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),0,0,0);
        LocalDateTime endTime = startTime.minusDays(1);
        clockinExample.or().andUserIdEqualTo(userId).andWorkStartBetween(startTime,endTime).andDeletedEqualTo(false);
        return clockinMapper.selectByExample(clockinExample);
    }


}
