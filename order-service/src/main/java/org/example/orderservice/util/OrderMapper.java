package org.example.orderservice.util;


import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.jpa.OrderEntity;
import org.example.orderservice.vo.RequestOrder;
import org.example.orderservice.vo.ResponseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    // target : entity, source : dto - db 입력 데이터
    @Mapping(target = "productId" ,source ="productId" )
    @Mapping(target = "qty" ,source ="qty" )
    @Mapping(target = "unitPrice" ,source ="unitPrice" )
    @Mapping(target = "totalPrice" ,source ="totalPrice" )
    @Mapping(target = "orderId" ,source ="orderId" )
    @Mapping(target = "userId" ,source ="userId" )
    OrderEntity dtoToEntity(OrderDto orderDto);

    // target : dto, source : entity - db 데이터 반환
    @Mapping(target = "productId" ,source = "productId" )
    @Mapping(target = "qty" ,source = "qty" )
    @Mapping(target = "unitPrice" ,source = "unitPrice" )
    @Mapping(target = "totalPrice" ,source = "totalPrice" )
    @Mapping(target = "orderId" ,source = "orderId" )
    @Mapping(target = "createdAt", source = "createdAt")
    OrderDto entityToDto(OrderEntity orderEntity);

    // target : dto, source : vo - 입력 데이터
    @Mapping(target = "productId" ,source = "productId" )
    @Mapping(target = "qty" ,source = "qty" )
    @Mapping(target = "unitPrice" ,source = "unitPrice" )
    OrderDto voToDto(RequestOrder requestOrder);

    // target : vo, source : dto - 출력 데이터
    @Mapping(target = "productId" ,source = "productId" )
    @Mapping(target = "qty" ,source = "qty" )
    @Mapping(target = "unitPrice" ,source = "unitPrice" )
    @Mapping(target = "totalPrice" ,source = "totalPrice" )
    @Mapping(target = "orderId" ,source = "orderId" )
    @Mapping(target = "createdAt", source = "createdAt")
    ResponseOrder dtoToVo(OrderDto orderDto);

    // target : vo, source : entity
    @Mapping(target = "productId" ,source = "productId" )
    @Mapping(target = "qty" ,source = "qty" )
    @Mapping(target = "unitPrice" ,source = "unitPrice" )
    @Mapping(target = "totalPrice" ,source = "totalPrice" )
    @Mapping(target = "orderId" ,source = "orderId" )
    @Mapping(target = "createdAt", source = "createdAt")
    ResponseOrder entityToVo(OrderEntity orderEntity);
}
