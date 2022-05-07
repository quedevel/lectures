package com.quecode.chapter10.service;

import com.quecode.chapter10.model.User;

public interface EmailProvider {
    String getEmail(User user);
}
