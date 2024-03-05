package com.geek.geekservice.controller;

import com.geek.geekservice.dao.Sku;
import com.geek.geekservice.exception.RepairHubException;
import com.geek.geekservice.repository.SkuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sku")
public class SkuController {
    @Autowired
    private SkuRepository skuRepository;
    @GetMapping
        public ResponseEntity<List<Sku>> getSkus(){
        try {
            return new ResponseEntity<>(skuRepository.findAll(), HttpStatus.OK);
        } catch (Exception e){
            log.error("GRH-008:get_skus: Error occurred while attempting to access geekdb.");
            throw new RepairHubException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "GRH-008:get_skus: Error occurred while attempting to access geekdb."
            );
        }
    }
}
