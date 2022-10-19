package globalServer;

import model.Message;
import model.SecurityComponent;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**@author Ammar Darwesh, Malek Abdul Sater  @coauthor**/
public class Home implements Serializable {
    User user;
    GlobalServer.ClientHandler localServer;
    LinkedList<GlobalServer.ClientHandler> globalClients;
    Logger logger;
    ArrayList<SecurityComponent> online;
    ArrayList<SecurityComponent> offline;

    public Home(User user) {
        globalClients = new LinkedList<>();
        logger = new Logger();
        logger.createLogger(user.getUserName());
        online = new ArrayList<>();
        offline = new ArrayList<>();
        this.user = user;
    }

    public void addGlobalClient(GlobalServer.ClientHandler globalClient) {
        globalClients.add(globalClient);
    }

    public void removeGlobalClient(GlobalServer.ClientHandler globalClient) {
        globalClients.remove(globalClient);
    }

    public void setLocalServer(GlobalServer.ClientHandler localServer) {
        this.localServer = localServer;
    }

    public void sendToAllClients(Object object) {
        GlobalServer.ClientHandler client;
        if (globalClients.size() > 0) {
            try {
                for (GlobalServer.ClientHandler globalClient : globalClients) {
                    client = globalClient;
                    client.getOos().writeObject(object);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public GlobalServer.ClientHandler getLocalServer() {
        return localServer;
    }

    public void setClientHandler(GlobalServer.ClientHandler clientHandler) {
        if (clientHandler.getServerOrClient().equals("server")) {
            localServer = clientHandler;
        } else {
            globalClients.add(clientHandler);
        }
    }

    public User getUser() {
        return user;
    }

    public GlobalServer.ClientHandler getGlobalClient(GlobalServer.ClientHandler clientHandler) {
        if (globalClients.get(globalClients.indexOf(clientHandler)) == null) {
            return null;
        } else {
            return globalClients.get(globalClients.indexOf(clientHandler));
        }
    }

    public void sendOnlineOfflineLists() {
            Message message = new Message();
            message.setOnlineSensors(online);
            message.setOfflineSensors(offline);
            sendToAllClients(message);
    }

    public void setOffline(ArrayList<SecurityComponent> offline) {
        this.offline = offline;
    }

    public void setOnline(ArrayList<SecurityComponent> online) {
        this.online = online;
    }

    public ArrayList<SecurityComponent> getOffline() {
        return offline;
    }

    public ArrayList<SecurityComponent> getOnline() {
        return online;
    }
}
