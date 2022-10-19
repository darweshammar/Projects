package GuiServer;

import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


public class ServerGui extends JPanel {
    private JTextArea jTextArea = new JTextArea();
    private JScrollPane jScrollPane = new JScrollPane(jTextArea);
    private Font font = new Font("Helvetika",1,12);
    public ServerGui(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600,600));
        jTextArea.setFont(font);
        jTextArea.setBackground(Color.BLACK);
        jTextArea.setForeground(Color.GREEN);
        add(jScrollPane);

        showPanelinFrame();



    }

    public void showPanelinFrame(){
        JFrame frame = new JFrame("You are in the matrix");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }
    public void append(String text){
       Date date = new Date();
        long getTime = date.getTime();
        Timestamp timestamp = new Timestamp(getTime);
        System.out.println(timestamp);
        jTextArea.append(timestamp + " : " +text + "\n");
    }

    public static void main(String[] args) {
        ServerGui gui = new ServerGui();
        gui.append("bajs");
        gui.append("bajs");
    }
}
