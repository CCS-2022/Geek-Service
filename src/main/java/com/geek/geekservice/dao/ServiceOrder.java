package com.geek.geekservice.dao;

import com.geek.geekservice.pojo.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;

@Data
@Entity
public class ServiceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;
    @NotNull
    private Long customerId;
    private Long techId;
    @NotNull
    private Long deviceId;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Date creationDate;
    private Date completionDate;
    private Date scheduledDate;
    private BigDecimal estimatedPrice;
    private BigDecimal finalPrice;

    // Transient Fields
    @Transient
    private String clientName;
    @Transient
    private String clientLocation;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(
            name = "sku_so",
            joinColumns = @JoinColumn(name = "service_order_id", referencedColumnName = "serviceId"),
            inverseJoinColumns = @JoinColumn(name = "sku_id", referencedColumnName = "skuId")
    )
    private Set<Sku> skus;

    // Default constructor
    public ServiceOrder(){}
}
