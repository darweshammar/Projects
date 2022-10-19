package client;

import java.io.Serializable;
import java.util.LinkedList;

public class MessageOnlineList extends Message implements Serializable {

    private LinkedList<User> onlineList;

    public MessageOnlineList(LinkedList<User> onlineList) {
        this.onlineList = onlineList;
    }
}
