package com.quecode.chapter8;

import com.quecode.chapter6.model.Order;
import com.quecode.chapter6.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MaxMinCount {
    public static void main(String[] args) {
        /**
         * 8.1 Max Min Count
         */
        Optional<Integer> max = Stream.of(5, 3, 6, 2, 1).max(Comparator.comparingInt(x -> x));
        System.out.println("max = " + max.get());

        User user1 = new User().setId(104).setName("Alice").setVerified(true).setEmailAddress(Optional.of("alice@innotree.com"));
        User user2 = new User().setId(105).setName("Bob").setVerified(false).setEmailAddress(Optional.of("bob@innotree.com"));
        User user3 = new User().setId(106).setName("Charlie").setVerified(false).setEmailAddress(Optional.of("charlie@innotree.com"));
        User user4 = new User().setId(107).setName("David").setVerified(true).setEmailAddress(Optional.of("david@innotree.com"));

        List<User> users = Arrays.asList(user1,user2,user3);

        User firstUser = users.stream()
                .min(Comparator.comparing(User::getName))
                .get();
        System.out.println("firstUser = " + firstUser);

        long count = Stream.of(1, -4, 5, -3, 6).filter(x -> x > 0).count();
        System.out.println("count = " + count);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        user1.setCreatedAt(now.minusDays(2));
        user2.setCreatedAt(now.minusHours(10));
        user3.setCreatedAt(now.minusHours(1));
        user4.setCreatedAt(now.minusHours(27));

        List<User> userList = Arrays.asList(user1, user2, user3, user4);
        long count1 = userList.stream()
                .filter(user -> user.getCreatedAt().isAfter(now.minusDays(1)))
                .filter(user -> !user.isVerified())
                .count();
        System.out.println("count1 = " + count1);

        Order order1 = new Order().setId(1006).setStatus(Order.OrderStatus.CREATED).setCreatedByUserId(101).setCreatedAt(now.minusHours(4));
        Order order2 = new Order().setId(1007).setStatus(Order.OrderStatus.ERROR).setCreatedByUserId(102).setCreatedAt(now.minusHours(1));
        Order order3 = new Order().setId(1008).setStatus(Order.OrderStatus.IN_PROGRESS).setCreatedByUserId(103).setCreatedAt(now.minusHours(3));
        Order order4 = new Order().setId(1009).setStatus(Order.OrderStatus.PROCESSED).setCreatedByUserId(104).setCreatedAt(now.minusHours(36));
        Order order5 = new Order().setId(1010).setStatus(Order.OrderStatus.ERROR).setCreatedByUserId(105).setCreatedAt(now.minusHours(24));

        order1.setAmount(BigDecimal.valueOf(1000));
        order2.setAmount(BigDecimal.valueOf(6000));
        order3.setAmount(BigDecimal.valueOf(7000));
        order4.setAmount(BigDecimal.valueOf(2000));
        order5.setAmount(BigDecimal.valueOf(5000));

        List<Order> orders = Arrays.asList(order1, order2, order3, order4, order5);
        Optional<Order> max1 = orders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.ERROR)
                .max(Comparator.comparing(Order::getAmount));
        System.out.println("max1 = " + max1.get());

        BigDecimal max2 = orders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.ERROR)
                .map(Order::getAmount)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        System.out.println("max2 = " + max2);
    }
}
