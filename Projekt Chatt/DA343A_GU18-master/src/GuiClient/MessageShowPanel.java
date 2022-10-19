package GuiClient;

import javax.swing.*;
import java.awt.*;

public class MessageShowPanel extends JPanel {
    private JLabel jLabelSenderinfo = new JLabel("Daniel - Klockan 13:37");
    private ImageIcon iconPicture = new ImageIcon("images/nopic.png");
    private JLabel jLabelPicture = new JLabel(iconPicture);
    private JPanel panelNorth = new JPanel(new BorderLayout());
    private JPanel panelCenter = new JPanel(new BorderLayout());
    private JPanel panelSouth = new JPanel(new BorderLayout());
    private JPanel panelNorthNorth = new JPanel(new BorderLayout());
    private JPanel panelSoutSouth = new JPanel(new GridLayout(1,3));

    private JButton buttonAdd = new JButton("Add Contact");
    private JButton buttondel = new JButton("Delete Contact");
    private JButton buttonNewMessage = new JButton("New message");
    private JTextArea jTextAreaMessage = new JTextArea();
    private JScrollPane jScrollPane = new JScrollPane(jTextAreaMessage);
    private ClientGui panel;


    public MessageShowPanel(ClientGui panel){
        this.panel=panel;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(380,600));
        setBorder(BorderFactory.createLineBorder(Color.black));
        panelSoutSouth.setPreferredSize(new Dimension(50,50));
        jLabelSenderinfo.setPreferredSize(new Dimension(1,20));
        jScrollPane.setPreferredSize(new Dimension(1,100));
        panelNorth.setBorder(BorderFactory.createTitledBorder("Sänt av :"));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(7,7,7,7));
        panelSouth.setBorder(BorderFactory.createEmptyBorder(7,7,7,7));
        panelNorthNorth.setBorder(BorderFactory.createBevelBorder(1,Color.DARK_GRAY,Color.LIGHT_GRAY));
        panelNorth.setBackground(Color.LIGHT_GRAY);
        panelCenter.setBackground(Color.LIGHT_GRAY);
        panelSouth.setBackground(Color.LIGHT_GRAY);
      //  jTextAreaMessage.setBackground(Color.lightGray);
        jTextAreaMessage.setEditable(false);
        panelSoutSouth.setBorder(BorderFactory.createBevelBorder(1,Color.DARK_GRAY,Color.LIGHT_GRAY));
        panelSoutSouth.add(buttonAdd);
        panelSoutSouth.add(buttondel);
        panelSoutSouth.add(buttonNewMessage);
        panelSouth.add(jScrollPane);

        panelNorthNorth.add(panelNorth, BorderLayout.NORTH);
        panelNorthNorth.add(panelCenter, BorderLayout.CENTER);
        panelNorthNorth.add(panelSouth, BorderLayout.SOUTH);
        panelCenter.add(jLabelPicture);
        panelNorth.add(jLabelSenderinfo);


        add(panelNorthNorth, BorderLayout.CENTER);
        add(panelSoutSouth, BorderLayout.SOUTH);

        //ACTION MF LISTENERS
        buttonAdd.addActionListener(e -> addContact());
        buttonNewMessage.addActionListener(e -> newMessage());


    }
    public void sendMessage(ImageIcon icon, String message){
        panel.sendMessage(icon,message);
    }
    public void newMessage(){
        WriteMessagePanel writeMessagePanel = new WriteMessagePanel("daniel",this); // fixa
        writeMessagePanel.showPanelinFrame();

    }

    public void addContact(){

    }
    public void setPicturetoframes(ImageIcon icon){
        jLabelPicture.setIcon(icon);
    }

}
