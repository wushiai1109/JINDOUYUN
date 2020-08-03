package com.jindouyun.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class JindouyunMergeOrder implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table jindouyun_merge_order
     *
     * @mbg.generated
     */
    public static final Boolean IS_DELETED = Deleted.IS_DELETED.value();

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table jindouyun_merge_order
     *
     * @mbg.generated
     */
    public static final Boolean NOT_DELETED = Deleted.NOT_DELETED.value();

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.admin_id
     *
     * @mbg.generated
     */
    private Integer adminId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.order_sn
     *
     * @mbg.generated
     */
    private String orderSn;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.message
     *
     * @mbg.generated
     */
    private String message;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.order_goods_list
     *
     * @mbg.generated
     */
    private String orderGoodsList;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.num
     *
     * @mbg.generated
     */
    private Short num;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.all_price
     *
     * @mbg.generated
     */
    private BigDecimal allPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.type
     *
     * @mbg.generated
     */
    private Byte type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.release
     *
     * @mbg.generated
     */
    private Byte release;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.status
     *
     * @mbg.generated
     */
    private Byte status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.add_time
     *
     * @mbg.generated
     */
    private LocalDateTime addTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.update_time
     *
     * @mbg.generated
     */
    private LocalDateTime updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_merge_order.deleted
     *
     * @mbg.generated
     */
    private Boolean deleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table jindouyun_merge_order
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.id
     *
     * @return the value of jindouyun_merge_order.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.id
     *
     * @param id the value for jindouyun_merge_order.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.admin_id
     *
     * @return the value of jindouyun_merge_order.admin_id
     *
     * @mbg.generated
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.admin_id
     *
     * @param adminId the value for jindouyun_merge_order.admin_id
     *
     * @mbg.generated
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.order_sn
     *
     * @return the value of jindouyun_merge_order.order_sn
     *
     * @mbg.generated
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.order_sn
     *
     * @param orderSn the value for jindouyun_merge_order.order_sn
     *
     * @mbg.generated
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.message
     *
     * @return the value of jindouyun_merge_order.message
     *
     * @mbg.generated
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.message
     *
     * @param message the value for jindouyun_merge_order.message
     *
     * @mbg.generated
     */
    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.order_goods_list
     *
     * @return the value of jindouyun_merge_order.order_goods_list
     *
     * @mbg.generated
     */
    public String getOrderGoodsList() {
        return orderGoodsList;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.order_goods_list
     *
     * @param orderGoodsList the value for jindouyun_merge_order.order_goods_list
     *
     * @mbg.generated
     */
    public void setOrderGoodsList(String orderGoodsList) {
        this.orderGoodsList = orderGoodsList == null ? null : orderGoodsList.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.num
     *
     * @return the value of jindouyun_merge_order.num
     *
     * @mbg.generated
     */
    public Short getNum() {
        return num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.num
     *
     * @param num the value for jindouyun_merge_order.num
     *
     * @mbg.generated
     */
    public void setNum(Short num) {
        this.num = num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.all_price
     *
     * @return the value of jindouyun_merge_order.all_price
     *
     * @mbg.generated
     */
    public BigDecimal getAllPrice() {
        return allPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.all_price
     *
     * @param allPrice the value for jindouyun_merge_order.all_price
     *
     * @mbg.generated
     */
    public void setAllPrice(BigDecimal allPrice) {
        this.allPrice = allPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.type
     *
     * @return the value of jindouyun_merge_order.type
     *
     * @mbg.generated
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.type
     *
     * @param type the value for jindouyun_merge_order.type
     *
     * @mbg.generated
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.release
     *
     * @return the value of jindouyun_merge_order.release
     *
     * @mbg.generated
     */
    public Byte getRelease() {
        return release;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.release
     *
     * @param release the value for jindouyun_merge_order.release
     *
     * @mbg.generated
     */
    public void setRelease(Byte release) {
        this.release = release;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.status
     *
     * @return the value of jindouyun_merge_order.status
     *
     * @mbg.generated
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.status
     *
     * @param status the value for jindouyun_merge_order.status
     *
     * @mbg.generated
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.add_time
     *
     * @return the value of jindouyun_merge_order.add_time
     *
     * @mbg.generated
     */
    public LocalDateTime getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.add_time
     *
     * @param addTime the value for jindouyun_merge_order.add_time
     *
     * @mbg.generated
     */
    public void setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.update_time
     *
     * @return the value of jindouyun_merge_order.update_time
     *
     * @mbg.generated
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.update_time
     *
     * @param updateTime the value for jindouyun_merge_order.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_merge_order
     *
     * @mbg.generated
     */
    public void andLogicalDeleted(boolean deleted) {
        setDeleted(deleted ? Deleted.IS_DELETED.value() : Deleted.NOT_DELETED.value());
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_merge_order.deleted
     *
     * @return the value of jindouyun_merge_order.deleted
     *
     * @mbg.generated
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_merge_order.deleted
     *
     * @param deleted the value for jindouyun_merge_order.deleted
     *
     * @mbg.generated
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_merge_order
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", IS_DELETED=").append(IS_DELETED);
        sb.append(", NOT_DELETED=").append(NOT_DELETED);
        sb.append(", id=").append(id);
        sb.append(", adminId=").append(adminId);
        sb.append(", orderSn=").append(orderSn);
        sb.append(", message=").append(message);
        sb.append(", orderGoodsList=").append(orderGoodsList);
        sb.append(", num=").append(num);
        sb.append(", allPrice=").append(allPrice);
        sb.append(", type=").append(type);
        sb.append(", release=").append(release);
        sb.append(", status=").append(status);
        sb.append(", addTime=").append(addTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_merge_order
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        JindouyunMergeOrder other = (JindouyunMergeOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAdminId() == null ? other.getAdminId() == null : this.getAdminId().equals(other.getAdminId()))
            && (this.getOrderSn() == null ? other.getOrderSn() == null : this.getOrderSn().equals(other.getOrderSn()))
            && (this.getMessage() == null ? other.getMessage() == null : this.getMessage().equals(other.getMessage()))
            && (this.getOrderGoodsList() == null ? other.getOrderGoodsList() == null : this.getOrderGoodsList().equals(other.getOrderGoodsList()))
            && (this.getNum() == null ? other.getNum() == null : this.getNum().equals(other.getNum()))
            && (this.getAllPrice() == null ? other.getAllPrice() == null : this.getAllPrice().equals(other.getAllPrice()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getRelease() == null ? other.getRelease() == null : this.getRelease().equals(other.getRelease()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_merge_order
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAdminId() == null) ? 0 : getAdminId().hashCode());
        result = prime * result + ((getOrderSn() == null) ? 0 : getOrderSn().hashCode());
        result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
        result = prime * result + ((getOrderGoodsList() == null) ? 0 : getOrderGoodsList().hashCode());
        result = prime * result + ((getNum() == null) ? 0 : getNum().hashCode());
        result = prime * result + ((getAllPrice() == null) ? 0 : getAllPrice().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getRelease() == null) ? 0 : getRelease().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table jindouyun_merge_order
     *
     * @mbg.generated
     */
    public enum Deleted {
        NOT_DELETED(new Boolean("0"), "未删除"),
        IS_DELETED(new Boolean("1"), "已删除");

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        private final Boolean value;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        private final String name;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        Deleted(Boolean value, String name) {
            this.value = value;
            this.name = name;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public Boolean getValue() {
            return this.value;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public Boolean value() {
            return this.value;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public String getName() {
            return this.name;
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table jindouyun_merge_order
     *
     * @mbg.generated
     */
    public enum Column {
        id("id", "id", "INTEGER", false),
        adminId("admin_id", "adminId", "INTEGER", false),
        orderSn("order_sn", "orderSn", "VARCHAR", false),
        message("message", "message", "VARCHAR", false),
        orderGoodsList("order_goods_list", "orderGoodsList", "VARCHAR", false),
        num("num", "num", "SMALLINT", false),
        allPrice("all_price", "allPrice", "DECIMAL", false),
        type("type", "type", "TINYINT", true),
        release("release", "release", "TINYINT", true),
        status("status", "status", "TINYINT", true),
        addTime("add_time", "addTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        deleted("deleted", "deleted", "BIT", false);

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        private static final String BEGINNING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        private static final String ENDING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        private final String column;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        private final boolean isColumnNameDelimited;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        private final String javaProperty;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        private final String jdbcType;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public String getJavaProperty() {
            return this.javaProperty;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public String getJdbcType() {
            return this.jdbcType;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        Column(String column, String javaProperty, String jdbcType, boolean isColumnNameDelimited) {
            this.column = column;
            this.javaProperty = javaProperty;
            this.jdbcType = jdbcType;
            this.isColumnNameDelimited = isColumnNameDelimited;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public static Column[] excludes(Column ... excludes) {
            ArrayList<Column> columns = new ArrayList<>(Arrays.asList(Column.values()));
            if (excludes != null && excludes.length > 0) {
                columns.removeAll(new ArrayList<>(Arrays.asList(excludes)));
            }
            return columns.toArray(new Column[]{});
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public String getEscapedColumnName() {
            if (this.isColumnNameDelimited) {
                return new StringBuilder().append(BEGINNING_DELIMITER).append(this.column).append(ENDING_DELIMITER).toString();
            } else {
                return this.column;
            }
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_merge_order
         *
         * @mbg.generated
         */
        public String getAliasedEscapedColumnName() {
            return this.getEscapedColumnName();
        }
    }
}