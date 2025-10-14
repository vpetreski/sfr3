package com.sfr3.mvp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Lease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Tenant tenant;

    private String propertyAddress;
    private BigDecimal monthlyRent;
    private int dueDayOfMonth;
    private boolean autoPayEnabled = true;

    private String paymentMethod; // "CARD" or "ACH"

    @Enumerated(EnumType.STRING)
    private PaymentStatus lastPaymentStatus = PaymentStatus.NONE;
    private LocalDate lastPaymentDate;
    private String lastPaymentError;

    public Lease() {
    }

    public Lease(Tenant tenant, String propertyAddress, BigDecimal monthlyRent, int dueDayOfMonth, boolean autoPayEnabled, String paymentMethod) {
        this.tenant = tenant;
        this.propertyAddress = propertyAddress;
        this.monthlyRent = monthlyRent;
        this.dueDayOfMonth = dueDayOfMonth;
        this.autoPayEnabled = autoPayEnabled;
        this.paymentMethod = paymentMethod;
    }
}
