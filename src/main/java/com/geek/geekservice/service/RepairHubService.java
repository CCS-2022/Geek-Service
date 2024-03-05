package com.geek.geekservice.service;

import com.geek.geekservice.dao.ServiceOrder;
import com.geek.geekservice.dao.Sku;
import com.geek.geekservice.exception.RepairHubException;
import com.geek.geekservice.pojo.Status;
import com.geek.geekservice.pojo.StatusUpdateModel;
import com.geek.geekservice.repository.DeviceRepository;
import com.geek.geekservice.repository.ServiceOrderRepository;
import com.geek.geekservice.repository.SkuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RepairHubService {
    @Autowired
    private ServiceOrderRepository serviceOrderRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private SkuRepository skuRepository;

    public ServiceOrder createServiceOrder(ServiceOrder serviceOrder) {

        // Set up initial details
        serviceOrder.setStatus(Status.CREATED);
        serviceOrder.setCreationDate(new Date(System.currentTimeMillis()));


        // TODO: Fetch correct tax value
        if (serviceOrder.getEstimatedPrice() == null) {
            serviceOrder.setEstimatedPrice(calculatePriceBySku(serviceOrder.getSkus()));
        }

        // Create service order in db
        try {
            serviceOrder = serviceOrderRepository.save(serviceOrder);
        }catch (Exception e){
            log.error("GRH-002: Error occurred while attempting to access geekdb. " + e.getMessage());
            throw new RepairHubException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "GRH-002:create: " + e.getMessage()
            );
        }

        // todo: Send service order to queue
        // Not sure how to do this yet

        return serviceOrder;
    }

    // TODO: Fetch correct tax value
    public BigDecimal calculatePriceBySku(Set<Sku> skus) {
        BigDecimal preTaxPrice = new BigDecimal("0.0");
        for (Sku sku : skus) {
            preTaxPrice = preTaxPrice.add(sku.getPrice());
        }
        return preTaxPrice.add(preTaxPrice.multiply(new BigDecimal("0.07")));
    }

    public ServiceOrder addSkusToOrder(ServiceOrder serviceOrder, Set<Sku> skus) {
        // fetch all skus
        Set<Sku> validSkus = new HashSet<>(skuRepository.findAll());
        int initialSkuCount = serviceOrder.getSkus().size();

        for (Sku sku : skus) {
            if (!validSkus.contains(sku) || serviceOrder.getSkus().contains(sku)) {
                log.info("Found invalid / duplicate sku: " + sku.toString());
            } else {
                serviceOrder.getSkus().add(sku);
            }
        }

        // I don't want to call db if no changes were made.
        if (initialSkuCount < serviceOrder.getSkus().size())
            return serviceOrderRepository.save(serviceOrder);
        else {
            log.info("No changes made to skus for SO id: " + serviceOrder.getServiceId());
            return serviceOrder;
        }
    }

    public ServiceOrder removeSkusFromOrder(ServiceOrder serviceOrder, Set<Sku> skus) {
        int initialSkuCount = serviceOrder.getSkus().size();
        for (Sku sku : skus) {
            if (serviceOrder.getSkus().contains(sku)) {
                serviceOrder.getSkus().remove(sku);
            } else {
                log.info("Sku: " + sku.toString() + " is not attached to service order: " + serviceOrder.getServiceId());
            }
        }

        // I don't want to call db if no changes were made.
        if (initialSkuCount > serviceOrder.getSkus().size())
            return serviceOrderRepository.save(serviceOrder);
        else {
            log.info("No changes made to skus for SO id: " + serviceOrder.getServiceId());
            return serviceOrder;
        }
    }

    public List<ServiceOrder> getRepairHistoryByCustomer(Long customerId) {
        try {
            return serviceOrderRepository.findByCustomerId(customerId);
        }catch (Exception e) {
            log.error("GRH-004: Error occurred while attempting to access geekdb. " + e.getMessage());
            throw new RepairHubException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "GRH-004:repair_history_customer: " + e.getMessage()
            );
        }
    }

    public List<ServiceOrder> getRepairHistoryByTech(Long techId) {
        try {
            return serviceOrderRepository.findByTechId(techId);
        }catch (Exception e) {
            log.error("GRH-004: Error occurred while attempting to access geekdb. " + e.getMessage());
            throw new RepairHubException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "GRH-004:repair_history_tech: " + e.getMessage()
            );
        }
    }

    // TODO: Think about who can make repair status changes.
    public ServiceOrder updateStatus(StatusUpdateModel statusUpdateModel) {
        try{
            // fetch so from db
            Optional<ServiceOrder> serviceOrder = serviceOrderRepository.findById(statusUpdateModel.getServiceId());
            if (serviceOrder.isPresent()) {
                // verify serviceOrder
                if (serviceOrder.get().getStatus() != Status.COMPLETED_SUCCESSFUL &&
                        serviceOrder.get().getStatus() != Status.COMPLETED_UNSUCCESSFUL &&
                        serviceOrder.get().getStatus() != Status.NO_REPAIR &&
                        serviceOrder.get().getStatus() != Status.CANCELED &&
                        serviceOrder.get().getStatus() != statusUpdateModel.getStatus()
                ) {
                    serviceOrder.get().setStatus(statusUpdateModel.getStatus());
                    return serviceOrderRepository.save(serviceOrder.get());
                } else {
                    log.info("Unable to update status");
                    throw new RepairHubException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "GRH-010:update_status: " + "Unable to update status from " + serviceOrder.get().getStatus()
                                    + " to " + statusUpdateModel.getStatus()
                    );
                }
            } else {
                log.info("Unable to update status: Service order not found");
                throw new RepairHubException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "GRH-010:update_status: Service order not found"
                );
            }
        }catch (Exception e){
            log.error("GRH-010: Error occurred while attempting to access geekdb. " + e.getMessage());
            throw new RepairHubException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "GRH-010:update_status: " + e.getMessage()
            );
        }
    }
}