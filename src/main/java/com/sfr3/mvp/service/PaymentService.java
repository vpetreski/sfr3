package com.sfr3.mvp.service;

import com.sfr3.mvp.integration.PaymentProvider;
import com.sfr3.mvp.integration.PaymentResult;
import com.sfr3.mvp.model.*;
import com.sfr3.mvp.repository.LeaseRepository;
import com.sfr3.mvp.repository.PaymentRepository;
import com.sfr3.mvp.repository.TenantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    private final LeaseRepository leaseRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentProvider paymentProvider;

    public PaymentService(LeaseRepository leaseRepository, PaymentRepository paymentRepository, PaymentProvider paymentProvider) {
        this.leaseRepository = leaseRepository;
        this.paymentRepository = paymentRepository;
        this.paymentProvider = paymentProvider;
    }

    @Transactional(readOnly = true)
    public List<Lease> listLeases() {
        return leaseRepository.findAll();
    }

    @Transactional
    public Lease payLease(Long leaseId) {
        Lease lease = leaseRepository.findById(leaseId).orElseThrow();
        LocalDate today = LocalDate.now();
        if (lease.getLastPaymentStatus() == PaymentStatus.SUCCESS &&
            lease.getLastPaymentDate() != null &&
            lease.getLastPaymentDate().getMonth().equals(today.getMonth()) &&
            lease.getLastPaymentDate().getYear() == today.getYear()) {
            return lease;
        }
        if (lease.getLastPaymentStatus() == PaymentStatus.PENDING) {
            return lease;
        }

        BigDecimal amount = lease.getMonthlyRent();
        Payment payment = new Payment(lease, amount, PaymentStatus.PENDING);
        paymentRepository.save(payment);

        PaymentResult result = paymentProvider.charge(lease, amount);

        switch (result.getStatus()) {
            case SUCCESS -> {
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setProviderReference(result.getProviderRef());
                lease.setLastPaymentStatus(PaymentStatus.SUCCESS);
                lease.setLastPaymentDate(today);
                lease.setLastPaymentError(null);
            }
            case FAILED -> {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setErrorMessage(result.getMessage());
                lease.setLastPaymentStatus(PaymentStatus.FAILED);
                lease.setLastPaymentDate(today);
                lease.setLastPaymentError(result.getMessage());
            }
            case PENDING -> {
                lease.setLastPaymentStatus(PaymentStatus.PENDING);
                lease.setLastPaymentDate(today);
                payment.setStatus(PaymentStatus.PENDING);
                payment.setProviderReference(result.getProviderRef());
                Long pId = payment.getId();
                Long lId = lease.getId();
                Executors.newSingleThreadScheduledExecutor().schedule(() -> finalizePendingSuccess(pId, lId),
                        5, TimeUnit.SECONDS);
            }
            default -> {}
        }

        paymentRepository.save(payment);
        leaseRepository.save(lease);
        return lease;
    }

    @Transactional
    public List<Lease> runScheduler() {
        LocalDate today = LocalDate.now();
        List<Lease> all = leaseRepository.findAll();
        for (Lease l : all) {
            if (!l.isAutoPayEnabled()) continue;
            if (l.getDueDayOfMonth() > today.getDayOfMonth()) continue;
            boolean alreadyPaidThisMonth = l.getLastPaymentStatus() == PaymentStatus.SUCCESS &&
                    l.getLastPaymentDate() != null &&
                    l.getLastPaymentDate().getMonth().equals(today.getMonth()) &&
                    l.getLastPaymentDate().getYear() == today.getYear();
            if (alreadyPaidThisMonth) continue;
            payLease(l.getId());
        }
        return leaseRepository.findAll();
    }

    @Transactional
    protected void finalizePendingSuccess(Long paymentId, Long leaseId) {
        Payment p = paymentRepository.findById(paymentId).orElse(null);
        Lease l = leaseRepository.findById(leaseId).orElse(null);
        if (p == null || l == null) return;
        if (p.getStatus() != PaymentStatus.PENDING) return;
        p.setStatus(PaymentStatus.SUCCESS);
        l.setLastPaymentStatus(PaymentStatus.SUCCESS);
        l.setLastPaymentError(null);
        l.setLastPaymentDate(LocalDate.now());
        paymentRepository.save(p);
        leaseRepository.save(l);
        System.out.println("[DummyPSP] Async confirmed SUCCESS for lease " + leaseId);
    }

    @Configuration
    static class SampleData {
        @Bean
        CommandLineRunner initData(TenantRepository tenants, LeaseRepository leases) {
            return args -> {
                if (tenants.count() > 0) return;
                Tenant alice = tenants.save(new Tenant("Alice", "alice@example.com"));
                Tenant bob = tenants.save(new Tenant("Bob", "bob@example.com"));
                Tenant charlie = tenants.save(new Tenant("Charlie", "charlie@example.com"));
                leases.save(new Lease(alice, "123 Oak St", new BigDecimal("1000.00"), 1, true, "CARD"));
                leases.save(new Lease(bob, "456 Pine Ave", new BigDecimal("1200.00"), 1, true, "CARD"));
                leases.save(new Lease(charlie, "789 Maple Rd", new BigDecimal("800.00"), 1, true, "ACH"));
            };
        }
    }
}
