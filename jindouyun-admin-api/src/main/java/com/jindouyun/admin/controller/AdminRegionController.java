package com.jindouyun.admin.controller;

import com.jindouyun.admin.model.vo.RegionVo;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunRegion;
import com.jindouyun.db.service.JindouyunRegionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/region")
@Validated
public class AdminRegionController {
    private final Log logger = LogFactory.getLog(AdminRegionController.class);

    @Autowired
    private JindouyunRegionService regionService;

    @GetMapping("/clist")
    public Object clist(@NotNull Integer id) {
        List<JindouyunRegion> regionList = regionService.queryByPid(id);
        return ResponseUtil.okList(regionList);
    }

    @GetMapping("/list")
    public Object list() {
        List<RegionVo> regionVoList = new ArrayList<>();

        List<JindouyunRegion> JindouyunRegions = regionService.getAll();
        Map<Byte, List<JindouyunRegion>> collect = JindouyunRegions.stream().collect(Collectors.groupingBy(JindouyunRegion::getType));
        byte provinceType = 1;
        List<JindouyunRegion> provinceList = collect.get(provinceType);
        byte cityType = 2;
        List<JindouyunRegion> city = collect.get(cityType);
        Map<Integer, List<JindouyunRegion>> cityListMap = city.stream().collect(Collectors.groupingBy(JindouyunRegion::getPid));
        byte areaType = 3;
        List<JindouyunRegion> areas = collect.get(areaType);
        Map<Integer, List<JindouyunRegion>> areaListMap = areas.stream().collect(Collectors.groupingBy(JindouyunRegion::getPid));

        for (JindouyunRegion province : provinceList) {
            RegionVo provinceVO = new RegionVo();
            provinceVO.setId(province.getId());
            provinceVO.setName(province.getName());
            provinceVO.setCode(province.getCode());
            provinceVO.setType(province.getType());

            List<JindouyunRegion> cityList = cityListMap.get(province.getId());
            List<RegionVo> cityVOList = new ArrayList<>();
            for (JindouyunRegion cityVo : cityList) {
                RegionVo cityVO = new RegionVo();
                cityVO.setId(cityVo.getId());
                cityVO.setName(cityVo.getName());
                cityVO.setCode(cityVo.getCode());
                cityVO.setType(cityVo.getType());

                List<JindouyunRegion> areaList = areaListMap.get(cityVo.getId());
                List<RegionVo> areaVOList = new ArrayList<>();
                for (JindouyunRegion area : areaList) {
                    RegionVo areaVO = new RegionVo();
                    areaVO.setId(area.getId());
                    areaVO.setName(area.getName());
                    areaVO.setCode(area.getCode());
                    areaVO.setType(area.getType());
                    areaVOList.add(areaVO);
                }

                cityVO.setChildren(areaVOList);
                cityVOList.add(cityVO);
            }
            provinceVO.setChildren(cityVOList);
            regionVoList.add(provinceVO);
        }

        return ResponseUtil.okList(regionVoList);
    }
}
