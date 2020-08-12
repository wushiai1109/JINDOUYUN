package com.jindouyun.admin.model.vo;

import com.jindouyun.db.domain.JindouyunExpressOrder;
import com.jindouyun.db.domain.JindouyunOrderSplit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @className: MergeInfo
 * @description:
 * @author: ZSZ
 * @date: 2020/8/11 0:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeExpressInfo {

    private int id;
    private String orderSn;
    private String message;
    private BigDecimal allPrice;
    private List<JindouyunExpressOrder> splitOrder;

}
