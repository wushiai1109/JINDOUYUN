package com.jindouyun.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatVo {
    private String[] columns = new String[0];
    private List<Map> rows = new ArrayList<>();

    public void add(Map... r) {
        rows.addAll(Arrays.asList(r));
    }
}
