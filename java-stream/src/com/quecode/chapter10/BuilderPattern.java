package com.quecode.chapter10;

import com.quecode.chapter10.model.User;

import java.util.Optional;

public class BuilderPattern {
    public static void main(String[] args) {
        /**
         * 10.2 Builder Pattern
         */
        //User user = User.builder(1,"Alice").withEmailAddress(Optional.of("alice@innotree.com")).withVerified(true).build();
        User user = User.builder(1,"Alice")
                .with(builder -> {
                    builder.emailAddress = Optional.of("alice@innotree.com");
                    builder.isVerified = true;
                }).build();

        System.out.println("user = " + user);
    }
}
