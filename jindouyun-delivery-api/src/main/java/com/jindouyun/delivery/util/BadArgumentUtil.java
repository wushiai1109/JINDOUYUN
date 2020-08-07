package com.jindouyun.delivery.util;

import com.jindouyun.common.constant.DeliveryConstant;

/**
 * @className: BadArgumentUtil
 * @description:
 * @author: ZSZ
 * @date: 2020/8/4 20:47
 */
public class BadArgumentUtil {

    public static boolean judgeTodayStatus(short today_status){
        return today_status == DeliveryConstant.TODAY_STATUS_REST
                || today_status == DeliveryConstant.TODAY_STATUS_WORK;
    }


    public static boolean judgeWorkStatus(short work_status){
        return work_status == DeliveryConstant.WORK_STATUS_NOT_WORKING ||
                work_status == DeliveryConstant.WORK_STATUS_WORKING_DELIVERED ||
                work_status == DeliveryConstant.WORK_STATUS_WORKING_UNDELIVERED;
    }

}
