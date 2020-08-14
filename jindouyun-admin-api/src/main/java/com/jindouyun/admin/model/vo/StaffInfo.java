package com.jindouyun.admin.model.vo;

import com.jindouyun.db.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @className: StaffInfo
 * @description:
 * @author: ZSZ
 * @date: 2020/8/13 18:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffInfo {

    private StaffVO staffVO;
    private List<JindouyunClockin> workTimes;
    private StaffPerformance performance;

}
