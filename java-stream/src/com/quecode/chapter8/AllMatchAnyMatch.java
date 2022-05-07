package com.quecode.chapter8;

import com.quecode.chapter6.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AllMatchAnyMatch {
    public static void main(String[] args) {
        /**
         * 8.2 All Match Any Match
         */
        List<Integer> integerList = Arrays.asList(3, -4, 2, 7, 9);
        boolean allPositive = integerList.stream().allMatch(i -> i >0);
        System.out.println("allPositive = " + allPositive);
        boolean anyNegative = integerList.stream().anyMatch(i -> i <0);
        System.out.println("anyNegative = " + anyNegative);

        User user1 = new User().setId(108).setName("Alice").setVerified(true).setEmailAddress(Optional.of("alice@innotree.com"));
        User user2 = new User().setId(109).setName("Bob").setVerified(false).setEmailAddress(Optional.of("bob@innotree.com"));
        User user3 = new User().setId(110).setName("Charlie").setVerified(false).setEmailAddress(Optional.of("charlie@innotree.com"));
        User user4 = new User().setId(111).setName("David").setVerified(true).setEmailAddress(Optional.of("david@innotree.com"));

        List<User> users = Arrays.asList(user1, user2, user3, user4);
        boolean allMatch = users.stream().allMatch(User::isVerified);
        System.out.println("allMatch = " + allMatch);
    }
}
