package com.javatechie.microservices.order_service.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.microservices.order_service.entity.Order;
import com.javatechie.microservices.order_service.entity.Payment;
import com.javatechie.microservices.order_service.entity.TransactionRequest;
import com.javatechie.microservices.order_service.entity.TransactionResponse;
import com.javatechie.microservices.order_service.repository.OrderRepository;

@Service
@RefreshScope
public class OrderService {
	
	 Logger logger= LoggerFactory.getLogger(OrderService.class);
	    @Autowired
	    private OrderRepository repository;
	    @Autowired
	    @Lazy
	    private RestTemplate template;
	    
	    @Value("${microservice.payment-service.endpoints.endpoint.uri}")
	    private String ENDPOINT_URL;
	    
	    
		public TransactionResponse saveOrder(TransactionRequest request) throws JsonProcessingException {
			// TODO Auto-generated method stub
			 String response = "";
			   Order order=request.getOrder();
			   Payment payment=request.getPayment();
			   payment.setAmount(order.getPrice());
			   payment.setOrderId(order.getId());
			   logger.info("Order-Service Request : "+new ObjectMapper().writeValueAsString(request));
			   
			  Payment paymentResponse= template.postForObject(ENDPOINT_URL, payment, Payment.class);
			 
			   response = paymentResponse.getPaymentStatus().equals("success") ? "payment processing successful and order placed" : "there is a failure in payment api , order added to cart";
		        logger.info("Order Service getting Response from Payment-Service : "+new ObjectMapper().writeValueAsString(response));
		        repository.save(order);
		        return new TransactionResponse(order, paymentResponse.getAmount(), paymentResponse.getTransactionId(), response);
		}

}
