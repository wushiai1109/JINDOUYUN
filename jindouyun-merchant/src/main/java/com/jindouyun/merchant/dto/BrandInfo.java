package com.jindouyun.merchant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @className:
 * @description:
 * @author: ZSZ
 * @date: 2020/8/5 17:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandInfo {

    private int id;
    private String name;
    private String desc;
    private String notice;
    private String picUrl;
    private Byte start_time;
    private Byte end_time;
    private BigDecimal delivery_price;
    private BigDecimal today_turnover;
    private BigDecimal average_price;
    private int today_order;
    private short status;

}
