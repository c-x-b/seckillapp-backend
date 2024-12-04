package com.example.seckillbackend.entity;

import java.util.List;

public class ProductResponse {

    private List<Product> content;       // 当前页的商品列表
    private long totalElements;          // 总记录数
    private int totalPages;              // 总页数
    private int number;                  // 当前页码（从1开始）

    public ProductResponse(List<Product> content, long totalElements, int totalPages, int number) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;
    }

    // Getter and Setter methods
    public List<Product> getContent() {
        return content;
    }

    public void setContent(List<Product> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

