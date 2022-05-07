package com.quecode.chapter8;

import com.quecode.chapter6.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Reduce {
    public static void main(String[] args) {
        /**
         * 8.4 Reduce
         */
        List<Integer> integerList = Arrays.asList(1,4,-2,-5,3);
        int sum = integerList.stream().reduce((x,y) -> x+y).get();
        System.out.println("sum = " + sum);

        int min = integerList.stream().reduce((x,y) -> {
            if(x < y){
                return x;
            }
            return y;
        }).get();
        System.out.println("min = " + min);

        int product = integerList.stream().reduce(1,(x,y)->x*y);
        System.out.println("product = " + product);

        List<String> strings = Arrays.asList("3","2","5","-4");
        Integer integerSum = strings.stream().map(Integer::parseInt).reduce(0, (x, y) -> x + y);
        System.out.println("integerSum = " + integerSum);

        Integer integerSum2 = strings.stream().reduce(0,(i,s) -> i +Integer.parseInt(s), (n1,n2) -> n1+n2);
        System.out.println("integerSum2 = " + integerSum2);

        User user1 = new User().setId(1).setName("Alice").setVerified(true).setEmailAddress(Optional.of("alice@innotree.com")).setFriendUserIds(Arrays.asList(201,202,203,204));
        User user2 = new User().setId(2).setName("Bob").setVerified(false).setEmailAddress(Optional.of("bob@innotree.com")).setFriendUserIds(Arrays.asList(202,203,204));
        User user3 = new User().setId(3).setName("Charlie").setVerified(false).setEmailAddress(Optional.of("charlie@innotree.com")).setFriendUserIds(Arrays.asList(201,203,204));
        User user4 = new User().setId(4).setName("David").setVerified(true).setEmailAddress(Optional.of("david@innotree.com")).setFriendUserIds(Arrays.asList(203,204));

        List<User> users = Arrays.asList(user1,user2,user3,user4);
        Integer sumOfNumberOfFriends = users.stream().map(User::getFriendUserIds).map(List::size).reduce(0, (x, y) -> x + y);
        System.out.println("sumOfNumberOfFriends = " + sumOfNumberOfFriends);

        //Integer sumOfNumberOfEmailLength = users.stream().map(User::getEmailAddress).map(String::length).reduce(0, (x, y) -> x + y);
//        System.out.println("sumOfNumberOfEmailLength = " + sumOfNumberOfEmailLength);
    }
}
