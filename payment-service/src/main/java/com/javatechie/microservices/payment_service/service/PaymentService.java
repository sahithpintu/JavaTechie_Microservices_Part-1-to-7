package com.javatechie.microservices.payment_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.microservices.payment_service.entity.Payment;
import com.javatechie.microservices.payment_service.repository.PaymentRepository;

@Service
@RefreshScope
public class PaymentService {

	   @Autowired
	    private PaymentRepository repository;
	   

	    Logger logger= LoggerFactory.getLogger(PaymentService.class);
	    
	public Payment doPayment(Payment payment) throws JsonProcessingException {
		
		payment.setTransactionId(UUID.randomUUID().toString());
		payment.setPaymentStatus(paymentProcessing());
		  logger.info("Payment-Service Request : {}",new ObjectMapper().writeValueAsString(payment));
		// TODO Auto-generated method stub
		  
		return repository.save(payment);
	}

	private String paymentProcessing() {
		// TODO Auto-generated method stub
		return new Random().nextBoolean()?"success":"false";
	}

	public Payment findPaymentHistoryByOrderId(int orderId) throws JsonProcessingException {
		// TODO Auto-generated method stub
		Payment payment= repository.findByOrderId(orderId);
		logger.info("paymentService findPaymentHistoryByOrderId : {}",new ObjectMapper().writeValueAsString(payment));
		return payment;
	}

}
