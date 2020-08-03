package com.jindouyun.db.dao;

import com.jindouyun.db.domain.JindouyunOrder;
//import com.jindouyun.db.domain.JindouyunOrderDemo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName JindouyunOrderMapperDemo
 * @Description
 * @Author Bruce
 * @Date 2020/8/3 4:34 下午
 */
@Repository
@Mapper
public interface JindouyunOrderMapperDemo {

    @Select("SELECT * from jindouyun_order where jindouyun_order.user_id = #{userId} and jindouyun_order.id in (select jindouyun_order_goods.order_id from jindouyun_order_goods where jindouyun_order_goods.goods_name LIKE CONCAT('%',#{keyword},'%'))")
    List<JindouyunOrder> find(@Param("userId") Integer userId, @Param("keyword") String keyword);

}
