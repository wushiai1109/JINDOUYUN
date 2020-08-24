package com.jindouyun.merchant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @className:
 * @description:
 * @author: ZSZ
 * @date: 2020/8/21 20:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    private String date;
    private String orderSn;
    private BigDecimal price;
}
