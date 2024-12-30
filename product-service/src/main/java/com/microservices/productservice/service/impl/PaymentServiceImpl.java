package com.microservices.productservice.service.impl;

import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import com.microservices.productservice.model.Customer;
import com.microservices.productservice.model.PaymentRequest;
import com.microservices.productservice.model.Product;
import com.microservices.productservice.repository.CustomerRepository;
import com.microservices.productservice.repository.ProductRepository;
import com.microservices.productservice.service.PaymentService;
import com.microservices.productservice.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public PaymentServiceImpl(ProductRepository productRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public String makePayment(int productId, int customerId, PaymentRequest payment) {
        Product product = productRepository.findById(productId).orElse(null);
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (product == null) {
            return "Product not found";
        }
        if (customer == null) {
            return "Customer not found";
        }

        BigDecimal productPrice = product.getPrice();

        Options options = new Options();
        options.setApiKey("apikey");
        options.setSecretKey("secretkey");
        options.setBaseUrl("https://sandbox-api.iyzipay.com");

        // Create Payment Request
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setLocale(Locale.TR.getValue());
        request.setConversationId("123456789");
        request.setPrice(productPrice);
        request.setPaidPrice(productPrice);
        request.setCurrency(Currency.TRY.name());
        request.setInstallment(1);

        // Credit Card Info
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardNumber(payment.getCardNumber());
        paymentCard.setCardHolderName(payment.getCardHolderName());
        paymentCard.setExpireMonth(payment.getExpireMonth());
        paymentCard.setExpireYear(payment.getExpireYear());
        paymentCard.setCvc(payment.getCvc());

        request.setPaymentCard(paymentCard);

        // Buyer Info from Customer
        Buyer buyer = new Buyer();
        buyer.setId(String.valueOf(customerId));
        buyer.setName(customer.getName());
        buyer.setSurname(customer.getSurName());
        buyer.setGsmNumber(customer.getGsmNumber());
        buyer.setEmail(customer.getEmail());
        buyer.setIdentityNumber(customer.getIdentityNumber());
        buyer.setRegistrationAddress(customer.getAddress());
        buyer.setIp("127.0.0.1");
        buyer.setCity(customer.getCity());
        buyer.setCountry(customer.getCountry());

        request.setBuyer(buyer);

        // ShippingAddress
        Address shippingAddress = new Address();
        shippingAddress.setContactName(buyer.getName() + " " + buyer.getSurname());
        shippingAddress.setCity(buyer.getCity());
        shippingAddress.setCountry(buyer.getCountry());
        shippingAddress.setZipCode(buyer.getZipCode());

        request.setShippingAddress(shippingAddress);

        // BillingAddress
        Address billingAddress = new Address();
        billingAddress.setContactName(buyer.getName() + " " + buyer.getSurname());
        billingAddress.setCity(buyer.getCity());
        billingAddress.setCountry(buyer.getCountry());
        billingAddress.setZipCode(buyer.getZipCode());

        request.setBillingAddress(billingAddress);

        // BasketItem
        List<BasketItem> basketItems = new ArrayList<>();
        BasketItem firstBasketItem = new BasketItem();
        firstBasketItem.setId("BI101"); // Unique
        firstBasketItem.setName(product.getName());
        firstBasketItem.setCategory1("Collectibles");
        firstBasketItem.setCategory2("Accessories");
        firstBasketItem.setItemType(BasketItemType.PHYSICAL.name());
        firstBasketItem.setPrice(productPrice);
        basketItems.add(firstBasketItem);

        request.setBasketItems(basketItems);

        // Iyzico Payment Process
        Payment result = Payment.create(request, options);

        // Payment state
        if ("success".equals(result.getStatus())) {
            return "Payment successful for product " + product.getName();
        } else {
            return "Payment failed" + result.getErrorMessage();
        }
    }
}
