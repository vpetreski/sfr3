package com.sfr3.mvp.repository;
import com.sfr3.mvp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentRepository extends JpaRepository<Payment, Long> {}
