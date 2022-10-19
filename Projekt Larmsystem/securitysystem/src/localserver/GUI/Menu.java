package localserver.GUI;


import model.SecurityComponent;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**@author Olof Persson  @coauthor Per Blomqvist, Jens Moths**/
public class Meny extends JFrame {


    JButton btnOpenDoor = new JButton("Unlock");
    JButton btnCloseDoor = new JButton("Lock");
    JButton btnAlarmOn = new JButton("Alarm");
    JButton btnChangeCode = new JButton("Change PIN code");
    JButton btnFingerprint = new JButton("Fingerprint Reader");
    public JButton btnGoOnline = new JButton("Go online");

    JList tfonlineMK = new JList();
    JList tfofflineMK = new JList();
    DefaultListModel OnlineListModel = new DefaultListModel();
    DefaultListModel OfflineListModel = new DefaultListModel();
    private MainFrame mainFrame;
    private ChangeCode changeCode;
    private FingerprintGui fingerprintGui;



    public Menu(MainFrame mainFrame, ChangeCode changeCode, FingerprintGui fingerprintGui){
        this.mainFrame = mainFrame;
        this.changeCode = changeCode;
        this.fingerprintGui = fingerprintGui;

        this.setBackground(new Color(83,86,91));
        getContentPane().setBackground(new Color(83,86,91));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setUndecorated(true);


        tfonlineMK .setBackground(new Color(43, 43, 43));
        tfofflineMK .setBackground(new Color(43, 43, 43));


        this.setLayout(new MigLayout());
        //setSize(600,1000);
        setVisible(true);
        //setLocation(500,200);

        DrawMeny();

    }

    public void DrawMeny(){

        btnOpenDoor.addActionListener(new ButtonListener());
        btnCloseDoor.addActionListener(new ButtonListener());
        btnAlarmOn.addActionListener(new ButtonListener());
        btnChangeCode.addActionListener(new ButtonListener());
        btnFingerprint.addActionListener(new ButtonListener());
        btnGoOnline.addActionListener(new ButtonListener());

        tfofflineMK.setPreferredSize(new Dimension(400,400));
        tfonlineMK.setPreferredSize(new Dimension(400, 400));

        btnOpenDoor.setPreferredSize(new Dimension(90,90));
        btnCloseDoor.setPreferredSize(new Dimension(90,90));
        btnAlarmOn.setPreferredSize(new Dimension(90,90));
        btnChangeCode.setPreferredSize(new Dimension(90,90));
        btnFingerprint.setPreferredSize(new Dimension(90,90));
        btnGoOnline.setPreferredSize(new Dimension(90,90));

        btnOpenDoor.setBackground(new Color(60, 63, 65));
        btnCloseDoor.setBackground(new Color(60, 63, 65));
        btnAlarmOn.setBackground(new Color(60, 63, 65));
        btnChangeCode.setBackground(new Color(60, 63, 65));
        btnFingerprint.setBackground(new Color(60, 63, 65));
        btnGoOnline.setBackground(new Color(60, 63, 65));

        btnOpenDoor.setForeground(Color.white);
        btnCloseDoor.setForeground(Color.white);
        btnAlarmOn.setForeground(Color.white);
        btnChangeCode.setForeground(Color.white);
        btnFingerprint.setForeground(Color.white);
        btnGoOnline.setForeground(Color.white);
        tfofflineMK.setForeground(Color.white);
        tfonlineMK.setForeground(Color.white);
        add(tfonlineMK,"span");
        add(tfofflineMK,"wrap, span");
        add(btnOpenDoor);
        add(btnCloseDoor);
        add(btnAlarmOn);
        add(btnChangeCode);
        add(btnFingerprint);
        add(btnGoOnline);
        Color color;
        TitledBorder offlineBorder = new TitledBorder(BorderFactory.createEtchedBorder(new Color(62, 134, 160), new Color(62, 134, 160)), "Offline");
        TitledBorder onlineBorder = new TitledBorder(BorderFactory.createEtchedBorder(new Color(62, 134, 160), new Color(62, 134, 160)), "Online");
        offlineBorder.setTitleColor(new Color(62, 134, 160));
        onlineBorder.setTitleColor(new Color(62, 134, 160));
        tfofflineMK.setBorder(offlineBorder);
        tfonlineMK.setBorder(onlineBorder);


    }

    public void updateOnlineMK(ArrayList<SecurityComponent> sensors) {
        OnlineListModel.clear();

        for (SecurityComponent s: sensors) {

            OnlineListModel.addElement(s);
        }
        tfonlineMK.setModel(OnlineListModel);
        tfonlineMK.repaint();


    }
    public void updateOfflineMK(ArrayList<SecurityComponent> sensor){
        OfflineListModel.clear();


        for (SecurityComponent s: sensor){

            OfflineListModel.addElement(s);
        }
        tfofflineMK.setModel(OfflineListModel);
        tfofflineMK.repaint();
    }




    private class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnOpenDoor){
                System.out.println("Dörr öppen");
                mainFrame.controller.setDoorOpen(true);
            }
            if (e.getSource() == btnCloseDoor){
                System.out.println("Dörr stängd");
                mainFrame.controller.setDoorOpen(false);
            }
            if(e.getSource() == btnAlarmOn){
                System.out.println("Larmet är på");
                mainFrame.controller.setAlarmOn(true);
            }
            if (e.getSource() == btnChangeCode){
                mainFrame.cc = new ChangeCode(mainFrame);
                //JFrame ChangeCode = new JFrame();
                //ChangeCode.setSize(new Dimension(320, 420));
                //ChangeCode.setTitle("Ändra kod");
                //ChangeCode.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
            if(e.getSource() == btnFingerprint){
                try {
                    mainFrame.controller.getFingerAmount();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                fingerprintGui.setVisible(true);
            }
            if(e.getSource() == btnGoOnline)
            {
                mainFrame.controller.connectToGlobal();
            }

        }
    }
}

