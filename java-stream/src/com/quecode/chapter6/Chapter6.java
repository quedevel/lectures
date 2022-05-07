package com.quecode.chapter6;

import com.quecode.chapter6.model.Order;
import com.quecode.chapter6.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 6.1 Stream
 * 6.2 Filter
 * 6.3 Map
 * 6.4 Stream의 구성 요소
 * 6.5 데이터 정렬
 * 6.6 중복제거
 * 6.7 FlatMap
 */
public class Chapter6 {

    public static void main(String[] args) {
        /**
         * 6.1 Stream
         */
        Stream<String> nameStream = Stream.of("Alice", "Bob", "Charlie");
        List<String> names = nameStream.collect(Collectors.toList());
        System.out.println("names = " + names);

        String[] cityArray = new String[]{"San Jose", "Seoul", "Tokyo"};
        Stream<String> cityStream = Arrays.stream(cityArray);
        List<String> cityList = cityStream.collect(Collectors.toList());
        System.out.println("cityList = " + cityList);

        Set<Integer> numberSet = new HashSet<>(Arrays.asList(3,5,7));
        Stream<Integer> numberStream = numberSet.stream();
        List<Integer> numberList = numberStream.collect(Collectors.toList());
        System.out.println("numberList = " + numberList);

        System.out.println();
        System.out.println();

        /**
         * 6.2 Filter
         */
        Stream<Integer> integerStream = Stream.of(3,-5,7,10,-3);
        Stream<Integer> filteredIntegerStream1 = integerStream.filter(x -> x > 0);
        List<Integer> filteredIntegers = filteredIntegerStream1.collect(Collectors.toList());
        System.out.println("filteredIntegers = " + filteredIntegers);

        List<Integer> filteredIntegers2 = Stream.of(3,-5,7,10,-3).filter(x -> x>0).collect(Collectors.toList());
        System.out.println("filteredIntegers2 = " + filteredIntegers2);

        User user1 = new User().setId(101).setName("Alice").setVerified(true).setEmailAddress(Optional.of("alice@innotree.com"));
        User user2 = new User().setId(102).setName("Bob").setVerified(false).setEmailAddress(Optional.of("bob@innotree.com"));
        User user3 = new User().setId(103).setName("Charlie").setVerified(false).setEmailAddress(Optional.of("charlie@innotree.com"));

        List<User> verifiedUsers = Stream.of(user1, user2, user3).filter(User::isVerified).collect(Collectors.toList());
        System.out.println("검증된 유저");
        verifiedUsers.forEach(System.out::println);

        List<User> unverifiedUsres = Stream.of(user1, user2, user3).filter(user -> !user.isVerified()).collect(Collectors.toList());
        System.out.println("검증되지 않은 유저");
        unverifiedUsres.forEach(System.out::println);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Order order1 = new Order().setId(1001).setStatus(Order.OrderStatus.CREATED).setCreatedByUserId(101).setCreatedAt(now.minusHours(4));
        Order order2 = new Order().setId(1002).setStatus(Order.OrderStatus.ERROR).setCreatedByUserId(102).setCreatedAt(now.minusHours(1));
        Order order3 = new Order().setId(1003).setStatus(Order.OrderStatus.IN_PROGRESS).setCreatedByUserId(103).setCreatedAt(now.minusHours(3));
        Order order4 = new Order().setId(1004).setStatus(Order.OrderStatus.PROCESSED).setCreatedByUserId(104).setCreatedAt(now.minusHours(36));
        Order order5 = new Order().setId(1005).setStatus(Order.OrderStatus.ERROR).setCreatedByUserId(105).setCreatedAt(now.minusHours(24));

        List<Order> filteredOrders = Stream.of(order1,order2,order3,order4,order5).filter(order -> order.getStatus() == Order.OrderStatus.ERROR).collect(Collectors.toList());
        System.out.println("ERROR 상태인 주문");
        System.out.println("filteredOrders = " + filteredOrders);
        System.out.println();
        System.out.println();

        /**
         * 6.3 Map
         */
        List<Integer> integers = Stream.of(3,6,-4).map(x -> x * 2).collect(Collectors.toList());
        System.out.println("integers = " + integers);

        List<String> strings = Stream.of(3,6,-4).map(x -> "Number is "+x).collect(Collectors.toList());
        System.out.println("strings = " + strings);

//        List<String> emails = Stream.of(user1, user2, user3).map(User::getEmailAddress).collect(Collectors.toList());
//        System.out.println("emails = " + emails);

        List<Long> userIds = Stream.of(order1,order2,order3,order4,order5).map(Order::getCreatedByUserId).collect(Collectors.toList());
        System.out.println("userIds = " + userIds);
        System.out.println();
        System.out.println();

        /**
         * 6.4 Stream의 구성 요소
         */
        List<User> users = Arrays.asList(user1, user2, user3);
        List<String> emailList = new ArrayList<>();
//        for (User user : users) {
//            if(!user.isVerified()){
//                emailList.add(user.getEmailAddress());
//            }
//        }
        System.out.println("emailList = " + emailList);

//        List<String> emailList2 = Stream.of(user1, user2, user3)
//                .filter(u -> !u.isVerified())
//                .map(User::getEmailAddress)
//                .collect(Collectors.toList());
//
//        System.out.println("emailList2 = " + emailList2);

        List<Order> orders = Arrays.asList(order1,order2,order3,order4,order5);
        List<Long> userIdList = orders.stream()
                .filter(o-> o.getStatus() == Order.OrderStatus.ERROR)
                .map(Order::getId)
                .collect(Collectors.toList());

        System.out.println("userIdList = " + userIdList);

        List<Long> userIdList2 = orders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.ERROR)
                .filter(o -> o.getCreatedAt().isAfter(now.minusHours(25)))
                .map(Order::getId)
                .collect(Collectors.toList());

        System.out.println("userIdList2 = " + userIdList2);
        System.out.println();
        System.out.println();

        /**
         * 6.5 데이터의 정렬
         */
        List<Integer> integerList = Arrays.asList(3,-5,7,4);
        List<Integer> sortedIntegers = integerList.stream()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("sortedIntegers = " + sortedIntegers);

        List<User> sortedUsers = users.stream()
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());

        System.out.println("sortedUsers = " + sortedUsers);

        List<Long> sortedOrders = orders.stream()
                .sorted(Comparator.comparing(Order::getCreatedAt))
                .map(Order::getId)
                .collect(Collectors.toList());

        System.out.println("sortedOrders = " + sortedOrders);
        System.out.println();
        System.out.println();

        /**
         * 6.6 중복제거
         */
        List<Integer> integerList1 = Arrays.asList(3,-5,4,-5,2,3);
        List<Integer> distinctIntegers1 = integerList1.stream().distinct().collect(Collectors.toList());
        System.out.println("distinctIntegers1 = " + distinctIntegers1);

        List<Long> distinctOrder = orders.stream().map(Order::getCreatedByUserId).distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        System.out.println("distinctOrder = " + distinctOrder);
        System.out.println();
        System.out.println();

        /**
         * 6.7 FlatMap
         */
        String[][] cities = new String[][]{
                {"Seoul","Busan"},
                {"San Francisco","New York"},
                {"Madrid","Barcelona"}
        };
        Stream<String[]> cityStream2 = Arrays.stream(cities);
        List<String> cityList2 = cityStream2.flatMap(Arrays::stream).collect(Collectors.toList());
        System.out.println("cityList2 = " + cityList2);


    }
}
