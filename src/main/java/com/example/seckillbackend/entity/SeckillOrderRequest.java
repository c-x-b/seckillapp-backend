package com.example.seckillbackend.entity;

import java.util.Date;

public class SeckillOrderRequest {

    private Long seckillId; // 秒杀 ID
    private Integer orderAmount; // 下单的数量
    private Date orderTime; // 下单时间

    public SeckillOrderRequest(Long seckillId, Integer orderAmount, Date orderTime) {
        this.seckillId = seckillId;
        this.orderAmount = orderAmount;
        this.orderTime = orderTime;
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
}
