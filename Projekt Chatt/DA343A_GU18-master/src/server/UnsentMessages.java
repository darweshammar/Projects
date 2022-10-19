package server;

import client.Message;
import client.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class UnsentMessages {

    private HashMap<User, LinkedList<Message>> unsent = new HashMap<>();

    public synchronized void put(User user, LinkedList<Message> messages){
        unsent.put(user, messages);
    }

    public synchronized LinkedList<Message> get(User user){
        return unsent.get(user);
    }
    public synchronized Boolean containsUser(User user) {
        if (unsent.containsKey(user)) {
            return true;
        } else {
            return false;
        }
    }
}
