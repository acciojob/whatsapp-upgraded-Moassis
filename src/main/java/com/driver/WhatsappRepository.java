// package com.driver;

// import java.util.ArrayList;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.List;
// import java.util.ListIterator;

// public class WhatsappRepository {

//     HashMap<String, User> userDb = new HashMap<>(); // <Mobile, User>
//     HashMap<String, Group> groupDb = new HashMap<>(); // <Group Name, Group>
//     HashMap<Integer, Message> messageDb = new HashMap<>(); // <MessageId, Message>
//     int groupCount = 1;
//     int messageCount = 1;

//     public String createUser(String name, String mobile) throws Exception {
//         User user = new User();
//         user.setMobile(mobile);
//         user.setName(name);
//         if (userDb.containsKey(mobile)) {
//             throw new Exception();
//         }
//         userDb.put(mobile, user);
//         return "SUCCESS";
//     }

//     public Group createGroup(List<User> users) {

//         Group group = new Group();
//         group.setAdmin(users.get(0));
//         group.setMembers(users);

//         if (users.size() == 2) {
//             String secondName = users.get(1).getName();
//             group.setName(secondName);
//             group.setNumberOfParticipants(2);
//             groupDb.put(secondName, group);
//         } else if (users.size() > 2) {
//             String name = "Group " + String.valueOf(groupCount);
//             groupCount++;
//             group.setName(name);
//             group.setNumberOfParticipants(users.size());
//             groupDb.put(name, group);
//         }
//         return group;
//     }

//     public int createMessage(String content) {

//         Message message = new Message();
//         int id = messageCount;
//         message.setId(id);
//         messageCount++;
//         message.setContent(content);
//         Date date = new Date();
//         message.setTimestamp(date);

//         messageDb.put(id, message);
//         return id;
//     }

//     public int sendMessage(Message message, User sender, Group group) throws Exception {

//         String name = group.getName();
//         if (!groupDb.containsKey(name)) {
//             throw new Exception("Group does not exist");
//         }
//         List<User> users = group.getMembers();
//         boolean flag = false;
//         for (User user : users) {
//             if (user.getMobile().equals(sender.getMobile())) {
//                 flag = true;
//                 break;
//             }
//         }
//         if (flag == false) {
//             throw new Exception("You are not allowed to send message");
//         }

//         // for messageDb
//         messageDb.put(message.getId(), message);

//         // for userDb
//         List<Message> userMessageList = sender.getMessageList();
//         userMessageList.add(message);
//         sender.setMessageList(userMessageList);
//         userDb.put(sender.getMobile(), sender);

//         // for groupDb
//         List<Message> groupMessageList = group.getMessageList();
//         groupMessageList.add(message);
//         group.setMessageList(groupMessageList);
//         groupDb.put(group.getName(), group);

//         return message.getId();
//     }

//     public String changeAdmin(User approver, User user, Group group) throws Exception {

//         String name = group.getName();
//         if (!groupDb.containsKey(name)) {
//             throw new Exception("Group does not exist");
//         }
//         if (!group.getAdmin().equals(approver)) {
//             throw new Exception("Approver does not have rights");
//         }
//         List<User> users = group.getMembers();
//         boolean flag = false;
//         for (User u : users) {
//             if (u.equals(user)) {
//                 flag = true;
//                 break;
//             }
//         }
//         if (flag == false) {
//             throw new Exception("User is not a participant");
//         }
//         group.setAdmin(user);
//         groupDb.put(name, group);

//         return "SUCCESS";
//     }

//     public int removeUser(User user) throws Exception {

//         Group searchGroup = null;
//         for (Group group : groupDb.values()) {
//             List<User> groupUserList = group.getMembers();
//             for (User member : groupUserList) {
//                 if (member.equals(user)) {
//                     searchGroup = group;
//                     break;
//                 }
//             }
//         }
//         if (searchGroup == null) {
//             throw new Exception("User not found");
//         }

//         if (user.equals(searchGroup.getAdmin())) {
//             throw new Exception("Cannot remove admin");
//         }

//         List<Message> userMessagesList = user.getMessageList();
//         List<Message> groupMessagesList = searchGroup.getMessageList();

//         // Remove messages from messsageDb
//         for (Message m : userMessagesList) {
//             int id = m.getId();
//             if (messageDb.containsKey(id)) {
//                 messageDb.remove(id);
//             }
//         }

//         // Remove messages from groupMessageList
//         for (Message m : userMessagesList) {
//             int id = m.getId();
//             ListIterator<Message> itr = groupMessagesList.listIterator();
//             while (itr.hasNext()) {
//                 if (id == itr.next().getId()) {
//                     itr.remove();
//                     break;
//                 }
//             }
//         }
//         searchGroup.setMessageList(groupMessagesList);
//         groupDb.put(searchGroup.getName(), searchGroup);

//         // Remove user for group list
//         List<User> searchGroupUsers = searchGroup.getMembers();
//         ListIterator<User> itr = searchGroupUsers.listIterator();
//         while (itr.hasNext()) {
//             if (itr.next().equals(user)) {
//                 itr.remove();
//                 break;
//             }
//         }
//         searchGroup.setMembers(searchGroupUsers);
//         searchGroup.setNumberOfParticipants(searchGroupUsers.size());
//         groupDb.put(searchGroup.getName(), searchGroup);

//         // Remove userMessageList from userDb
//         userMessagesList = new ArrayList<>();
//         user.setMessageList(userMessagesList);
//         userDb.put(user.getMobile(), user);

//         // return (updated number of users in the group + the updated number of
//         // messages
//         // in group +
//         // the updated number of overall messages)

//         int ans = searchGroupUsers.size() + searchGroup.getMessageList().size() +
//                 messageDb.size();

//         return ans;

//     }

//     public String findMessage(Date start, Date end, int k) {
//         return null;
//     }

//     public Message getMessage(int id) {
//         return messageDb.get(id);
//     }
// }

package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class WhatsappRepository {

    private int groupCount = 0;

    private int messageCount = 0;

    HashMap<String, User> userHashMap = new HashMap<>(); // key as mobile

    HashMap<Group, List<User>> groupHashMap = new HashMap<>(); // group Name as key

    HashMap<Group, List<Message>> messagesInGroup = new HashMap<>();

    List<Message> messageList = new ArrayList<>();

    HashMap<User, List<Message>> userMessageList = new HashMap<>();

    public String createUser(String name, String mobile) throws Exception {

        if (userHashMap.containsKey(mobile)) {
            throw new Exception("User already exists");
        }
        User user = new User(name, mobile);
        userHashMap.put(mobile, user);
        return "SUCCESS";

    }

    public Group createGroup(List<User> users) {
        if (users.size() == 2) {
            Group group = new Group(users.get(1).getName(), 2);
            groupHashMap.put(group, users);
            return group;
        }
        Group group = new Group("Group " + ++groupCount, users.size());
        groupHashMap.put(group, users);
        return group;
    }

    public int createMessage(String content) {
        Message message = new Message(++messageCount, content);
        message.setTimestamp(new Date());
        messageList.add(message);
        return messageCount;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        // Throw "Group does not exist" if the mentioned group does not exist
        // Throw "You are not allowed to send message" if the sender is not a member of
        // the group
        // If the message is sent successfully, return the final number of messagesin
        // that group.
        if (!groupHashMap.containsKey(group)) {
            throw new Exception("Group does not exist");
        }
        boolean checker = false;
        for (User user : groupHashMap.get(group)) {
            if (user.equals(sender)) {
                checker = true;
                break;
            }
        }

        if (!checker) {
            throw new Exception("You are not allowed to send message");
        }

        // Group List
        if (messagesInGroup.containsKey(group)) {
            messagesInGroup.get(group).add(message);
        } else {
            List<Message> messages = new ArrayList<>();
            messages.add(message);
            messagesInGroup.put(group, messages);
        }

        // User List
        if (userMessageList.containsKey(sender)) {
            userMessageList.get(sender).add(message);
        } else {
            List<Message> messages = new ArrayList<>();
            messages.add(message);
            userMessageList.put(sender, messages);
        }

        return messagesInGroup.get(group).size();

    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        // Throw "Group does not exist" if the mentioned group does not exist
        // Throw "Approver does not have rights" if the approver is not the current
        // admin of the group
        // Throw "User is not a participant" if the user is not a part of the group
        // Change the admin of the group to "user" and return "SUCCESS". Note that at
        // one time there is only one admin and the admin rights are transferred from
        // approver to user.

        if (!groupHashMap.containsKey(group)) {
            throw new Exception("Group does not exist");
        }

        User pastAdmin = groupHashMap.get(group).get(0);
        if (!approver.equals(pastAdmin)) {
            throw new Exception("Approver does not have rights");
        }

        boolean check = false;
        for (User user1 : groupHashMap.get(group)) {
            if (user1.equals(user)) {
                check = true;
            }
        }

        if (!check) {
            throw new Exception("User is not a participant");
        }

        User newAdmin = null;
        Iterator<User> userIterator = groupHashMap.get(group).iterator();

        while (userIterator.hasNext()) {
            User u = userIterator.next();
            if (u.equals(user)) {
                newAdmin = u;
                userIterator.remove();
            }
        }

        groupHashMap.get(group).add(0, newAdmin);
        return "SUCCESS";

    }

    public int removeUser(User user) throws Exception {
        // A user belongs to exactly one group
        // If user is not found in any group, throw "User not found" exception
        // If user is found in a group and it is the admin, throw "Cannot removeadmin"
        // exception
        // If user is not the admin, remove the user from the group, remove all its
        // messages from all the databases, and update relevant attributesaccordingly.
        // If user is removed successfully, return (the updated number of users inthe
        // group + the updated number of messages in group + the updated number of
        // overall messages)

        boolean check = false;
        Group group1 = null;
        for (Group group : groupHashMap.keySet()) {
            for (User user1 : groupHashMap.get(group)) {
                if (user1.equals(user)) {
                    check = true;
                    group1 = group;
                    break;
                }
            }
        }
        if (!check) {
            throw new Exception("User not found");
        }

        if (groupHashMap.get(group1).get(0).equals(user)) {
            throw new Exception("Cannot remove admin");
        }

        List<Message> userMessages = userMessageList.get(user);

        for (Group group : messagesInGroup.keySet()) {
            for (Message message : messagesInGroup.get(group)) {
                if (userMessages.contains(message)) {
                    messagesInGroup.get(group).remove(message);
                }
            }
        }

        for (Message message : messageList) {
            if (userMessages.contains(message)) {
                messageList.remove(message);
            }
        }

        groupHashMap.get(group1).remove(user);

        userMessageList.remove(user);

        return groupHashMap.get(group1).size() + messagesInGroup.get(group1).size() +
                messageList.size();

    }

}
