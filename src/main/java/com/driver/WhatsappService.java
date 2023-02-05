package com.driver;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class WhatsappService {

    WhatsappRepository whatsappRepository = new WhatsappRepository();

    public String createUser(String name, String mobile) throws Exception {
        String result = whatsappRepository.createUser(name, mobile);
        return result;
    }

    public Group createGroup(List<User> users) {
        Group result = whatsappRepository.createGroup(users);
        return result;
    }

    public int createMessage(String content) {
        int result = whatsappRepository.createMessage(content);
        return result;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        int result = whatsappRepository.sendMessage(message, sender, group);
        return result;
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        String result = whatsappRepository.changeAdmin(approver, user, group);
        return result;
    }

    public int removeUser(User user) throws Exception {
        int result = whatsappRepository.removeUser(user);
        return result;
    }

    public String findMessage(Date start, Date end, int k) throws Exception {
        String result = whatsappRepository.findMessage(start, end, k);
        return result;
    }

}
