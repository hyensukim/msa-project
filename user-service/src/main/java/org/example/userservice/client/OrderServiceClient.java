package org.example.userservice.client;

import org.example.userservice.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="order-service") // Eureka Server 에 등록된 네이밍
public interface OrderServiceClient {

//    인터페이스 특성상 모든 추상 메서드는 public abstract 입니다.

    @GetMapping("/order-service/{userId}/orders_ng")
    List<ResponseOrder> getOrders(@PathVariable String userId);

}
