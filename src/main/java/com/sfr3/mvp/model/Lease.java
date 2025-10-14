package com.sfr3.mvp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    public Lease() {}
    public Lease(Tenant tenant, String propertyAddress, BigDecimal monthlyRent, int dueDayOfMonth, boolean autoPayEnabled, String paymentMethod) {
        this.tenant = tenant;
        this.propertyAddress = propertyAddress;
        this.monthlyRent = monthlyRent;
        this.dueDayOfMonth = dueDayOfMonth;
        this.autoPayEnabled = autoPayEnabled;
        this.paymentMethod = paymentMethod;
    }

    public Long getId() { return id; }
    public Tenant getTenant() { return tenant; }
    public String getPropertyAddress() { return propertyAddress; }
    public BigDecimal getMonthlyRent() { return monthlyRent; }
    public int getDueDayOfMonth() { return dueDayOfMonth; }
    public boolean isAutoPayEnabled() { return autoPayEnabled; }
    public String getPaymentMethod() { return paymentMethod; }
    public PaymentStatus getLastPaymentStatus() { return lastPaymentStatus; }
    public LocalDate getLastPaymentDate() { return lastPaymentDate; }
    public String getLastPaymentError() { return lastPaymentError; }

    public void setTenant(Tenant tenant) { this.tenant = tenant; }
    public void setPropertyAddress(String propertyAddress) { this.propertyAddress = propertyAddress; }
    public void setMonthlyRent(BigDecimal monthlyRent) { this.monthlyRent = monthlyRent; }
    public void setDueDayOfMonth(int dueDayOfMonth) { this.dueDayOfMonth = dueDayOfMonth; }
    public void setAutoPayEnabled(boolean autoPayEnabled) { this.autoPayEnabled = autoPayEnabled; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setLastPaymentStatus(PaymentStatus lastPaymentStatus) { this.lastPaymentStatus = lastPaymentStatus; }
    public void setLastPaymentDate(LocalDate lastPaymentDate) { this.lastPaymentDate = lastPaymentDate; }
    public void setLastPaymentError(String lastPaymentError) { this.lastPaymentError = lastPaymentError; }
}
