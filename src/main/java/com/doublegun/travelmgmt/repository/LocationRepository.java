package com.doublegun.travelmgmt.repository;

import com.doublegun.travelmgmt.model.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO location (location_name, location_address, cost, details, insert_ts, update_ts) " +
            "VALUES (:locationName, :locationAddress, :cost, CAST(:details AS jsonb), :insertTs, :updateTs)", nativeQuery = true)
    int insertLocation(@Param("locationName") String locationName,
                       @Param("locationAddress") String locationAddress,
                       @Param("cost") Double cost,
                       @Param("details") String details, @Param("insertTs") Long insertTs, @Param("updateTs") Long updateTs);

    @Query("SELECT l FROM Location l")
    List<Location> getLocations();
}
