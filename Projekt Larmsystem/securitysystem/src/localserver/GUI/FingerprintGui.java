package localserver.GUI;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

/**@author Per Blomqvist, Jens Moths  @coauthor**/
public class FingerprintGui extends JFrame implements ActionListener {
    private MainFrame mainFrame;
    private JFrame fingerFrame;
    private JButton add, delete, clear, returnbtn;
    private JPanel panel;

    private JLabel fingersLabel;


    private Font font = new Font("Courier", Font.BOLD, 30);
    public FingerprintGui(MainFrame mainFrame) throws ParseException, IOException {


        this.mainFrame = mainFrame;

        fingerFrame = new JFrame();
        setTitle("Fingerprint Scanner");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setBackground(new Color(62, 134, 160));
        fingerFrame.setBackground(new Color(62, 134, 160));
        draw();

    }

    public void draw() throws ParseException {
        panel = new JPanel(new GridLayout(5, 1));
        panel.setBackground(new Color(62, 134, 160));
        fingersLabel = new JLabel("Waiting for fingers", SwingConstants.CENTER);
        add = new JButton("Add a finger");
        delete = new JButton("Delete the latest used finger");
        clear = new JButton("Clear");
        returnbtn = new JButton("Return");
        add.setBackground(new Color(60, 63, 65));
        delete.setBackground(new Color(60, 63, 65));
        clear.setBackground(new Color(60, 63, 65));
        fingersLabel.setOpaque(true);
        fingersLabel.setBackground(new Color(62, 134, 160));
        returnbtn.setBackground(new Color(60, 63, 65));

        add.setFocusPainted(false);
        delete.setFocusPainted(false);
        clear.setFocusPainted(false);
        returnbtn.setFocusPainted(false);


        add.setForeground(Color.WHITE);
        delete.setForeground(Color.WHITE);
        clear.setForeground(Color.WHITE);
        fingersLabel.setForeground(Color.WHITE);
        returnbtn.setForeground(Color.WHITE);
        add.addActionListener(this::actionPerformed);
        delete.addActionListener(this::actionPerformed);
        clear.addActionListener(this::actionPerformed);
        returnbtn.addActionListener(this::actionPerformed);
        fingersLabel.setFont(font);
        add.setFont(font);
        delete.setFont(font);
        clear.setFont(font);
        returnbtn.setFont(font);


        this.add(panel);
        panel.add(fingersLabel);
        panel.add(add);
        panel.add(delete);
        panel.add(clear);
        panel.add(returnbtn);
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == add) {
            try {
                mainFrame.controller.sendToMK('a');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (actionEvent.getSource() == delete) {
            try {

                mainFrame.controller.sendToMK('d');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (actionEvent.getSource() == clear) {
            try {
                mainFrame.controller.sendToMK('e');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (actionEvent.getSource() == returnbtn) {
            setVisible(false);
        }
    }

    public void setFingersAmount(int fingersAmount) {
        fingersLabel.setText("You have " + fingersAmount + " authorised fingers");
    }
}

