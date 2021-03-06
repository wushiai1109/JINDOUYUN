package com.jindouyun.db.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class JindouyunRegisteBrand implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table jindouyun_registe_brand
     *
     * @mbg.generated
     */
    public static final Boolean IS_DELETED = Deleted.IS_DELETED.value();

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table jindouyun_registe_brand
     *
     * @mbg.generated
     */
    public static final Boolean NOT_DELETED = Deleted.NOT_DELETED.value();

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_registe_brand.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_registe_brand.brandid
     *
     * @mbg.generated
     */
    private Integer brandid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_registe_brand.adderss_id
     *
     * @mbg.generated
     */
    private Integer adderssId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_registe_brand.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_registe_brand.desc
     *
     * @mbg.generated
     */
    private String desc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_registe_brand.pic_url
     *
     * @mbg.generated
     */
    private String picUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_registe_brand.add_time
     *
     * @mbg.generated
     */
    private LocalDateTime addTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_registe_brand.update_time
     *
     * @mbg.generated
     */
    private LocalDateTime updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_registe_brand.deleted
     *
     * @mbg.generated
     */
    private Boolean deleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table jindouyun_registe_brand
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_registe_brand.id
     *
     * @return the value of jindouyun_registe_brand.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_registe_brand.id
     *
     * @param id the value for jindouyun_registe_brand.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_registe_brand.brandid
     *
     * @return the value of jindouyun_registe_brand.brandid
     *
     * @mbg.generated
     */
    public Integer getBrandid() {
        return brandid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_registe_brand.brandid
     *
     * @param brandid the value for jindouyun_registe_brand.brandid
     *
     * @mbg.generated
     */
    public void setBrandid(Integer brandid) {
        this.brandid = brandid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_registe_brand.adderss_id
     *
     * @return the value of jindouyun_registe_brand.adderss_id
     *
     * @mbg.generated
     */
    public Integer getAdderssId() {
        return adderssId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_registe_brand.adderss_id
     *
     * @param adderssId the value for jindouyun_registe_brand.adderss_id
     *
     * @mbg.generated
     */
    public void setAdderssId(Integer adderssId) {
        this.adderssId = adderssId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_registe_brand.name
     *
     * @return the value of jindouyun_registe_brand.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_registe_brand.name
     *
     * @param name the value for jindouyun_registe_brand.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_registe_brand.desc
     *
     * @return the value of jindouyun_registe_brand.desc
     *
     * @mbg.generated
     */
    public String getDesc() {
        return desc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_registe_brand.desc
     *
     * @param desc the value for jindouyun_registe_brand.desc
     *
     * @mbg.generated
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_registe_brand.pic_url
     *
     * @return the value of jindouyun_registe_brand.pic_url
     *
     * @mbg.generated
     */
    public String getPicUrl() {
        return picUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_registe_brand.pic_url
     *
     * @param picUrl the value for jindouyun_registe_brand.pic_url
     *
     * @mbg.generated
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_registe_brand.add_time
     *
     * @return the value of jindouyun_registe_brand.add_time
     *
     * @mbg.generated
     */
    public LocalDateTime getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_registe_brand.add_time
     *
     * @param addTime the value for jindouyun_registe_brand.add_time
     *
     * @mbg.generated
     */
    public void setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_registe_brand.update_time
     *
     * @return the value of jindouyun_registe_brand.update_time
     *
     * @mbg.generated
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_registe_brand.update_time
     *
     * @param updateTime the value for jindouyun_registe_brand.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_registe_brand
     *
     * @mbg.generated
     */
    public void andLogicalDeleted(boolean deleted) {
        setDeleted(deleted ? Deleted.IS_DELETED.value() : Deleted.NOT_DELETED.value());
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_registe_brand.deleted
     *
     * @return the value of jindouyun_registe_brand.deleted
     *
     * @mbg.generated
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_registe_brand.deleted
     *
     * @param deleted the value for jindouyun_registe_brand.deleted
     *
     * @mbg.generated
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_registe_brand
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
        sb.append(", brandid=").append(brandid);
        sb.append(", adderssId=").append(adderssId);
        sb.append(", name=").append(name);
        sb.append(", desc=").append(desc);
        sb.append(", picUrl=").append(picUrl);
        sb.append(", addTime=").append(addTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_registe_brand
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
        JindouyunRegisteBrand other = (JindouyunRegisteBrand) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBrandid() == null ? other.getBrandid() == null : this.getBrandid().equals(other.getBrandid()))
            && (this.getAdderssId() == null ? other.getAdderssId() == null : this.getAdderssId().equals(other.getAdderssId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getDesc() == null ? other.getDesc() == null : this.getDesc().equals(other.getDesc()))
            && (this.getPicUrl() == null ? other.getPicUrl() == null : this.getPicUrl().equals(other.getPicUrl()))
            && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jindouyun_registe_brand
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBrandid() == null) ? 0 : getBrandid().hashCode());
        result = prime * result + ((getAdderssId() == null) ? 0 : getAdderssId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDesc() == null) ? 0 : getDesc().hashCode());
        result = prime * result + ((getPicUrl() == null) ? 0 : getPicUrl().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table jindouyun_registe_brand
     *
     * @mbg.generated
     */
    public enum Deleted {
        NOT_DELETED(new Boolean("0"), "未删除"),
        IS_DELETED(new Boolean("1"), "已删除");

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        private final Boolean value;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        private final String name;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        Deleted(Boolean value, String name) {
            this.value = value;
            this.name = name;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        public Boolean getValue() {
            return this.value;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        public Boolean value() {
            return this.value;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        public String getName() {
            return this.name;
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table jindouyun_registe_brand
     *
     * @mbg.generated
     */
    public enum Column {
        id("id", "id", "INTEGER", false),
        brandid("brandid", "brandid", "INTEGER", false),
        adderssId("adderss_id", "adderssId", "INTEGER", false),
        name("name", "name", "VARCHAR", true),
        desc("desc", "desc", "VARCHAR", true),
        picUrl("pic_url", "picUrl", "VARCHAR", false),
        addTime("add_time", "addTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        deleted("deleted", "deleted", "BIT", false);

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        private static final String BEGINNING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        private static final String ENDING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        private final String column;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        private final boolean isColumnNameDelimited;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        private final String javaProperty;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        private final String jdbcType;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        public String getJavaProperty() {
            return this.javaProperty;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        public String getJdbcType() {
            return this.jdbcType;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_registe_brand
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
         * This method corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table jindouyun_registe_brand
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
         * This method corresponds to the database table jindouyun_registe_brand
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
         * This method corresponds to the database table jindouyun_registe_brand
         *
         * @mbg.generated
         */
        public String getAliasedEscapedColumnName() {
            return this.getEscapedColumnName();
        }
    }
}