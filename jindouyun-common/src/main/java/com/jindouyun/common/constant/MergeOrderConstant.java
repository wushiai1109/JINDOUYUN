package com.jindouyun.common.constant;

/**
 * @className: MergeOrderConstant
 * @description: 合单状态
 *
 *         21：买家付款，商家接单，未发货，
 *         31：商家发货，等待骑手接单，
 *         32：骑手接单，派送中
 *         33：已送达
 *
 * @author: ZSZ
 * @date: 2020/8/13 13:47
 */

public class MergeOrderConstant {

    public static final Byte MERGE_ORDER_UNDELIVER = 21;
    public static final Byte MERGE_ORDER_MERCHANT_RECEIVE = 22;
    public static final Byte MERGE_ORDER_DELIVER = 31;
    public static final Byte MERGE_ORDER_RECEIVE = 32;
    public static final Byte MERGE_ORDER_ARRIVED = 33;

}
