package com.jindouyun.core.system;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统设置
 */
public class SystemConfig {
    // 小程序相关配置
    public final static String Jindouyun_WX_INDEX_NEW = "Jindouyun_wx_index_new";
    public final static String Jindouyun_WX_INDEX_HOT = "Jindouyun_wx_index_hot";
    public final static String Jindouyun_WX_INDEX_BRAND = "Jindouyun_wx_index_brand";
    public final static String Jindouyun_WX_INDEX_TOPIC = "Jindouyun_wx_index_topic";
    public final static String Jindouyun_WX_INDEX_CATLOG_LIST = "Jindouyun_wx_catlog_list";
    public final static String Jindouyun_WX_INDEX_CATLOG_GOODS = "Jindouyun_wx_catlog_goods";
    public final static String Jindouyun_WX_SHARE = "Jindouyun_wx_share";
    // 运费相关配置
    public final static String Jindouyun_EXPRESS_FREIGHT_VALUE = "Jindouyun_express_freight_value";
    public final static String Jindouyun_EXPRESS_FREIGHT_MIN = "Jindouyun_express_freight_min";
    // 订单相关配置
    public final static String Jindouyun_ORDER_UNPAID = "Jindouyun_order_unpaid";
    public final static String Jindouyun_ORDER_UNCONFIRM = "Jindouyun_order_unconfirm";
    public final static String Jindouyun_ORDER_COMMENT = "Jindouyun_order_comment";
    // 商场相关配置
    public final static String Jindouyun_MALL_NAME = "Jindouyun_mall_name";
    public final static String Jindouyun_MALL_ADDRESS = "Jindouyun_mall_address";
    public final static String Jindouyun_MALL_PHONE = "Jindouyun_mall_phone";
    public final static String Jindouyun_MALL_QQ = "Jindouyun_mall_qq";
    public final static String Jindouyun_MALL_LONGITUDE = "Jindouyun_mall_longitude";
    public final static String Jindouyun_MALL_Latitude = "Jindouyun_mall_latitude";
    //快递包裹的费用
    public final static String Jindouyun_FALSE_ISWEIGHT = "Jindouyun_FALSE_ISWEIGHT";
    public final static String Jindouyun_TRUE_ISWEIGHT = "Jindouyun_TRUE_ISWEIGHT";

    //所有的配置均保存在该 HashMap 中
    private static Map<String, String> SYSTEM_CONFIGS = new HashMap<>();

    private static String getConfig(String keyName) {
        return SYSTEM_CONFIGS.get(keyName);
    }

    private static Integer getConfigInt(String keyName) {
        return Integer.parseInt(SYSTEM_CONFIGS.get(keyName));
    }

    private static Boolean getConfigBoolean(String keyName) {
        return Boolean.valueOf(SYSTEM_CONFIGS.get(keyName));
    }

    private static BigDecimal getConfigBigDec(String keyName) {
        return new BigDecimal(SYSTEM_CONFIGS.get(keyName));
    }

    public static Integer getNewLimit() {
        return getConfigInt(Jindouyun_WX_INDEX_NEW);
    }

    public static Integer getHotLimit() {
        return getConfigInt(Jindouyun_WX_INDEX_HOT);
    }

    public static Integer getBrandLimit() {
        return getConfigInt(Jindouyun_WX_INDEX_BRAND);
    }

    public static Integer getTopicLimit() {
        return getConfigInt(Jindouyun_WX_INDEX_TOPIC);
    }

    public static Integer getCatlogListLimit() {
        return getConfigInt(Jindouyun_WX_INDEX_CATLOG_LIST);
    }

    public static Integer getCatlogMoreLimit() {
        return getConfigInt(Jindouyun_WX_INDEX_CATLOG_GOODS);
    }

    public static boolean isAutoCreateShareImage() {
        return getConfigBoolean(Jindouyun_WX_SHARE);
    }

    public static BigDecimal getFreight() {
        return getConfigBigDec(Jindouyun_EXPRESS_FREIGHT_VALUE);
    }

    public static BigDecimal getFreightLimit() {
        return getConfigBigDec(Jindouyun_EXPRESS_FREIGHT_MIN);
    }

    public static Integer getOrderUnpaid() {
        return getConfigInt(Jindouyun_ORDER_UNPAID);
    }

    public static Integer getOrderUnconfirm() {
        return getConfigInt(Jindouyun_ORDER_UNCONFIRM);
    }

    public static Integer getOrderComment() {
        return getConfigInt(Jindouyun_ORDER_COMMENT);
    }

    public static String getMallName() {
        return getConfig(Jindouyun_MALL_NAME);
    }

    public static String getMallAddress() {
        return getConfig(Jindouyun_MALL_ADDRESS);
    }

    public static String getMallPhone() {
        return getConfig(Jindouyun_MALL_PHONE);
    }

    public static String getMallQQ() {
        return getConfig(Jindouyun_MALL_QQ);
    }

    public static String getMallLongitude() {
        return getConfig(Jindouyun_MALL_LONGITUDE);
    }

    public static String getMallLatitude() {
        return getConfig(Jindouyun_MALL_Latitude);
    }

    public static void setConfigs(Map<String, String> configs) {
        SYSTEM_CONFIGS = configs;
    }

    public static void updateConfigs(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            SYSTEM_CONFIGS.put(entry.getKey(), entry.getValue());
        }
    }

    public static BigDecimal getFalseIsweight() {
        return getConfigBigDec(Jindouyun_FALSE_ISWEIGHT);
    }

    public static BigDecimal getTrueIsweight() {
        return getConfigBigDec(Jindouyun_TRUE_ISWEIGHT);
    }
}