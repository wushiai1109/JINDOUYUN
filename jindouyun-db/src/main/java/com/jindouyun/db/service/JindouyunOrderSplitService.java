package com.jindouyun.db.service;

import com.github.pagehelper.PageHelper;
import com.jindouyun.db.dao.JindouyunOrderSplitMapper;
import com.jindouyun.db.domain.JindouyunOrderSplit;
import com.jindouyun.db.domain.JindouyunOrderSplit.Column;
import com.jindouyun.db.domain.JindouyunOrderSplitExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @className: JindouyunOrderSplitService
 * @description:
 * @author: ZSZ
 * @date: 2020/8/9 20:10
 */
@Service
public class JindouyunOrderSplitService {

    Column[] setMergeIdNullColumns = new JindouyunOrderSplit.Column[]{Column.mergeId,Column.updateTime};

    @Resource
    private JindouyunOrderSplitMapper splitMapper;

    public int setMergeIdNull(Integer mergeId){
        JindouyunOrderSplit orderSplit = new JindouyunOrderSplit();
        orderSplit.setMergeId(-1);
        orderSplit.setUpdateTime(LocalDateTime.now());
        JindouyunOrderSplitExample example = new JindouyunOrderSplitExample();
        example.or().andMergeIdEqualTo(mergeId);
        return splitMapper.updateByExampleSelective(orderSplit,example);
    }

    /**
     * 添加mergeId
     * @param orderSplitId
     * @param mergeId
     * @return
     */
    public int updateMergeId(Integer orderSplitId, Integer mergeId){
        JindouyunOrderSplit orderSplit = new JindouyunOrderSplit();
        orderSplit.setMergeId(mergeId);
        orderSplit.setUpdateTime(LocalDateTime.now());
        JindouyunOrderSplitExample example = new JindouyunOrderSplitExample();
        example.or().andIdEqualTo(orderSplitId);
        return splitMapper.updateByExampleSelective(orderSplit,example);
    }

    public JindouyunOrderSplit queryById(Integer id){return splitMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据 mergerId brandId orderId 以及 orderStatus 查找
     * @param mergerId
     * @param brandId
     * @param orderStatusArray
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    public List<JindouyunOrderSplit> querySelective(Byte type, Integer mergerId,Integer brandId, List<Short> orderStatusArray, List<Integer> orderIdArray, Integer page, Integer limit, String sort, String order){
        JindouyunOrderSplitExample example = new JindouyunOrderSplitExample();
        JindouyunOrderSplitExample.Criteria criteria = example.createCriteria();

        //商品
        if( type == 0){
            if(brandId != null && brandId !=0){
                return new ArrayList<>();
            }
            criteria.andBrandIdEqualTo(0);
        //外卖
        }else if( type == 1){

            if(brandId != null){
                criteria.andBrandIdEqualTo(brandId);
            }else{
                criteria.andBrandIdNotEqualTo(0);
            }
        }
        if(mergerId != null){
            criteria.andMergeIdEqualTo(mergerId);
        }

        if(orderIdArray != null){
            criteria.andOrderIdIn(orderIdArray);
        }

        if(orderStatusArray != null){
            criteria.andOrderStatusIn(orderStatusArray);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page,limit);

        return splitMapper.selectByExample(example);
    }

}
