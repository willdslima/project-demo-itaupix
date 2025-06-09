package com.module.pix.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
public class PixKey {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(length = 9, nullable = false)
    private String keyType;
    @Column(length = 77, nullable = false, unique = true)
    private String keyValue;
    @Column(length = 10, nullable = false)
    private String accountType;

    @Column(length = 4, nullable = false)
    private Integer agencyNumber;

    @Column(length = 8, nullable = false)
    private Integer accountNumber;

    @Column(length = 30, nullable = false)
    private String firstName;

    @Column(length = 45)
    private String lastName;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
