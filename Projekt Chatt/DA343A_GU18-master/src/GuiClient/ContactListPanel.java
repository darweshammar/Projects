package GuiClient;

import javax.swing.*;
import java.awt.*;

public class ContactListPanel extends JPanel {
    private String[] contactListArray;
    private String[] onlineListArray = {""};
    private JList<String> contactList;
    private JScrollPane scrollPane;
    private JList jListOnline = new JList(onlineListArray);
    private JScrollPane scrollpaneOnline = new JScrollPane(jListOnline);
    private JLabel jLabelOnlineList = new JLabel("People online");
    private JLabel contactListLabel = new JLabel("Contact list");
    private JLabel jLabelempty = new JLabel();
    private JPanel jPanelcontactList = new JPanel(new BorderLayout());
    private JPanel jPanelOnlineList = new JPanel(new BorderLayout());
    private ClientGui mainPanel;
    public ContactListPanel(ClientGui mainPanel){
        this.mainPanel = mainPanel;
        setPreferredSize(new Dimension(150,600));
        contactListLabel.setPreferredSize(new Dimension(150,20));
        contactListArray = new String[]{""};
        contactList = new JList<String>(contactListArray);
        this.scrollPane  = new JScrollPane(contactList);
        jLabelempty.setPreferredSize(new Dimension(20,30));
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        setBorder(BorderFactory.createBevelBorder(1,Color.BLACK,Color.DARK_GRAY));
        scrollPane.setPreferredSize(new Dimension(150,300));
        scrollpaneOnline.setPreferredSize(new Dimension(150,300));

        jPanelOnlineList.add(jLabelOnlineList,BorderLayout.NORTH);
        jPanelOnlineList.add(scrollpaneOnline);
        jPanelcontactList.add(jLabelempty, BorderLayout.SOUTH);
        jPanelcontactList.add(contactListLabel, BorderLayout.NORTH);
        jPanelcontactList.add(scrollPane, BorderLayout.CENTER);
        jPanelOnlineList.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        jPanelcontactList.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        jPanelcontactList.setBackground(Color.LIGHT_GRAY);
        jPanelOnlineList.setBackground(Color.LIGHT_GRAY);
        add(jPanelcontactList, BorderLayout.CENTER);
        add(jPanelOnlineList, BorderLayout.SOUTH);
        //add(contactListLabel, BorderLayout.NORTH);

    }
    public void refreshOnlineList(String[] onlineList){
        jListOnline.setListData(onlineList);
    }

    public void refreshContactList(String[] contactListArray){
        contactList.setListData(contactListArray);
    }

    public static void main(String[] args) {
        String[] bajs = new String[10];
        for (int i = 0; i<bajs.length; i++){
            bajs[i] = String.valueOf(i);
        }

    }

}

