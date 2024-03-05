package com.geek.geekservice.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NonNull;
@Data
@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;
    @NonNull
    private Long customerId;
    @NonNull
    private String deviceType;
    private String serialNumber;
    private String modelNumber;
    private String alias;
    private String deviceDescription;
}
