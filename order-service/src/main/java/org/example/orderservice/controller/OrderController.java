package org.example.orderservice.controller;

import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.jpa.OrderEntity;
import org.example.orderservice.service.OrderService;
import org.example.orderservice.util.OrderMapper;
import org.example.orderservice.vo.RequestOrder;
import org.example.orderservice.vo.ResponseOrder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service") // prefix 설정
public class OrderController {

    Environment env;

    OrderService orderService;

    public OrderController(Environment env, OrderService orderService) {
        this.env = env;
        this.orderService = orderService;
    }

    @GetMapping("/health-check")
    public String status(){
        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                    @RequestBody RequestOrder orderDetails){
        OrderDto orderDto = OrderMapper.INSTANCE.voToDto(orderDetails);

        orderDto.setUserId(userId);

        OrderDto createdOrder = orderService.createOrder(orderDto);

        ResponseOrder responseUser = OrderMapper.INSTANCE.dtoToVo(createdOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

//    @GetMapping("/{orderId}/orders")
//    public ResponseEntity<ResponseOrder>  getOrderByOrderId(@PathVariable("orderId") String orderId){
//        OrderDto orderDto = orderService.getOrderByOrderId(orderId);
//
//        ResponseOrder responseOrder = OrderMapper.INSTANCE.dtoToVo(orderDto);
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseOrder);
//    }

    // Users-Microservice 와 통신
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrderByUserId(@PathVariable("userId") String userId){
        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();

        orderList.forEach(entity -> result.add(OrderMapper.INSTANCE.entityToVo(entity)));

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
