package org.example.orderservice.service;

import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.jpa.OrderEntity;

public interface OrderService {
    // 주문 생성
    OrderDto createOrder(OrderDto orderDetails);

    OrderDto getOrderByOrderId(String orderId);

    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
