package com.microservices.productservice.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PaymentRequest {
    private String cardHolderName;
    private String cardNumber;
    private String expireMonth;
    private String expireYear;
    private String cvc;
}
