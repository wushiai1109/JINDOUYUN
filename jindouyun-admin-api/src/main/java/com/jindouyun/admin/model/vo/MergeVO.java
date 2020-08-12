package com.jindouyun.admin.model.vo;

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
public class MergeVO {
    private Byte type;
    private List<JindouyunOrderSplit> unMergeList;
    private List<MergeInfo> mergeList;
}
