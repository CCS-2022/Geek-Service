package com.geek.geekservice.controller;

import com.geek.geekservice.dao.Device;
import com.geek.geekservice.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevicesByCustomerId(@RequestParam Long customerId) {
        return new ResponseEntity<>(deviceService.getAllDevicesByCustomerId(customerId), HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<Device> registerDevice(@RequestBody Device device) {
        return new ResponseEntity<>(deviceService.registerDevice(device), HttpStatus.OK);
    }
}
