package com.javatechie.microservices.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javatechie.microservices.payment_service.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    Payment findByOrderId(int orderId);
}