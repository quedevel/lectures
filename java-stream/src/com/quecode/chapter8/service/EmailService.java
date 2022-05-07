package com.quecode.chapter8.service;

import com.quecode.chapter6.model.User;

public class EmailService {

    public void sendPlayWithFriendsEmail(User user){
        user.getEmailAddress().ifPresent(s -> System.out.println("Sending 'Play With Friends' email to " + s));
    }

    public static void sendMakeMoreFriendsEmail(User user){
        user.getEmailAddress().ifPresent(s -> System.out.println("Sending 'Mak More Friends' email to " + s));
    }
}
