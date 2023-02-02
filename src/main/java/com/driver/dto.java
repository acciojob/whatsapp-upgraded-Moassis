package com.driver;

public class dto {
    Message message;
    User sender;
    Group group;

    public dto(Message message, User sender, Group group) {
        this.message = message;
        this.sender = sender;
        this.group = group;
    }
}
