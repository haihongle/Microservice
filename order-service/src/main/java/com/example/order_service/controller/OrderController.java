package com.example.order_service.controller;


import com.example.order_service.entity.Order;
import com.example.order_service.feign.ProductClient;
import com.example.order_service.feign.UserClient;
import com.example.order_service.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;

    public OrderController(OrderRepository orderRepository,
                           UserClient userClient,
                           ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.productClient = productClient;
    }

    @PostMapping
    public Map<String, Object> createOrder(@RequestBody Order order) {

        Order savedOrder = orderRepository.save(order);

        Object user = userClient.getUserById(order.getUserId());
        Object product = productClient.getProductById(order.getProductId());

        Map<String, Object> response = new HashMap<>();
        response.put("order", savedOrder);
        response.put("user", user);
        response.put("product", product);

        System.out.println("Order: " + order);
        System.out.println("User: " + user);
        System.out.println("Product: " + product);


        return response;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}

