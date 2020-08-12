package com.jindouyun.admin.model.vo;

import com.jindouyun.db.domain.BrandVo;
import com.jindouyun.db.domain.JindouyunOrderGoods;
import com.jindouyun.db.domain.JindouyunOrderSplit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private BrandVo brandVo;
    private JindouyunOrderSplit orderSplit;
    private List<JindouyunOrderGoods> orderGoods;

}
