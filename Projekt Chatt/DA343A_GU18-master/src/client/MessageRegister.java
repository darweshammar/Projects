package client;

import java.util.LinkedList;

public class MessageRegister {

    private LinkedList<Message> messages = new LinkedList<>();

    public void storeMessage(Message message){
        messages.addLast(message);
        //view.setMessageList(updateMessageList());
    }

    public String[] getMessageList(){
        String[] messageList = new String[messages.size()];

        for(int i = 0; i < messages.size(); i++){
            String str = "From: " + messages.get(i).getSender() +
                    "Time sent: " + messages.get(i).getTimeSent().getTime();
            messageList[i] = str;
        }

        return messageList;
    }

}
