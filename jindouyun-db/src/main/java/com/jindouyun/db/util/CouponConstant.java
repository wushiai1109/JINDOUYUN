package com.jindouyun.db.util;

public class CouponConstant {

    //优惠券类型
    public static final Short TYPE_COMMON = 0;                  //通用券，用户领取
    public static final Short TYPE_REGISTER = 1;                //注册赠券
    public static final Short TYPE_CODE = 2;                    //优惠券码兑换

    //商品限制类型
    public static final Short GOODS_TYPE_ALL = 0;               //全部商品
    public static final Short GOODS_TYPE_CATEGORY = 1;          //类目限制
    public static final Short GOODS_TYPE_ARRAY = 2;             //商品限制

    //优惠券状态
    public static final Short STATUS_NORMAL = 0;                //正常可用
    public static final Short STATUS_EXPIRED = 1;               //过期
    public static final Short STATUS_OUT = 2;                   //下架

    //有效时间限制
    public static final Short TIME_TYPE_DAYS = 0;               //基于领取时间的有效天数days
    public static final Short TIME_TYPE_TIME = 1;               //start_time和end_time是优惠券有效期
}
