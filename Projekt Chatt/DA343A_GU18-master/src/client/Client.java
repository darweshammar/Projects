package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Client {
    private ClientController controller;
    private Socket socket;
    private String ip;

    ObjectOutputStream oos;
    ObjectInputStream ois;

    public Client(String ip, User user, ClientController controller) {

        this.controller = controller;
    }

    public Client(String ip, ClientController controller){
        this.controller = controller;
        this.ip = ip;


    }

    public void startReceiver(){

        new Reciever().start();
    }
    public void connect(){
       try {
           socket = new Socket(ip, 4999);
           oos = new ObjectOutputStream(socket.getOutputStream());
           ois = new ObjectInputStream(socket.getInputStream());
       } catch (IOException e) {}

    }

    public void logIn(MessageLogIn logInMessage){
        try{

            oos.writeObject(logInMessage);
            oos.flush();

        }catch (IOException e) {}
    }


    public void authenticate(User user){
        try {

            oos.writeObject(user);
            oos.flush();

        }catch (IOException e){}

    }

    public void send(Message message){
        try {

            oos.writeObject(message);
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){

        try {
            oos.writeObject(new MessageLogOut());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    private class Reciever extends Thread{
        @Override
        public void run() {



            while (true){

                try {

                    Object object = ois.readObject();

                    if(object instanceof Message){
                        controller.storeMessage((Message) object);

                        //System.out.println(((Message) object).getMessageText());

                    } else if (object instanceof LinkedList){
                        controller.setOnlineList((LinkedList<User>) ois.readObject());
                    } else if ( object instanceof User){
                        controller.setUser((User) object);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
