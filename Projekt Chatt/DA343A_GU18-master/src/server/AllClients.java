package server;
import client.User;
import java.util.LinkedList;

public class AllClients {

    private LinkedList<User> clients = new LinkedList<>();

    public synchronized void put(User user) {
        clients.add(user);
    }

    public synchronized User get(User user) {
        return get(user);
    }

    public synchronized Boolean containsUser(User user) {
        if (clients.contains(user)) {
            return true;
        } else {
            return false;
        }
    }
}
