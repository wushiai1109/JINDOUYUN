package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunMergeOrderMapper;
import com.jindouyun.db.domain.JindouyunMergeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @ClassName JindouyunMergeOrderService
 * @Description
 * @Author Bruce
 * @Date 2020/8/10 5:18 下午
 */
@Service
@Transactional
public class JindouyunMergeOrderService {

    @Autowired
    private JindouyunMergeOrderMapper mergeOrderMapper;

    public JindouyunMergeOrder selectById(Integer orderId) {
        return mergeOrderMapper.selectByPrimaryKey(orderId);
    }
}
