package com.geek.geekservice.repository;

import com.geek.geekservice.dao.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    List<ServiceOrder> findByCustomerId(Long customerId);
    List<ServiceOrder> findByTechId(Long techId);
}
