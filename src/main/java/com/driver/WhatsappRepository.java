package com.driver;

import java.util.*;

public class WhatsappRepository {

    HashMap<String, User> userDb = new HashMap<>(); // <Mobile, User>
    HashMap<String, Group> groupDb = new HashMap<>(); // <Group Name, Group>
    HashMap<Integer, Message> messageDb = new HashMap<>(); // <MessageId, Message>
    int groupCount = 1;
    int messageCount = 1;

    public String createUser(String name, String mobile) throws Exception {
        User user = new User();
        user.setMobile(mobile);
        user.setName(name);
        if (userDb.containsKey(mobile)) {
            throw new Exception();
        }
        userDb.put(mobile, user);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {

        Group group = new Group();
        group.setAdmin(users.get(0));
        group.setMembers(users);

        if (users.size() == 2) {
            String secondName = users.get(1).getName();
            group.setName(secondName);
            group.setNumberOfParticipants(2);
            groupDb.put(secondName, group);
        } else if (users.size() > 2) {
            String name = "Group " + String.valueOf(groupCount);
            groupCount++;
            group.setName(name);
            group.setNumberOfParticipants(users.size());
            groupDb.put(name, group);
        }
        return group;
    }

    public int createMessage(String content) {

        Message message = new Message();
        int id = messageCount;
        message.setId(id);
        messageCount++;
        message.setContent(content);
        Date date = new Date();
        message.setTimestamp(date);

        messageDb.put(id, message);
        return id;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {

        String name = group.getName();
        if (!groupDb.containsKey(name)) {
            throw new Exception("Group does not exist");
        }
        List<User> users = group.getMembers();
        boolean flag = false;
        for (User user : users) {
            if (user.getMobile().equals(sender.getMobile())) {
                flag = true;
                break;
            }
        }
        if (flag == false) {
            throw new Exception("You are not allowed to send message");
        }

        // for messageDb
        messageDb.put(message.getId(), message);

        // for userDb
        List<Message> userMessageList = sender.getMessageList();
        userMessageList.add(message);
        sender.setMessageList(userMessageList);
        userDb.put(sender.getMobile(), sender);

        // for groupDb
        List<Message> groupMessageList = group.getMessageList();
        groupMessageList.add(message);
        group.setMessageList(groupMessageList);
        groupDb.put(group.getName(), group);

        return message.getId();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {

        String name = group.getName();
        if (!groupDb.containsKey(name)) {
            throw new Exception("Group does not exist");
        }
        if (!group.getAdmin().equals(approver)) {
            throw new Exception("Approver does not have rights");
        }
        List<User> users = group.getMembers();
        boolean flag = false;
        for (User u : users) {
            if (u.equals(user)) {
                flag = true;
                break;
            }
        }
        if (flag == false) {
            throw new Exception("User is not a participant");
        }
        group.setAdmin(user);
        groupDb.put(name, group);

        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception {

        Group searchGroup = null;
        for (Group group : groupDb.values()) {
            List<User> groupUserList = group.getMembers();
            for (User member : groupUserList) {
                if (member.equals(user)) {
                    searchGroup = group;
                    break;
                }
            }
        }
        if (searchGroup == null) {
            throw new Exception("User not found");
        }

        if (user.equals(searchGroup.getAdmin())) {
            throw new Exception("Cannot remove admin");
        }

        List<Message> userMessagesList = user.getMessageList();
        List<Message> groupMessagesList = searchGroup.getMessageList();

        // Remove messages from messsageDb
        for (Message m : userMessagesList) {
            int id = m.getId();
            if (messageDb.containsKey(id)) {
                messageDb.remove(id);
            }
        }

        // Remove messages from groupMessageList
        for (Message m : userMessagesList) {
            int id = m.getId();
            ListIterator<Message> itr = groupMessagesList.listIterator();
            while (itr.hasNext()) {
                if (id == itr.next().getId()) {
                    itr.remove();
                    break;
                }
            }
        }
        searchGroup.setMessageList(groupMessagesList);
        groupDb.put(searchGroup.getName(), searchGroup);

        // Remove user for group list
        List<User> searchGroupUsers = searchGroup.getMembers();
        ListIterator<User> itr = searchGroupUsers.listIterator();
        while (itr.hasNext()) {
            if (itr.next().equals(user)) {
                itr.remove();
                break;
            }
        }
        searchGroup.setMembers(searchGroupUsers);
        searchGroup.setNumberOfParticipants(searchGroupUsers.size());
        groupDb.put(searchGroup.getName(), searchGroup);

        // Remove userMessageList from userDb
        userMessagesList = new ArrayList<>();
        user.setMessageList(userMessagesList);
        userDb.put(user.getMobile(), user);

        // return (updated number of users in the group + the updated number of
        // messages in group + the updated number of overall messages)

        int ans = searchGroupUsers.size() + searchGroup.getMessageList().size() +
                messageDb.size();

        return ans;
    }

    public String findMessage(Date start, Date end, int k) {
        return null;
    }
}