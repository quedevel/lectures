package com.quecode.chapter10.service;

import com.quecode.chapter10.model.User;

public abstract class AbstractUserService {

    protected abstract boolean validateUser(User user);

    protected abstract void writeToDB(User user);

    public void createUser(User user){
        if(validateUser(user)){
            writeToDB(user);
        } else {
            System.out.println("Cannot create user");
        }
    }
}
