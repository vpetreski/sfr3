package com.sfr3.mvp.integration;

import com.sfr3.mvp.model.PaymentStatus;

public class PaymentResult {
    private final PaymentStatus status;
    private final String providerRef;
    private final String message;

    public PaymentResult(PaymentStatus status, String providerRef, String message) {
        this.status = status;
        this.providerRef = providerRef;
        this.message = message;
    }

    public PaymentStatus getStatus() { return status; }
    public String getProviderRef() { return providerRef; }
    public String getMessage() { return message; }

    public static PaymentResult success(String ref) { return new PaymentResult(PaymentStatus.SUCCESS, ref, null); }
    public static PaymentResult failed(String msg) { return new PaymentResult(PaymentStatus.FAILED, null, msg); }
    public static PaymentResult pending(String ref) { return new PaymentResult(PaymentStatus.PENDING, ref, null); }
}
