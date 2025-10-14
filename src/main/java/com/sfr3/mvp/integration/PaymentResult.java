package com.sfr3.mvp.integration;

import com.sfr3.mvp.model.PaymentStatus;

public record PaymentResult(PaymentStatus status, String providerRef, String message) {

    public static PaymentResult success(String ref) {
        return new PaymentResult(PaymentStatus.SUCCESS, ref, null);
    }

    public static PaymentResult failed(String msg) {
        return new PaymentResult(PaymentStatus.FAILED, null, msg);
    }

    public static PaymentResult pending(String ref) {
        return new PaymentResult(PaymentStatus.PENDING, ref, null);
    }
}
