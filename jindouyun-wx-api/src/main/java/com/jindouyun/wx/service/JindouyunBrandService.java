package com.jindouyun.wx.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunBrandMapper;
import com.jindouyun.db.domain.JindouyunBrand;
import com.jindouyun.db.domain.JindouyunBrand.Column;
import com.jindouyun.db.domain.JindouyunBrandExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName JindouyunBrandService
 * @Description
 * @Author Bruce
 * @Date 2020/7/21 8:18 下午
 */
@Service
@Transactional
public class JindouyunBrandService {

    @Autowired
    private JindouyunBrandMapper brandMapper;

    private Column[] columns = new Column[]{Column.id, Column.name, Column.desc, Column.picUrl, Column.floorPrice};

    public List<JindouyunBrand> query(Integer page, Integer limit, String sort, String order) {
        JindouyunBrandExample example = new JindouyunBrandExample();
        example.or().andDeletedEqualTo(false);
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }
        PageHelper.startPage(page, limit);
        return brandMapper.selectByExampleSelective(example, columns);
    }

    public JindouyunBrand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

}
