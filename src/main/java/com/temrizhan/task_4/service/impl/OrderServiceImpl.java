package com.temrizhan.task_4.service.impl;

import com.temrizhan.task_4.dto.OrderDto;
import com.temrizhan.task_4.entity.Customer;
import com.temrizhan.task_4.entity.Order;
import com.temrizhan.task_4.entity.OrderStatus;
import com.temrizhan.task_4.exception.BusinessException;
import com.temrizhan.task_4.exception.NotFoundException;
import com.temrizhan.task_4.repository.OrderRepository;
import com.temrizhan.task_4.service.CustomerService;
import com.temrizhan.task_4.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;

    @Transactional
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Transactional
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));
    }


    @Transactional
    public Order createOrder(OrderDto orderDto) {
        customerService.getCustomerById(orderDto.customerId());

        Order order = new Order();
        order.setCustomerId(orderDto.customerId());
        order.setAmount(orderDto.amount());
        order.setStatus(OrderStatus.NEW);

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(Long id, OrderDto orderDto) {
        Order order = getOrderById(id);
        customerService.getCustomerById(orderDto.customerId());

        order.setCustomerId(orderDto.customerId());
        order.setAmount(orderDto.amount());

        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }


    @Transactional
    public void payOrder(Long id) {
        Order order = getOrderById(id);

        if (order.getStatus() != OrderStatus.NEW) {
            throw new BusinessException("Order cannot be paid. Current status is: " + order.getStatus());
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long id) {
        Order order = getOrderById(id);

        if (order.getStatus() == OrderStatus.PAID) {
            throw new BusinessException("Cannot cancel a PAID order");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
