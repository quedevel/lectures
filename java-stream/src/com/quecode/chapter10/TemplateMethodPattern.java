package com.quecode.chapter10;

import com.quecode.chapter10.model.User;
import com.quecode.chapter10.service.InternalUserService;
import com.quecode.chapter10.service.UserService;
import com.quecode.chapter10.service.UserServiceInFunctionalWay;

import java.util.Arrays;
import java.util.Optional;

public class TemplateMethodPattern {
    public static void main(String[] args) {
        /**
         * 10.5 Template Method Pattern
         */
        User alice = User.builder(1,"Alice")
                .with(builder -> {
                    builder.emailAddress = Optional.of("alice@innotree.com");
                    builder.isVerified = false;
                    builder.friendUserIds = Arrays.asList(201,202,203,204,211,212,213,214);
                }).build();

        UserService userService = new UserService();
        InternalUserService internalUserService = new InternalUserService();

        userService.createUser(alice);
        internalUserService.createUser(alice);

        UserServiceInFunctionalWay userServiceInFunctionalWay = new UserServiceInFunctionalWay(
                user -> {
                    System.out.println("Validating user "+user.getName());
                    return user.getName() != null && user.getEmailAddress().isPresent();
                },
                user -> System.out.println("Cannot create user")
        );
        userServiceInFunctionalWay.createUser(alice);
    }
}
