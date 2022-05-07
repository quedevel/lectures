package com.quecode.chapter8;

import com.quecode.chapter6.model.User;
import com.quecode.chapter8.service.EmailService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PartitioningBy {
    public static void main(String[] args) {
        /**
         * 8.8 Partitioning By
         */
        List<Integer> integerList = Arrays.asList(13, 2, 101, 203, 304, 402, 305, 349, 2312, 203, 70);
        Map<Boolean, List<Integer>> booleanListMap = integerList.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("booleanListMap = " + booleanListMap);

        User user1 = new User().setId(13).setName("Alice").setVerified(true).setEmailAddress(Optional.of("alice@innotree.com")).setFriendUserIds(Arrays.asList(201,202,203,204));
        User user2 = new User().setId(14).setName("Bob").setVerified(false).setEmailAddress(Optional.of("bob@innotree.com")).setFriendUserIds(Arrays.asList(202,203,204));
        User user3 = new User().setId(15).setName("Charlie").setVerified(false).setEmailAddress(Optional.of("charlie@innotree.com")).setFriendUserIds(Arrays.asList(201,203,204));
        User user4 = new User().setId(16).setName("David").setVerified(true).setEmailAddress(Optional.of("david@innotree.com")).setFriendUserIds(Arrays.asList(203,204));

        List<User> users = Arrays.asList(user1,user2,user3,user4);
        Map<Boolean, List<User>> booleanListMap1 = users.stream()
                .collect(Collectors.partitioningBy(u -> u.getFriendUserIds().size() > 2));
        System.out.println("booleanListMap1 = " + booleanListMap1);

        EmailService emailService = new EmailService();

        for (User user : booleanListMap1.get(true)) {
            emailService.sendPlayWithFriendsEmail(user);
        }

        for (User user : booleanListMap1.get(false)) {
            emailService.sendMakeMoreFriendsEmail(user);
        }
    }
}
