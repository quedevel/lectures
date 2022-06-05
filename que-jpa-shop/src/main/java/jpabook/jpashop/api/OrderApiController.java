package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDTO;
import jpabook.jpashop.repository.order.query.OrderItemQueryDTO;
import jpabook.jpashop.repository.order.query.OrderQueryDTO;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

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
                .collect(toList());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDTO> ordersV3(){
        return orderRepository.findAllWithItem().stream()
                .map(OrderDTO::new)
                .collect(toList());
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDTO> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        return orders.stream()
                .map(OrderDTO::new)
                .collect(toList());
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDTO> ordersV4(){
        return orderQueryRepository.findOrderQueryOrderDTOs();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDTO> ordersV5(){
        return orderQueryRepository.findAllByDTO_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDTO> ordersV6(){
        List<OrderFlatDTO> flats = orderQueryRepository.findAllByDTO_flat();
        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDTO(o.getOrderId(), o.getName(), o.getOrderDate(), o.getStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDTO(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDTO(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());
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
                    .collect(toList());
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
