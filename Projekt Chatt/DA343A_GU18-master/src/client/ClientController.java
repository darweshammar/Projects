package client;

import GuiClient.ClientGui;

import javax.swing.*;
import java.util.LinkedList;

public class ClientController {
    private Client client;
    private User user;
    private MessageRegister register = new MessageRegister();
    private ClientGui gui;



  //  public ClientController(){
     //   client = new Client("10.2.12.10",new User("Thomas",new ImageIcon("images/gubbe.jpg")));

    //}

    public ClientController(){
        new loginGUI(this);
    }

    public User setOnlineList(LinkedList<User> onlineList){

        String[] onlineArray = new String[onlineList.size()];

        for ( int i = 0; i < onlineList.size() ; i++){
            onlineArray[i] = onlineList.get(i).getName();
        }
        return onlineList.get(0);
    }

    public void storeMessage(Message message){


        register.storeMessage(message);
        gui.updateMessages(register.getMessageList());
    }

    public void send(Message message){
        client.send(message);
    }

    public void disconnect(){ client.disconnect();
    }

    public void updateMessageView(String[] messageList ){
        //todo
      //  view.setMessageList(messageList);
    }

    public void messageReceived(Message message){

        register.storeMessage(message);
        updateMessageView(register.getMessageList());

    }

    public void login(String name, String ip){

        client = new Client(ip,this);
        client.connect();
        client.logIn(new MessageLogIn(name));
        startGui();
        client.startReceiver();



    }

    public void setUser(User user) {
        this.user = user;
    }

    public void register(String name, String ip, ImageIcon icon){

        user = new User(name,icon);

        client = new Client(ip,user ,this);
        client.connect();
        client.authenticate(user);
        startGui();
        client.startReceiver();

    }

    public void startGui(){
        gui = new GUI();
    }

    public static void main(String[] args) {
        ClientController controller = new ClientController();
    }



    }

