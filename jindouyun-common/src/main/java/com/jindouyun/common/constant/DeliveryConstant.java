package com.jindouyun.common.constant;

/**
 * @className: DeliveryConstant
 * @description: 骑手状态常量
 * @author: ZSZ
 * @date: 2020/8/4 20:53
 */
public class DeliveryConstant {

    //骑手今日状态
    public static final byte TODAY_STATUS_REST = 0;
    public static final byte TODAY_STATUS_WORK = 1;

    //骑手工作状态
    public static final byte WORK_STATUS_NOT_WORKING = 0;
    public static final byte WORK_STATUS_WORKING_UNDELIVERED = 1;
    public static final byte WORK_STATUS_WORKING_DELIVERED = 2;

}
