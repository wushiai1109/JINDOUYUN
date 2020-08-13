package com.jindouyun.admin.model.vo;

import com.jindouyun.db.domain.JindouyunClockin;
import com.jindouyun.db.domain.JindouyunDeliveriesPerformance;
import com.jindouyun.db.domain.JindouyunDeliveryStaff;
import com.jindouyun.db.domain.JindouyunUser;
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

    private JindouyunUser user;
    private JindouyunDeliveryStaff staff;
    private List<JindouyunClockin> workTimes;
    private JindouyunDeliveriesPerformance performance;

}
