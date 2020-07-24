package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunOrder;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

public interface OrderMapper {
    int updateWithOptimisticLocker(@Param("lastUpdateTime") LocalDateTime lastUpdateTime, @Param("order") JindouyunOrder order);
}