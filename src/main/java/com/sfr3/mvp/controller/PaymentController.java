package com.sfr3.mvp.controller;

import com.sfr3.mvp.model.Lease;
import com.sfr3.mvp.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/leases")
    public List<Lease> leases() {
        return paymentService.listLeases();
    }

    @PostMapping("/manualPay")
    public Lease manualPay(@RequestParam Long leaseId) {
        return paymentService.payLease(leaseId);
    }

    @PostMapping("/runScheduler")
    public List<Lease> runScheduler() {
        return paymentService.runScheduler();
    }
}
