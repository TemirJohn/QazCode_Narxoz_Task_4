package com.temrizhan.task_4.service;

import com.temrizhan.task_4.dto.CustomerDto;
import com.temrizhan.task_4.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomerService {
    Page<Customer> getAllCustomer(Pageable pageable);
    Customer getCustomerById(Long customerId);
    Customer createCustomer(CustomerDto customerDto);
    Customer updateCustomer(Long id, CustomerDto customerDto);
    void deleteCustomer(Long id);
}
