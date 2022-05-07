package com.quecode.chapter8;

import com.quecode.chapter6.model.User;
import com.quecode.chapter8.service.EmailService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ForEach {
    public static void main(String[] args) {
        /**
         * 8.9 For Each
         */
        User user1 = new User().setId(17).setName("Alice").setVerified(true).setEmailAddress(Optional.of("alice@innotree.com")).setFriendUserIds(Arrays.asList(201,202,203,204));
        User user2 = new User().setId(18).setName("Bob").setVerified(false).setEmailAddress(Optional.of("bob@innotree.com")).setFriendUserIds(Arrays.asList(202,203,204));
        User user3 = new User().setId(19).setName("Charlie").setVerified(false).setEmailAddress(Optional.of("charlie@innotree.com")).setFriendUserIds(Arrays.asList(201,203,204));
        User user4 = new User().setId(20).setName("David").setVerified(true).setEmailAddress(Optional.of("david@innotree.com")).setFriendUserIds(Arrays.asList(203,204));

        List<User> users = Arrays.asList(user1,user2,user3,user4);

        users.stream()
                .filter(User::isVerified)
                .forEach(EmailService::sendMakeMoreFriendsEmail);
    }
}
