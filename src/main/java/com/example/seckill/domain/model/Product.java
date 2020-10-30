package com.example.seckill.domain.model;

import java.io.Serializable;

public class Product implements Serializable {
    private Integer id;

    private String title;

    private Long price;

    private Integer number;

    private String flag;

    private String status;

    private Integer seckillPrice;

    private Integer seckillNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(Integer seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public Integer getSeckillNumber() {
        return seckillNumber;
    }

    public void setSeckillNumber(Integer seckillNumber) {
        this.seckillNumber = seckillNumber;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", number=" + number +
                ", flag='" + flag + '\'' +
                ", status='" + status + '\'' +
                ", seckillPrice=" + seckillPrice +
                ", seckillNumber=" + seckillNumber +
                '}';
    }
}
