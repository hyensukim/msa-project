package org.example.orderservice.service;

import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.jpa.OrderEntity;
import org.example.orderservice.jpa.OrderRepository;
import org.example.orderservice.util.OrderMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDetails) {

        orderDetails.setOrderId(UUID.randomUUID().toString());
        orderDetails.setTotalPrice(orderDetails.getUnitPrice() * orderDetails.getQty()); // 수량 * 단가

        OrderEntity orderEntity = OrderMapper.INSTANCE.dtoToEntity(orderDetails);

        orderEntity = orderRepository.save(orderEntity);

        return OrderMapper.INSTANCE.entityToDto(orderEntity);
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        return OrderMapper.INSTANCE.entityToDto(orderEntity);
    }

    @Override
    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
