package com.jindouyun.wx.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName CouponVo
 * @Description
 * @Author Bruce
 * @Date 2020/7/24 12:48 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponVo implements Serializable {
    private Integer id;
    private Integer cid;
    private String name;
    private String desc;
    private String tag;
    private BigDecimal min;
    private BigDecimal discount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean available;
}
