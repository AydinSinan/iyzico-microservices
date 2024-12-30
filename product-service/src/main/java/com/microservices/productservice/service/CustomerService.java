package com.microservices.productservice.service;

import com.microservices.productservice.model.Customer;

public interface CustomerService {
    Customer addCustomer(Customer customer);
    Customer getCustomerById(int id);
}
