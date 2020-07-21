package com.jindouyun.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class JindouyunBrand implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_brand.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_brand.adderss_id
     *
     * @mbg.generated
     */
    private Integer adderssId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_brand.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_brand.desc
     *
     * @mbg.generated
     */
    private String desc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_brand.pic_url
     *
     * @mbg.generated
     */
    private String picUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_brand.sort_order
     *
     * @mbg.generated
     */
    private Byte sortOrder;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_brand.floor_price
     *
     * @mbg.generated
     */
    private BigDecimal floorPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_brand.add_time
     *
     * @mbg.generated
     */
    private Date addTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_brand.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jindouyun_brand.deleted
     *
     * @mbg.generated
     */
    private Boolean deleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table jindouyun_brand
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_brand.id
     *
     * @return the value of jindouyun_brand.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_brand.id
     *
     * @param id the value for jindouyun_brand.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_brand.adderss_id
     *
     * @return the value of jindouyun_brand.adderss_id
     *
     * @mbg.generated
     */
    public Integer getAdderssId() {
        return adderssId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_brand.adderss_id
     *
     * @param adderssId the value for jindouyun_brand.adderss_id
     *
     * @mbg.generated
     */
    public void setAdderssId(Integer adderssId) {
        this.adderssId = adderssId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_brand.name
     *
     * @return the value of jindouyun_brand.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_brand.name
     *
     * @param name the value for jindouyun_brand.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_brand.desc
     *
     * @return the value of jindouyun_brand.desc
     *
     * @mbg.generated
     */
    public String getDesc() {
        return desc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_brand.desc
     *
     * @param desc the value for jindouyun_brand.desc
     *
     * @mbg.generated
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_brand.pic_url
     *
     * @return the value of jindouyun_brand.pic_url
     *
     * @mbg.generated
     */
    public String getPicUrl() {
        return picUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_brand.pic_url
     *
     * @param picUrl the value for jindouyun_brand.pic_url
     *
     * @mbg.generated
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_brand.sort_order
     *
     * @return the value of jindouyun_brand.sort_order
     *
     * @mbg.generated
     */
    public Byte getSortOrder() {
        return sortOrder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_brand.sort_order
     *
     * @param sortOrder the value for jindouyun_brand.sort_order
     *
     * @mbg.generated
     */
    public void setSortOrder(Byte sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_brand.floor_price
     *
     * @return the value of jindouyun_brand.floor_price
     *
     * @mbg.generated
     */
    public BigDecimal getFloorPrice() {
        return floorPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_brand.floor_price
     *
     * @param floorPrice the value for jindouyun_brand.floor_price
     *
     * @mbg.generated
     */
    public void setFloorPrice(BigDecimal floorPrice) {
        this.floorPrice = floorPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_brand.add_time
     *
     * @return the value of jindouyun_brand.add_time
     *
     * @mbg.generated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_brand.add_time
     *
     * @param addTime the value for jindouyun_brand.add_time
     *
     * @mbg.generated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_brand.update_time
     *
     * @return the value of jindouyun_brand.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_brand.update_time
     *
     * @param updateTime the value for jindouyun_brand.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jindouyun_brand.deleted
     *
     * @return the value of jindouyun_brand.deleted
     *
     * @mbg.generated
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jindouyun_brand.deleted
     *
     * @param deleted the value for jindouyun_brand.deleted
     *
     * @mbg.generated
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}