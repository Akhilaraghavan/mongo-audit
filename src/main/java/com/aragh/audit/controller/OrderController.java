package com.aragh.audit.controller;

import com.aragh.audit.model.Order;
import com.aragh.audit.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1")
public class OrderController {

    private OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders")
    public List<Order> findOrders() {
        return orderRepository.findAll();
    }

    @PostMapping("/order")
    public String saveOrder(@RequestBody Order order) {
        return orderRepository.save(order).getId();
    }
}
