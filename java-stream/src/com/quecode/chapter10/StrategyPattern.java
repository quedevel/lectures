package com.quecode.chapter10;

import com.quecode.chapter10.model.User;
import com.quecode.chapter10.service.EmailProvider;
import com.quecode.chapter10.service.EmailSender;
import com.quecode.chapter10.service.MakeMoreFriendsEmailProvider;
import com.quecode.chapter10.service.VerifyYourEmailAddressEmailProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StrategyPattern {
    public static void main(String[] args) {
        /**
         * 10.4 Strategy Pattern
         */
        User user1 = User.builder(1,"Alice")
                .with(builder -> {
                    builder.emailAddress = Optional.of("alice@innotree.com");
                    builder.isVerified = false;
                    builder.friendUserIds = Arrays.asList(201,202,203,204,211,212,213,214);
                }).build();
        User user2 = User.builder(1,"Bob")
                .with(builder -> {
                    builder.emailAddress = Optional.of("bob@innotree.com");
                    builder.isVerified = true;
                    builder.friendUserIds = Arrays.asList(212,213,214);
                }).build();
        User user3 = User.builder(1,"Charlie")
                .with(builder -> {
                    builder.emailAddress = Optional.of("charlie@innotree.com");
                    builder.isVerified = true;
                    builder.friendUserIds = Arrays.asList(201,202,203,204,211,212);
                }).build();

        List<User> users = Arrays.asList(user1,user2,user3);

        EmailSender emailSender = new EmailSender();
        EmailProvider verify = new VerifyYourEmailAddressEmailProvider();
        EmailProvider make = new MakeMoreFriendsEmailProvider();

        emailSender.setEmailProvider(verify);
        users.stream()
                .filter(user -> !user.isVerified())
                .forEach(emailSender::sendEmail);

        emailSender.setEmailProvider(make);
        users.stream()
                .filter(User::isVerified)
                .filter(user -> user.getFriendUserIds().size() <= 5)
                .forEach(emailSender::sendEmail);

        emailSender.setEmailProvider(user -> "'Play With Friends' email for "+user.getName());
        users.stream()
                .filter(User::isVerified)
                .filter(user -> user.getFriendUserIds().size() > 5)
                .forEach(emailSender::sendEmail);
    }
}
