package com.microservices.productservice.service.impl;

import com.microservices.productservice.model.Customer;
import com.microservices.productservice.repository.CustomerRepository;
import com.microservices.productservice.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(int id) {
        return customerRepository.findById(id).orElse(null);
    }
}
