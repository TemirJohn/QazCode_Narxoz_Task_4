package com.temrizhan.task_4.service.impl;

import com.temrizhan.task_4.dto.OrderDto;
import com.temrizhan.task_4.entity.Customer;
import com.temrizhan.task_4.entity.Order;
import com.temrizhan.task_4.entity.OrderStatus;
import com.temrizhan.task_4.exception.BusinessException;
import com.temrizhan.task_4.exception.NotFoundException;
import com.temrizhan.task_4.repository.OrderRepository;
import com.temrizhan.task_4.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void getAllOrders_ReturnsPageOfOrders() {
        Order order = new Order();
        order.setId(1L);
        Page<Order> expectedPage = new PageImpl<>(List.of(order));
        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Order> actualPage = orderService.getAllOrders(pageable);

        assertEquals(1, actualPage.getTotalElements());
        verify(orderRepository).findAll(pageable);
    }

    @Test
    void createOrder_WhenCustomerExists_SavesOrderWithNewStatus() {
        OrderDto dto = new OrderDto(1L, new BigDecimal("100.50"));
        Customer customer = new Customer();
        customer.setId(1L);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setCustomerId(customer);
        savedOrder.setAmount(dto.amount());
        savedOrder.setStatus(OrderStatus.NEW);

        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.createOrder(dto);

        assertEquals(OrderStatus.NEW, result.getStatus());
        assertEquals(new BigDecimal("100.50"), result.getAmount());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void payOrder_WhenStatusIsNew_ChangesStatusToPaid() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.NEW);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.payOrder(1L);

        assertEquals(OrderStatus.PAID, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void payOrder_WhenStatusIsNotNew_ThrowsBusinessException() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BusinessException.class, () -> orderService.payOrder(1L));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void cancelOrder_WhenStatusIsPaid_ThrowsBusinessException() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BusinessException.class, () -> orderService.cancelOrder(1L));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void cancelOrder_WhenStatusIsNew_ChangesStatusToCancelled() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.NEW);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepository).save(order);
    }
}