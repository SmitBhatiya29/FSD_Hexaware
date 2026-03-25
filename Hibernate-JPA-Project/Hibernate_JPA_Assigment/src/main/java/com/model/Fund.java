package com.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Fund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(name = "aum_amount" , nullable = false)
    private BigDecimal aumAmount;

    @Column(name = "exepense_ratio" , nullable = false)
    private BigDecimal exepenseRatio;


    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public Fund() {}

    public Fund(long id, String name, BigDecimal aumAmount, BigDecimal exepenseRatio, Instant createdAt, Manager manager) {
        this.id = id;
        this.name = name;
        this.aumAmount = aumAmount;
        this.exepenseRatio = exepenseRatio;
        this.createdAt = createdAt;
        this.manager = manager;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAumAmount() {
        return aumAmount;
    }

    public void setAumAmount(BigDecimal aumAmount) {
        this.aumAmount = aumAmount;
    }

    public BigDecimal getExepenseRatio() {
        return exepenseRatio;
    }

    public void setExepenseRatio(BigDecimal exepenseRatio) {
        this.exepenseRatio = exepenseRatio;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Fund{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", aumAmount=" + aumAmount +
                ", exepenseRatio=" + exepenseRatio +
                ", createdAt=" + createdAt +
                ", manager=" + manager +
                '}';
    }
}
