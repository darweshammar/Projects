package globalClient;

import globalServer.Logger;
import model.Message;
import model.SecurityComponent;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**@author Ammar Darwesh, Malek Abdul Sater  @coauthor**/
public class GlobalClient {

    private Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Receiver receiver;

    private GlobalClientController globalClientController;

    public GlobalClient(String ip, int port, GlobalClientController globalClientController) {
        this.ip = ip;
        this.port = port;
        this.globalClientController = globalClientController;
        connect();
    }


    public void connect() {
        try {
            socket = new Socket(ip, port);
            System.out.println("You're connected");
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            receiver = new Receiver();
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection faulty in client");
            //receiver.interrupt();
            receiver = null;
            connect();
        }
    }

    public void closeSocket() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void send(Object obj) {
        try {
            oos.writeObject(obj);
            oos.flush();
            System.out.println("You have sent: " + obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Receiver extends Thread {

        @Override
        public void run() {
            try {
                oos.writeObject("globalClient");
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    Object objectRead = ois.readObject();

                    if (objectRead instanceof String) {
                        System.out.println("You have received: " + objectRead);

                        if (objectRead.equals("user authenticated")) {
                            globalClientController.authenticateUser();
                        } else if (objectRead.equals("user unauthenticated")) {
                            JOptionPane.showMessageDialog(null, "username or password are incorrect");
                        } else if (objectRead.equals("local server offline")){
                            globalClientController.clearList();
                        }

                    } else if (objectRead instanceof Logger) {
                        globalClientController.setLogger((Logger) objectRead);

                    } else if (objectRead instanceof ImageIcon) {
                        globalClientController.addImage((ImageIcon) objectRead);

                    } else if(objectRead instanceof Message){
                        Message msg = (Message) objectRead;
                        ArrayList<SecurityComponent> onlineSensor = msg.getOnlineSensors();
                        ArrayList<SecurityComponent> offlineSensor = msg.getOfflineSensors();
                        globalClientController.setOnlinesensor(onlineSensor);
                        globalClientController.setOfflinesensor(offlineSensor);
                    }

                } catch (IOException | ClassNotFoundException e) {
                    break;
                }
            }
        }
    }
}




