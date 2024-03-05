package com.geek.geekservice.controller;

import com.geek.geekservice.dao.ServiceOrder;
import com.geek.geekservice.dao.Sku;
import com.geek.geekservice.exception.RepairHubException;
import com.geek.geekservice.pojo.StatusUpdateModel;
import com.geek.geekservice.repository.ServiceOrderRepository;
import com.geek.geekservice.service.RepairHubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/repair")
public class RepairHubController {
    @Autowired
    private RepairHubService repairHubService;
    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @PostMapping("/create")
    public ResponseEntity<ServiceOrder> createServiceOrder(@RequestBody ServiceOrder serviceOrder) {
        return new ResponseEntity<>(repairHubService.createServiceOrder(serviceOrder), HttpStatus.CREATED);
    }

    @PostMapping("/update/add/skus")
    public ResponseEntity<ServiceOrder> updateServiceOrderAddSkus(@RequestParam Long serviceId,
                                                           @RequestBody Set<Sku> skus) {
        try {
            Optional<ServiceOrder> serviceOrder = serviceOrderRepository.findById(serviceId);
            if (serviceOrder.isEmpty()) throw new RepairHubException();
            return new ResponseEntity<>(repairHubService.addSkusToOrder(serviceOrder.get(), skus), HttpStatus.OK);
        }catch (RepairHubException e) {
            log.error("GRH-005:update_skus: Service order not found.");
            throw new RepairHubException(
                    HttpStatus.NOT_FOUND,
                    "GRH-005:update_skus: Service order not found."
            );
        } catch (Exception e) {
            log.error("GRH-005: Error occurred while attempting to access geekdb. " + e.getMessage());
            throw new RepairHubException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "GRH-005:update_skus: " + e.getMessage()
            );
        }
    }

    @PostMapping("/update/remove/skus")
    public ResponseEntity<ServiceOrder> updateServiceOrderRemoveSkus(@RequestParam Long serviceId,
                                                               @RequestBody Set<Sku> skus) {
        try {
            Optional<ServiceOrder> serviceOrder = serviceOrderRepository.findById(serviceId);
            if (serviceOrder.isEmpty()) throw new RepairHubException();
            return new ResponseEntity<>(repairHubService.removeSkusFromOrder(serviceOrder.get(), skus), HttpStatus.OK);
        }catch (RepairHubException e) {
            log.error("GRH-009:update_skus: Service order not found.");
            throw new RepairHubException(
                    HttpStatus.NOT_FOUND,
                    "GRH-009:update_skus: Service order not found."
            );
        } catch (Exception e) {
            log.error("GRH-009: Error occurred while attempting to access geekdb. " + e.getMessage());
            throw new RepairHubException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "GRH-009:update_skus: " + e.getMessage()
            );
        }
    }

    @PostMapping("/update/status")
    public ResponseEntity<ServiceOrder> updateServiceOrderStatus(@RequestBody StatusUpdateModel statusUpdateModel) {
        return new ResponseEntity<>(repairHubService.updateStatus(statusUpdateModel), HttpStatus.OK);
    }

    @PostMapping("/price")
    public ResponseEntity<BigDecimal> calculatePriceBySkus(@RequestBody Set<Sku> skus) {
        return new ResponseEntity<>(repairHubService.calculatePriceBySku(skus), HttpStatus.OK);
    }

    @GetMapping("/history/customer")
    public ResponseEntity<List<ServiceOrder>> getRepairHistoryCustomer(@RequestParam Long customerId) {
        if (customerId != null) {
            return new ResponseEntity<>(repairHubService.getRepairHistoryByCustomer(customerId), HttpStatus.OK);
        }else {
            log.error("GRH-003:repair_history_customer: customerId was null.");
            throw new RepairHubException(
                    HttpStatus.BAD_REQUEST,
                    "GRH-003:repair_history_customer: customerId was null."
            );
        }
    }

    @GetMapping("/history/tech")
    public ResponseEntity<List<ServiceOrder>> getRepairHistoryTech(@RequestParam Long techId) {
        if (techId != null) {
            return new ResponseEntity<>(repairHubService.getRepairHistoryByTech(techId), HttpStatus.OK);
        }else {
            log.error("GRH-003:repair_history: techId was null.");
            throw new RepairHubException(
                    HttpStatus.BAD_REQUEST,
                    "GRH-003:repair_history: techId was null."
            );
        }
    }
}
