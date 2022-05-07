package com.quecode.chapter8;

import com.quecode.chapter6.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GroupingBy {
    public static void main(String[] args) {
        /**
         * 8.7 Grouping By
         */
        List<Integer> integerList = Arrays.asList(13, 2, 101, 203, 304, 402, 305, 349, 2312, 203, 70);
        Map<Integer, List<Integer>> integerListMap = integerList.stream().collect(Collectors.groupingBy(n -> n % 10));
        System.out.println("integerListMap = " + integerListMap);

        Map<Integer, List<String>> integerListMap1 = integerList.stream()
                .collect(Collectors.groupingBy(n -> n % 10, Collectors.mapping(n -> "unit digit is " + n, Collectors.toList())));
        System.out.println("integerListMap1 = " + integerListMap1);

        User user1 = new User().setId(9).setName("Alice").setVerified(true).setEmailAddress(Optional.of("alice@innotree.com")).setFriendUserIds(Arrays.asList(201,202,203,204));
        User user2 = new User().setId(10).setName("Bob").setVerified(false).setEmailAddress(Optional.of("bob@innotree.com")).setFriendUserIds(Arrays.asList(202,203,204));
        User user3 = new User().setId(11).setName("Charlie").setVerified(false).setEmailAddress(Optional.of("charlie@innotree.com")).setFriendUserIds(Arrays.asList(201,203,204));
        User user4 = new User().setId(12).setName("David").setVerified(true).setEmailAddress(Optional.of("david@innotree.com")).setFriendUserIds(Arrays.asList(203,204));

        List<User> users = Arrays.asList(user1,user2,user3,user4);
        Map<Boolean, List<String>> booleanListMap = users.stream()
                .collect(Collectors.groupingBy(User::isVerified, Collectors.mapping(user -> "User name is " + user.getName(), Collectors.toList())));
        System.out.println("booleanListMap = " + booleanListMap);


    }
}
