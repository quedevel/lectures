package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDTO {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Address address;

    public OrderSimpleQueryDTO(Long orderId, String name, LocalDateTime orderDate, OrderStatus status, Address address){
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.status = status;
        this.address = address;
    }
}