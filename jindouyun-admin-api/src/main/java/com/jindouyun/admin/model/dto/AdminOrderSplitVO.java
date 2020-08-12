package com.jindouyun.admin.model.dto;

import com.jindouyun.db.domain.JindouyunOrderSplit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: AdminOrderSplitVO
 * @description:
 * @author: ZSZ
 * @date: 2020/8/12 14:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderSplitVO {

    private JindouyunOrderSplit orderSplit;

}
