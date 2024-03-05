package com.geek.geekservice.service;

import com.geek.geekservice.dao.Device;
import com.geek.geekservice.exception.RepairHubException;
import com.geek.geekservice.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public Device registerDevice(Device device) {
        try {
            return deviceRepository.save(device);
        }catch (Exception e) {
            log.error("GRH-001: Error occurred while attempting to register device. " + e.getMessage());
            throw new RepairHubException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "GRH-001:register_device: " + e.getMessage()
            );
        }
    }

    public List<Device> getAllDevicesByCustomerId(Long customerId) {
        try {
            return deviceRepository.findAllById(List.of(customerId));
        }catch (Exception e) {
            log.error("GRH-006: Error occurred while attempting to access geekdb. " + e.getMessage());
            throw new RepairHubException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "GRH-006:get_devices: " + e.getMessage()
            );
        }
    }
}
