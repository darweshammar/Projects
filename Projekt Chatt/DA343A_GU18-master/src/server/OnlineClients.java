package server;

import client.User;

import java.util.HashMap;
import java.util.LinkedList;

public class OnlineClients {
    private HashMap<User, Object> clients = new HashMap<>();
    // egna tillägg

    public synchronized void put(User user, Object client) {
        clients.put(user, client);
    }

    public synchronized Object get(User user) {
        return get(user);
    }

    public synchronized Boolean containsUser(User user) {
        if (clients.containsKey(user)) {
            return true;
        } else {
            return false;
        }
    }

    public int size(){

        return clients.size();

    }

    public synchronized void removeUser(User user) {
        clients.remove(user);
    }

    public synchronized LinkedList getOnlineLinkedList (){
        return new LinkedList<>(clients.keySet());
    }

}
