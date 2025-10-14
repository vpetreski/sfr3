package com.sfr3.mvp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Lease lease;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String providerReference;
    private String errorMessage;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Payment() {
    }

    public Payment(Lease lease, BigDecimal amount, PaymentStatus status) {
        this.lease = lease;
        this.amount = amount;
        this.status = status;
    }

    @PreUpdate
    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
