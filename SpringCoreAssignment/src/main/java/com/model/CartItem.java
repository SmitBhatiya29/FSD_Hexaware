package com.model;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class CartItem {
    private int id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private User user;
    public CartItem() {}

    public CartItem(int id, String name, BigDecimal price, int quantity, User user) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", user_id=" + user.getId() +
                '}';
    }
}
