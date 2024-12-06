package com.doublegun.travelmgmt.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "location")
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_name", nullable = false, unique = true, columnDefinition = "TEXT")
    private String locationName;

    @Column(name = "location_address", nullable = false, columnDefinition = "TEXT")
    private String locationAddress;

    @Column(name = "cost", nullable = false)
    private Double cost;

    @Column(name = "details", columnDefinition = "jsonb")
    private String details;

    @Column(name = "insert_ts", nullable = false, updatable = false)
    private Long insertTs;

    @Column(name = "update_ts", nullable = false)
    private Long updateTs;

    @PrePersist
    protected void onCreate() {
        long currentTime = System.currentTimeMillis();
        this.insertTs = currentTime;
        this.updateTs = currentTime;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTs = System.currentTimeMillis();
    }

}
