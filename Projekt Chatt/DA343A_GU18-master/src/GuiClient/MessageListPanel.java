package GuiClient;

import javax.swing.*;
import java.awt.*;

public class MessageListPanel extends JPanel {
    private JList messages ;
    private JScrollPane scrollpane;
    private JLabel jLabel = new JLabel("Messages");
    private ClientGui mainPanel;
    public MessageListPanel(ClientGui mainPanel){
        this.mainPanel = mainPanel;
        setPreferredSize(new Dimension(200,400));
        String[] arr = {""};
        setLayout(new BorderLayout());
        messages = new JList(arr);
        scrollpane = new JScrollPane(messages);
        messages.addListSelectionListener(e -> showMessage());
        //messages.setBackground(Color.LIGHT_GRAY);
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createTitledBorder("Message list"));
        add(jLabel, BorderLayout.NORTH);
        add(scrollpane, BorderLayout.CENTER);
    }
    public void refreshMessageList(String[] arr){
        messages.setListData(arr);
    }
    public int showMessage() {
        return  messages.getSelectedIndex();
    }

}
