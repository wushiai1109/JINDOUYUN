package com.jindouyun.admin.jindouyun.merchant.dto;

import com.jindouyun.db.domain.JindouyunAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private String picUrl;
    private LocalDate start_time;
    private LocalDate end_time;
    private BigDecimal delivery_price;
    private BigDecimal today_turnover;
    private BigDecimal average_price;
    private int today_order;
    private short status;

}
