package com.quecode.chapter8;

import com.quecode.chapter6.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToMap {
    public static void main(String[] args) {
        /**
         * 8.6 To Map
         */
        Map<Integer, String> stringMap = Stream.of(3, 5, -4, 2, 6).collect(Collectors.toMap(x -> x, x -> "Number is " + x));
        System.out.println("stringMap = " + stringMap.get(3));

        User user1 = new User().setId(5).setName("Alice").setVerified(true).setEmailAddress(Optional.of("alice@innotree.com")).setFriendUserIds(Arrays.asList(201,202,203,204));
        User user2 = new User().setId(6).setName("Bob").setVerified(false).setEmailAddress(Optional.of("bob@innotree.com")).setFriendUserIds(Arrays.asList(202,203,204));
        User user3 = new User().setId(7).setName("Charlie").setVerified(false).setEmailAddress(Optional.of("charlie@innotree.com")).setFriendUserIds(Arrays.asList(201,203,204));
        User user4 = new User().setId(8).setName("David").setVerified(true).setEmailAddress(Optional.of("david@innotree.com")).setFriendUserIds(Arrays.asList(203,204));

        List<User> users = Arrays.asList(user1,user2,user3,user4);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        System.out.println("userMap = " + userMap);
    }
}
