package com.temrizhan.task_4.service.impl;

import com.temrizhan.task_4.dto.CustomerDto;
import com.temrizhan.task_4.entity.Customer;
import com.temrizhan.task_4.exception.BusinessException;
import com.temrizhan.task_4.exception.NotFoundException;
import com.temrizhan.task_4.repository.CustomerRepository;
import com.temrizhan.task_4.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public Page<Customer> getAllCustomer(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Transactional
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + customerId));
    }

    @Transactional
    public Customer createCustomer(CustomerDto customerDto) {
        if (customerRepository.existsByEmail(customerDto.email())) {
            throw new BusinessException("Email already exists: " + customerDto.email());
        }

        Customer customer = new Customer();
        customer.setName(customerDto.name());
        customer.setEmail(customerDto.email());

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, CustomerDto customerDto) {
        Customer customer = getCustomerById(id);

        if (!customer.getEmail().equals(customerDto.email()) && customerRepository.existsByEmail(customerDto.email())) {
            throw new BusinessException("Email already in use by another customer: " + customerDto.email());
        }

        customer.setName(customerDto.name());
        customer.setEmail(customerDto.email());

        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }
}