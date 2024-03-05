package com.geek.geekservice.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class Sku {
    @Id
    private String skuId;
    private String description;
    private BigDecimal price;
    private int repairDurationMins;

    @ManyToMany(mappedBy = "skus")
    @JsonIgnore
    private Set<ServiceOrder> serviceOrders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sku sku = (Sku) o;
        return repairDurationMins == sku.repairDurationMins && Objects.equals(skuId, sku.skuId) && Objects.equals(description, sku.description) && Objects.equals(price, sku.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skuId, description, price, repairDurationMins);
    }
}
