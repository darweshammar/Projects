package globalClient;

import model.Message;
import model.SecurityComponent;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**@author Ammar Darwesh, Malek Abdul Sater  @coauthor Per Blomqvist**/
public class MainPanel extends JPanel {

    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    private JPanel leftPanelNorth;
    private JPanel leftPanelSouth;
    private JPanel leftPanelCenter;
    private JPanel centerPanelNorth;
    private JPanel centerPanelCenter;
    private JPanel centerPanelSouth;
    private JPanel pnlDateTime;

    //private JButton btnON;
    //private JButton btnOFF;
    private JButton btnLock;
    private JButton btnUnlock;

    private JButton btnOk;
    private JTextField textFieldPopup;
    private String location;
    private SecurityComponent chosenSensor;
    private JPopupMenu popup;
    private JPanel popupPanel;

    private JTextArea taLogger;
    private JList taOnline;
    private JList taOffline;
    private JList listImages;
    private DefaultListModel defaultListModelImages;
    private DefaultListModel dlmOnline;
    private DefaultListModel dlmOffline;

    private GlobalClientController globalClientController;

    private JFrame frame;

    private JScrollPane scrollPaneLogger;
    private JScrollPane scrollPaneOnline;
    private JScrollPane scrollPaneOffline;
    private JScrollPane scrollPanePictures;

    private JButton btnFilter;
    private JButton btnPhoto;
    private JButton btnLocation;
    private JTextField tfStartDate;
    private JTextField tfEndDate;
    private JTextField tfStartTime;
    private JTextField tfEndTime;
    private ButtonListener buttonListener = new ButtonListener();

    public MainPanel(GlobalClientController globalClientController) {
        this.globalClientController = globalClientController;
        frame = new JFrame();
        draw();
    }

    public void drawPnlDateTime() {
        Dimension tfDimension = new Dimension(125, 35);
        btnFilter = new JButton("Filter");
        btnFilter.addActionListener(buttonListener);
        pnlDateTime.setPreferredSize(new Dimension(600, 45));
        tfStartDate = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        tfStartTime = new JTextField(new SimpleDateFormat("HH-mm").format(Calendar.getInstance().getTime()));
        tfEndDate = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        tfEndTime = new JTextField(new SimpleDateFormat("HH-mm").format(Calendar.getInstance().getTime()));

        tfStartDate.setPreferredSize(tfDimension);
        tfStartTime.setPreferredSize(tfDimension);
        tfEndDate.setPreferredSize(tfDimension);
        tfEndTime.setPreferredSize(tfDimension);
        btnFilter.setPreferredSize(new Dimension(70, 35));

        tfStartDate.setBorder(BorderFactory.createTitledBorder("From: yyyy-mm-dd"));
        tfStartTime.setBorder(BorderFactory.createTitledBorder("From: HH-mm"));
        tfEndDate.setBorder(BorderFactory.createTitledBorder("To: yyyy-mm-dd"));
        tfEndTime.setBorder(BorderFactory.createTitledBorder("To: HH-mm"));

        pnlDateTime.add(tfStartDate);
        pnlDateTime.add(tfStartTime);
        pnlDateTime.add(tfEndDate);
        pnlDateTime.add(tfEndTime);
        pnlDateTime.add(btnFilter);

        rightPanel.add(BorderLayout.CENTER, pnlDateTime);
    }

    public void draw() {

        this.setPreferredSize(new Dimension(1435, 630));
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(60, 63, 65));
        LineBorder border = new LineBorder(new Color(184, 207, 229));
        this.setBorder(border);

        Dimension btnDimension = new Dimension(200, 20);

        leftPanel = new JPanel();
        centerPanel = new JPanel();
        rightPanel = new JPanel();
        leftPanelNorth = new JPanel();
        leftPanelSouth = new JPanel();
        leftPanelCenter = new JPanel();
        centerPanelNorth = new JPanel();
        centerPanelCenter = new JPanel();
        centerPanelSouth = new JPanel();
        pnlDateTime = new JPanel();
        drawPnlDateTime();
        taLogger = new JTextArea();
        dlmOffline = new DefaultListModel();
        dlmOnline = new DefaultListModel();
        defaultListModelImages = new DefaultListModel();
        taOffline = new JList(dlmOffline);
        taOnline = new JList(dlmOnline);
        listImages = new JList(defaultListModelImages);

        taOnline.setForeground(Color.white);
        taOffline.setForeground(Color.white);
        taLogger.setForeground(Color.white);
        listImages.setForeground(Color.white);

        leftPanel.setPreferredSize(new Dimension(280, 300));
        centerPanel.setPreferredSize(new Dimension(430, 600));
        rightPanel.setPreferredSize(new Dimension(630, 600));

        leftPanelSouth.setPreferredSize(new Dimension(260, 80));
        leftPanelNorth.setPreferredSize(new Dimension(260, 80));
        leftPanelCenter.setPreferredSize(new Dimension(260, 300));

        centerPanelCenter.setPreferredSize(new Dimension(420, 300));
        centerPanelNorth.setPreferredSize(new Dimension(420, 300));
        centerPanelSouth.setPreferredSize(new Dimension(840, 660));

        taLogger.setEditable(false);
        scrollPaneLogger = new JScrollPane(taLogger);
        scrollPaneOffline = new JScrollPane(taOffline);
        scrollPaneOnline = new JScrollPane(taOnline);
        scrollPanePictures = new JScrollPane(listImages);

        scrollPaneLogger.setPreferredSize(new Dimension(600, 530));
        scrollPaneOnline.setPreferredSize(new Dimension(380, 260));
        scrollPaneOffline.setPreferredSize(new Dimension(380, 260));
        scrollPanePictures.setPreferredSize(new Dimension(200, 200));

        btnLock = new JButton("Lock door");
        btnUnlock = new JButton("Unlock door");

        //btnOFF = new JButton("Off");
        //btnON = new JButton("On");
        btnLocation = new JButton("Change location");
        btnPhoto = new JButton("Take photo");

        btnUnlock.setPreferredSize(new Dimension(btnDimension));
        btnLock.setPreferredSize(new Dimension(btnDimension));

        //btnOFF.setPreferredSize(new Dimension(btnDimension));
        //btnON.setPreferredSize(new Dimension(btnDimension));
        btnPhoto.setPreferredSize(new Dimension(btnDimension));
        btnLocation.setPreferredSize(new Dimension(btnDimension));

        centerPanelNorth.add(scrollPaneOnline);
        centerPanelCenter.add(scrollPaneOffline);

        leftPanelSouth.add(btnLock, BorderLayout.SOUTH);
        leftPanelSouth.add(btnUnlock, BorderLayout.SOUTH);

        //leftPanelNorth.add(btnON, BorderLayout.CENTER);
        //leftPanelNorth.add(btnOFF, BorderLayout.CENTER);
        leftPanelCenter.add(btnPhoto);
        leftPanelCenter.add(scrollPanePictures);

        //leftPanel.add(leftPanelNorth, BorderLayout.NORTH);
        leftPanel.add(leftPanelCenter, BorderLayout.NORTH);
        leftPanel.add(leftPanelSouth, BorderLayout.CENTER);

        centerPanel.add(centerPanelNorth, BorderLayout.NORTH);
        centerPanel.add(centerPanelCenter, BorderLayout.CENTER);
        centerPanel.add(centerPanelSouth, BorderLayout.SOUTH);

        TitledBorder loggerBorder = new TitledBorder("Logger");
        TitledBorder offlineBorder = new TitledBorder("Offline Components");
        TitledBorder onlineBorder = new TitledBorder("Online Components");
        TitledBorder onOffBorder = new TitledBorder("Turn On/Off");
        TitledBorder doorBorder = new TitledBorder("Door");
        TitledBorder camera = new TitledBorder("Camera");
        TitledBorder borderImages = new TitledBorder("Images");

        loggerBorder.setTitleColor(new Color(62, 134, 160));
        onlineBorder.setTitleColor(new Color(62, 134, 160));
        offlineBorder.setTitleColor(new Color(62, 134, 160));
        onOffBorder.setTitleColor(new Color(62, 134, 160));
        doorBorder.setTitleColor(new Color(62, 134, 160));
        camera.setTitleColor(new Color(62, 134, 160));
        borderImages.setTitleColor(new Color(62, 134, 160));

        centerPanelNorth.setBorder(onlineBorder);
        centerPanelCenter.setBorder(offlineBorder);

        leftPanelNorth.setBorder(onOffBorder);
        leftPanelSouth.setBorder(doorBorder);
        leftPanelCenter.setBorder(camera);
        centerPanelSouth.setBorder(borderImages);

        scrollPaneLogger.setBorder(loggerBorder);

        rightPanel.add(scrollPaneLogger);

        leftPanel.setBackground(new Color(60, 63, 65));
        leftPanelSouth.setBackground(new Color(60, 63, 65));
        leftPanelNorth.setBackground(new Color(60, 63, 65));
        leftPanelCenter.setBackground(new Color(60, 63, 65));
        centerPanelSouth.setBackground(new Color(60, 63, 65));

        centerPanelCenter.setBackground(new Color(60, 63, 65));
        centerPanelNorth.setBackground(new Color(60, 63, 65));
        centerPanel.setBackground(new Color(60, 63, 65));

        listImages.setBackground(new Color(43, 43, 43));
        taOnline.setBackground(new Color(43, 43, 43));
        taOffline.setBackground(new Color(43, 43, 43));
        taLogger.setBackground(new Color(43, 43, 43));

        rightPanel.setBackground(new Color(60, 63, 65));

        //btnOFF.setBackground(new Color(43, 43, 43));
        //btnON.setBackground(new Color(43, 43, 43));

        btnLock.setBackground(new Color(43, 43, 43));
        btnUnlock.setBackground(new Color(43, 43, 43));
        btnPhoto.setBackground(new Color(43, 43, 43));
        btnPhoto.setForeground(Color.white);
        btnLocation.setBackground(new Color(43, 43, 43));
        btnLocation.setForeground(Color.white);
        //btnON.setForeground(Color.white);
        //btnOFF.setForeground(Color.white);
        btnUnlock.setForeground(Color.white);
        btnLock.setForeground(Color.white);

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);


        taOnline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        taOnline.addMouseListener(buttonListener);
        listImages.addMouseListener(buttonListener);
        listImages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //btnOFF.addActionListener(buttonListener);
        //btnON.addActionListener(buttonListener);
        btnLock.addActionListener(buttonListener);
        btnUnlock.addActionListener(buttonListener);
        btnPhoto.addActionListener(buttonListener);
        btnLocation.addActionListener(buttonListener);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setContentPane(MainPanel.this);
        frame.setBackground(new Color(83, 86, 91));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                globalClientController.closeSocket();
            }
        });
    }

    public void setOnlineSensor(ArrayList<SecurityComponent> rey) {
        dlmOnline.clear();

        for (SecurityComponent s : rey
        ) {
            dlmOnline.addElement(s);
        }
        taOnline.setModel(dlmOnline);
        taOnline.repaint();
    }

    public void setOfflineSensor(ArrayList<SecurityComponent> rey) {
        dlmOffline.clear();

        for (SecurityComponent s : rey
        ) {
            dlmOffline.addElement(s);
        }
        taOffline.setModel(dlmOffline);
        taOffline.repaint();
    }

    public void clearList() {
        dlmOnline.clear();
        dlmOffline.clear();
        taOnline.setModel(dlmOnline);
        taOffline.setModel(dlmOffline);
        taOnline.repaint();
        taOffline.repaint();
    }


    public void setTaLogger(String text) {
        taLogger.setText(text);
    }

    public String[] getStartDate() {
        String temp = tfStartDate.getText();
        if (temp.matches("\\d{4}-\\d{2}-\\d{2}")) {
            String[] startDate = tfStartDate.getText().split("-");
            return startDate;
        } else {
            JOptionPane.showMessageDialog(null, "Use the correct format!");
            return null;
        }
    }

    public String[] getStartTime() {
        String temp = tfStartTime.getText();
        if (temp.matches("\\d{2}-\\d{2}")) {
            String[] startTime = tfStartTime.getText().split("-");
            return startTime;
        } else {
            JOptionPane.showMessageDialog(null, "Use the correct format!");
            return null;
        }
    }

    public String[] getEndDate() {
        String temp = tfEndDate.getText();
        if (temp.matches("\\d{4}-\\d{2}-\\d{2}")) {
            String[] endDate = tfEndDate.getText().split("-");
            return endDate;
        } else {
            JOptionPane.showMessageDialog(null, "Use the correct format!");
            return null;
        }
    }

    public String[] getEndTime() {
        String temp = tfEndTime.getText();
        if (temp.matches("\\d{2}-\\d{2}")) {
            String[] endTime = tfEndTime.getText().split("-");
            return endTime;
        } else {
            JOptionPane.showMessageDialog(null, "Use the correct format!");
            return null;
        }
    }

    public void updateImageList(LinkedList<ImageIcon> list) {
        defaultListModelImages.clear();
        for (ImageIcon imageIcon : list) {
            defaultListModelImages.addElement(imageIcon.toString());
        }

        listImages.setModel(defaultListModelImages);
        listImages.repaint();
    }

    public JLabel cropImage(ImageIcon icon, int width, int height) {
        int newWidth = icon.getIconWidth();
        int newHeight = icon.getIconHeight();

        if (icon.getIconWidth() > width) {
            newWidth = width;
            newHeight = (newWidth * icon.getIconHeight()) / icon.getIconWidth();
        }

        if (newHeight > height) {
            newHeight = height;
            newWidth = (icon.getIconWidth() * newHeight) / icon.getIconHeight();
        }

        return new JLabel(new ImageIcon(icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT)));
    }


    private class ButtonListener extends MouseAdapter implements ActionListener {

        public void mouseClicked(MouseEvent e) {

            if (SwingUtilities.isRightMouseButton(e) && !taOnline.isSelectionEmpty() && taOnline.locationToIndex(e.getPoint()) == taOnline.getSelectedIndex()) {

                chosenSensor = (SecurityComponent) dlmOnline.getElementAt(taOnline.getSelectedIndex());
                popup = new JPopupMenu();
                JLabel label1 = new JLabel("Välj location för: " + chosenSensor.getClass().getSimpleName() + "\n" + " id: " + chosenSensor.getId());
                popupPanel = new JPanel();
                popup.add(popupPanel);
                popupPanel.add(label1);

                btnOk = new JButton("OK");
                btnOk.addActionListener(this::actionPerformed);

                textFieldPopup = new JTextField();
                textFieldPopup.setToolTipText("Type in the new location:");

                popupPanel.setPreferredSize(new Dimension(300, 90));
                textFieldPopup.setPreferredSize(new Dimension(200, 20));
                btnOk.setPreferredSize(new Dimension(150, 20));
                popupPanel.add(textFieldPopup);
                popupPanel.add(btnOk, CENTER_ALIGNMENT);
                popup.show(taOnline, e.getX(), e.getY());

            } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                int index = listImages.locationToIndex(e.getPoint());
                ImageIcon imageIcon = globalClientController.getImages().get(index);
                centerPanelSouth.removeAll();
                centerPanelSouth.add(cropImage(imageIcon, 780, 660));
                centerPanelSouth.revalidate();
                centerPanelSouth.repaint();
            }
        }


        @Override

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnLock) {
                chosenSensor = (SecurityComponent) dlmOnline.getElementAt(taOnline.getSelectedIndex());
                globalClientController.send(new Message("lock", chosenSensor));

            } else if (e.getSource() == btnUnlock) {
                chosenSensor = (SecurityComponent) dlmOnline.getElementAt(taOnline.getSelectedIndex());
                globalClientController.send(new Message("unlock", chosenSensor));

            } else if (e.getSource() == btnFilter) {
                setTaLogger(globalClientController.getClientLoggerText());

            } else if (e.getSource() == btnPhoto) {
                globalClientController.send("Take photo");

            } else if (e.getSource() == btnOk) {

                location = textFieldPopup.getText();
                System.out.println("The new location: " + location);
                chosenSensor.setLocation(location);
                popup.setVisible(false);
                globalClientController.send(new Message("new location", chosenSensor));
            }
        }
    }
}
