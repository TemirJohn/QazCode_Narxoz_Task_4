package com.temrizhan.task_4.service;

import com.temrizhan.task_4.dto.OrderDto;
import com.temrizhan.task_4.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderService {
    Page<Order> getAllOrders(Pageable pageable);
    Order getOrderById(Long id);
    Order createOrder(OrderDto orderDto);
    Order updateOrder(Long id, OrderDto orderDto);
    void deleteOrder(Long id);
    void payOrder(Long id);
    void cancelOrder(Long id);
}
