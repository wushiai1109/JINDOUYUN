package com.jindouyun.db.domain;

import com.jindouyun.db.domain.JindouyunExpressOrder;
import com.jindouyun.db.domain.JindouyunOrderSplit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @className: MergeInfo
 * @description:
 * @author: ZSZ
 * @date: 2020/8/11 0:38
 */
public class MergeExpressInfo {

    private int id;
    private String orderSn;
    private String message;
    private BigDecimal allPrice;
    private int num;
    private Byte type;
    private Byte release;
    private Byte status;
    private LocalDateTime releaseTime;
    private LocalDateTime receiveTime;
    private LocalDateTime pickupTime;
    private LocalDateTime arriveTime;
    private JindouyunGrabOrder grabOrder;
    private List<JindouyunExpressOrder> splitOrder;

    public JindouyunGrabOrder getGrabOrder() {
        return grabOrder;
    }

    public void setGrabOrder(JindouyunGrabOrder grabOrder) {
        this.grabOrder = grabOrder;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(BigDecimal allPrice) {
        this.allPrice = allPrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Byte getRelease() {
        return release;
    }

    public void setRelease(Byte release) {
        this.release = release;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public LocalDateTime getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(LocalDateTime releaseTime) {
        this.releaseTime = releaseTime;
    }

    public LocalDateTime getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(LocalDateTime receiveTime) {
        this.receiveTime = receiveTime;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(LocalDateTime arriveTime) {
        this.arriveTime = arriveTime;
    }

    public List<JindouyunExpressOrder> getSplitOrder() {
        return splitOrder;
    }

    public void setSplitOrder(List<JindouyunExpressOrder> splitOrder) {
        this.splitOrder = splitOrder;
    }


}
