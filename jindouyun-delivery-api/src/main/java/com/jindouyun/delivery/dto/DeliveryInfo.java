package com.jindouyun.delivery.dto;

import com.jindouyun.db.domain.JindouyunDeliveryStaff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @className: DeliveryInfo
 * @description: 骑手信息
 * @author: ZSZ
 * @date: 2020/8/4 15:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfo {
    private String username;
    private byte gender;
    private LocalDate birthday;
    private String nickname;
    private String mobile;
    private String avatar;
    private boolean auth;
    private boolean isApply;
    private JindouyunDeliveryStaff deliveryStaff;
}
