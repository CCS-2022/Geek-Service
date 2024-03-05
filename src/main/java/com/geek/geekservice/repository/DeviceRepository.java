package com.geek.geekservice.repository;

import com.geek.geekservice.dao.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query(value = "SELECT d FROM Device d " +
            "WHERE LOWER(d.deviceType) = LOWER(:deviceType) " +
            "AND LOWER(d.deviceDescription) = LOWER(:deviceDescription)")
    Device findByDeviceTypeAndDeviceDescription(@Param("deviceType") String deviceType,
                                                @Param("deviceDescription") String deviceDescription);
}
