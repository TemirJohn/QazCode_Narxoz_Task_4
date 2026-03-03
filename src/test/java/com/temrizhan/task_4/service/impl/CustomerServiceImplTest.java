package com.temrizhan.task_4.service.impl;

import com.temrizhan.task_4.dto.CustomerDto;
import com.temrizhan.task_4.entity.Customer;
import com.temrizhan.task_4.exception.BusinessException;
import com.temrizhan.task_4.exception.NotFoundException;
import com.temrizhan.task_4.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void getAllCustomer_ReturnsPageOfCustomers() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Mergen Temrizhan");
        Page<Customer> expectedPage = new PageImpl<>(List.of(customer));
        Pageable pageable = PageRequest.of(0, 10);

        when(customerRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Customer> actualPage = customerService.getAllCustomer(pageable);

        assertEquals(1, actualPage.getTotalElements());
        assertEquals("Mergen Temrizhan", actualPage.getContent().get(0).getName());
        verify(customerRepository, times(1)).findAll(pageable);
    }

    @Test
    void getCustomerById_WhenCustomerExists_ReturnsCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getCustomerById_WhenCustomerDoesNotExist_ThrowsNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    void createCustomer_WhenEmailIsUnique_SavesAndReturnsCustomer() {
        CustomerDto dto = new CustomerDto("Mergen Temrizhan", "mergen.temirzhan@example.com");
        Customer savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setName(dto.name());
        savedCustomer.setEmail(dto.email());

        when(customerRepository.existsByEmail(dto.email())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        Customer result = customerService.createCustomer(dto);

        assertNotNull(result);
        assertEquals("Mergen Temrizhan", result.getName());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomer_WhenEmailExists_ThrowsBusinessException() {
        CustomerDto dto = new CustomerDto("Mergen Temrizhan", "mergen.temirzhan@example.com");
        when(customerRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(BusinessException.class, () -> customerService.createCustomer(dto));
        verify(customerRepository, never()).save(any(Customer.class));
    }
}