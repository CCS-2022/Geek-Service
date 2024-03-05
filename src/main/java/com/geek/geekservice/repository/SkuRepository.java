package com.geek.geekservice.repository;

import com.geek.geekservice.dao.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SkuRepository extends JpaRepository<Sku, String> {
}
