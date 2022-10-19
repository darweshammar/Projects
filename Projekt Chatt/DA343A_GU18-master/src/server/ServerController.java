package server;

import client.Client;
import client.Message;
import client.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class ServerController {

    private AllClients allClients;
    private OnlineClients onlineClients;
    private UnsentMessages unsentMessages;

    public ServerController(int port) {

        new Connection(port).start();

        allClients = new AllClients();
        onlineClients = new OnlineClients();
        unsentMessages = new UnsentMessages();

    }

    private class Connection extends Thread {

        private int port;

        public Connection(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            Socket socket;
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    socket = serverSocket.accept();
                    System.out.println("Ansluten");
                    ClientHandler clientHandler = new ClientHandler(socket);
                    clientHandler.start();
                    //clientHandler.sendOnlineList();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler extends Thread {

        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream dos;
        private LinkedList<Message> pendingMessages = new LinkedList<>();
        private User user;
        private Message message;
        private Object readObject;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            System.out.println("ClientHandler running");

            try {
                ois = new ObjectInputStream(socket.getInputStream());
                dos = new ObjectOutputStream(socket.getOutputStream());
/*
                User user = (User) ois.readObject();
                System.out.println(user.getName());
                dos.writeObject(new Message("bajs"));

 */

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Clienhandler is done");
        }

        public void sendMessage(Message message) {

            try {
                User recipient = message.getRecipient();

                if (onlineClients.containsUser(recipient)) {

                    ClientHandler recipientClient = (ClientHandler) onlineClients.get(recipient);
                    recipientClient.sendMessage(message); //hmmmmm kommer detta verkligen att fungera?...
                    dos.writeObject(message);
                    dos.flush();

                }
                //if person isn't online, store messages in unsent messages.
                else if (!onlineClients.containsUser(message.getRecipient()) && (allClients.containsUser(user))) {

                    pendingMessages.add(message);
                    unsentMessages.put(message.getRecipient(), pendingMessages);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendOnlineList() {
            try {

                onlineClients.getOnlineLinkedList();
                dos.writeObject(onlineClients.getOnlineLinkedList());
                dos.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendUnsentMessages(User user) {
            LinkedList<Message> list = unsentMessages.get(user);
            Message message;
            if (unsentMessages.containsUser(user)) {
                for (int i = 0; i < list.size(); i++) {
                    message = list.get(i);
                    try {
                        dos.writeObject(message);
                        dos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void run() {

            try {
                readObject = ois.readObject();
                System.out.println(readObject.toString());
            } catch (IOException | ClassNotFoundException e) {
                //e.printStackTrace();
            }

            if (readObject instanceof User) {
                user = (User) readObject;
            }

            if (!onlineClients.containsUser(user)) {

                onlineClients.put(user, ClientHandler.this);
                System.out.println(onlineClients.size());

            }


            if (!allClients.containsUser(user)) {
                allClients.put(user);
            }
            sendOnlineList();
            sendUnsentMessages(user);


            while (true) {

                //Deletes user from online clients if object read is user
                if (readObject instanceof Message) {
                    message = (Message) readObject;
                } else if (readObject instanceof User) {

                    user = (User) readObject;
                    onlineClients.removeUser(user);

                    sendOnlineList();

                }


                sendMessage(message);
            }
        }
    }

    public static void main(String[] args) {
        new ServerController(4999);
    }
}



