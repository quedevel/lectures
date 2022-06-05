package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        all.forEach(order -> {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem -> orderItem.getItem().getName());
        });
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDTO> ordersV2(){
        return orderRepository.findAllByString(new OrderSearch()).stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDTO> ordersV3(){
        return orderRepository.findAllWithItem().stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    @Data
    static class OrderDTO{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus status;
        private Address address;
        private List<OrderItemDTO> orderItems;
        public OrderDTO(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            status = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDTO{
        private String itemName;
        private int orderPrice;
        private int count;
        public OrderItemDTO(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
