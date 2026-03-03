package com.temrizhan.task_4.controller;

import com.temrizhan.task_4.dto.OrderDto;
import com.temrizhan.task_4.entity.Order;
import com.temrizhan.task_4.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@Valid @RequestBody OrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }

    @GetMapping
    public Page<Order> getAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable Long id, @Valid @RequestBody OrderDto orderDto) {
        return orderService.updateOrder(id, orderDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }


    @PostMapping("/{id}/pay")
    public void pay(@PathVariable Long id) {
        orderService.payOrder(id);
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        orderService.cancelOrder(id);
    }
}