package GuiClient;

import client.ClientController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientGui extends JPanel {
    private ClientController clientController;
   private  ContactListPanel contactListPanel;
   private MessageListPanel messageListPanel;
   private MessageShowPanel messageShowPanel = new MessageShowPanel(this);
   public ClientGui(ClientController controller){
       this.clientController = controller;

       setLayout(new BorderLayout());
       contactListPanel = new ContactListPanel(this);
       messageListPanel = new MessageListPanel(this);
       setPreferredSize(new Dimension(1000,600));
       messageListPanel.setBorder(BorderFactory.createBevelBorder(1,Color.DARK_GRAY,Color.LIGHT_GRAY));
       setBorder(BorderFactory.createEmptyBorder(7,7,7,7));


       add(messageListPanel, BorderLayout.EAST);
       add(messageShowPanel, BorderLayout.CENTER);
       add(contactListPanel,BorderLayout.WEST);

       showPanelinFrame();
       repaint();
   }
   public void addContact(String Name, int index){
       //todo använd  till det ni skall.



   }
   public void showPicture(ImageIcon icon){
       messageShowPanel.setPicturetoframes(icon);
   }




   public void showPanelinFrame(){
       JFrame frame = new JFrame("Messanger");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.add(this);
       frame.pack();
       frame.setVisible(true);
   }
   public void refreshContactList(String[] contactlist){
       contactListPanel.refreshContactList(contactlist);
   }
   public void refreshMessages(String[] messages){
       messageListPanel.refreshMessageList(messages);
   }
   public void refreshOnlineList(String[] onlineList){
       contactListPanel.refreshOnlineList(onlineList);
   }
   public void refreshAll(String[] onlineList, String[] contactList, String[] Messages){
       refreshMessages(Messages);
       refreshContactList(contactList);
       refreshOnlineList(onlineList);
   }
   public void sendMessage(ImageIcon icon, String message){

   }

    public static void main(String[] args) throws IOException {
        String[] arr = new String[30];
        String[] messages = new String[30];
        for(int i = 0; i<arr.length;i++){
            arr[i] = String.valueOf(i);
        }
        for(int i = 0; i<messages.length;i++){
            messages[i] = String.valueOf(i);
        }
        ClientGui panel = new ClientGui(new ClientController());
        panel.refreshAll(arr,arr,arr);



    }
}
