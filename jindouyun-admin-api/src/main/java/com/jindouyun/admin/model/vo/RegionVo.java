package com.jindouyun.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionVo {
    private Integer id;
    private String name;
    private Byte type;
    private Integer code;

    private List<RegionVo> children;

}
