package com.doublegun.travelmgmt.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    private String role = "USER"; // Default role

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

