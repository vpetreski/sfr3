package com.sfr3.mvp.integration;

import com.sfr3.mvp.model.Lease;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Slf4j
public class DummyPaymentProvider implements PaymentProvider {

    @Override
    public PaymentResult charge(Lease lease, BigDecimal amount) {
        String method = lease.getPaymentMethod() == null ? "CARD" : lease.getPaymentMethod().toUpperCase();
        if ("ACH".equals(method)) {
            String ref = "ACH-" + UUID.randomUUID();
            log.info("[DummyPSP] ACH initiated lease {} amount {} -> PENDING {}", lease.getId(), amount, ref);
            return PaymentResult.pending(ref);
        } else {
            boolean shouldFail = (lease.getId() != null && lease.getId() % 2 == 0);
            if (shouldFail) {
                return PaymentResult.failed("Payment declined by bank (simulated)");
            } else {
                String ref = "CARD-" + UUID.randomUUID();
                return PaymentResult.success(ref);
            }
        }
    }
}
