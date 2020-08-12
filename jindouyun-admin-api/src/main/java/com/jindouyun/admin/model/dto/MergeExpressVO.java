package com.jindouyun.admin.model.dto;

import com.jindouyun.db.domain.JindouyunExpressOrder;
import com.jindouyun.db.domain.JindouyunOrderSplit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @className: MergeVO
 * @description:
 * @author: ZSZ
 * @date: 2020/8/11 0:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeExpressVO {
    private Byte type;
    private List<JindouyunExpressOrder> unMergeList;
    private List<MergeExpressInfo> mergeList;
}
