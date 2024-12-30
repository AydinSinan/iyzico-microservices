package com.microservices.productservice.service;

import com.microservices.productservice.model.PaymentRequest;

public interface PaymentService {

    String makePayment(int productId, int customerId, PaymentRequest paymentRequest);
}
