package com.example.seckillbackend.dto;

public class OrderRequest {

    private Long goodsId; // 商品 ID
    private Integer orderAmount; // 下单的数量

    // Getters and Setters
    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }
}
