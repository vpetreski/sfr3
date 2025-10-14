package com.sfr3.mvp.integration;

import com.sfr3.mvp.model.Lease;
import java.math.BigDecimal;

public interface PaymentProvider {
    PaymentResult charge(Lease lease, BigDecimal amount);
}
