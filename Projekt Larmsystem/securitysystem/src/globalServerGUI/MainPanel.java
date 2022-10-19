package globalServerGUI;

import globalServer.GlobalServerController;
import globalServer.Home;
import globalServer.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**@author Ammar Darwesh, Malek Abdul Sater  @coauthor**/
public class MainPanel extends JPanel {
    private RegisterPanel registerPanel;
    private JPanel pnlLeft;
    private JPanel pnlRight;
    private JButton btnRegister;
    private JButton btnDelete;
    private JTextField tfSearch;
    private JLabel lblSearch;
    private JLabel lblEmpty;
    private JScrollPane loggerScrollPane;
    private JTextArea taLogger;
    private JTable tblInfo;
    private ButtonListener buttonListener;
    private GlobalServerController globalServerController;
    private DefaultTableModel model;
    private String[] columns;
    private JScrollPane tblScrollPane;
    private JPanel pnlDateTime = new JPanel();

    private JButton btnGetLog;
    private JTextField tfStartDate;
    private JTextField tfEndDate;
    private JTextField tfStartTime;
    private JTextField tfEndTime;
    TableRowSorter<DefaultTableModel> rowSorter;

    public MainPanel(GlobalServerController globalServerController) {
        this.globalServerController = globalServerController;
        draw();
    }

    public void drawPnlDateTime() {
        Dimension tfDimension = new Dimension(125, 35);
        btnGetLog = new JButton("Filter");
        btnGetLog.addActionListener(buttonListener);
        pnlDateTime.setPreferredSize(new Dimension(600, 45));
        tfStartDate = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        tfStartTime = new JTextField(new SimpleDateFormat("HH-mm").format(Calendar.getInstance().getTime()));
        tfEndDate = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        tfEndTime = new JTextField(new SimpleDateFormat("HH-mm").format(Calendar.getInstance().getTime()));

        tfStartDate.setPreferredSize(tfDimension);
        tfStartTime.setPreferredSize(tfDimension);
        tfEndDate.setPreferredSize(tfDimension);
        tfEndTime.setPreferredSize(tfDimension);
        btnGetLog.setPreferredSize(new Dimension(70, 35));

        tfStartDate.setBorder(BorderFactory.createTitledBorder("From: yyyy-mm-dd"));
        tfStartTime.setBorder(BorderFactory.createTitledBorder("From: HH-mm"));
        tfEndDate.setBorder(BorderFactory.createTitledBorder("To: yyyy-mm-dd"));
        tfEndTime.setBorder(BorderFactory.createTitledBorder("To: HH-mm"));

        pnlDateTime.add(tfStartDate);
        pnlDateTime.add(tfStartTime);
        pnlDateTime.add(tfEndDate);
        pnlDateTime.add(tfEndTime);
        pnlDateTime.add(btnGetLog);

        pnlRight.add(BorderLayout.CENTER, pnlDateTime);
    }

    public void draw() {

        buttonListener = new ButtonListener();

        model = new DefaultTableModel();
        pnlLeft = new JPanel();
        pnlRight = new JPanel();


        btnRegister = new JButton();
        btnDelete = new JButton();

        tfSearch = new JTextField();

        lblSearch = new JLabel();
        lblEmpty = new JLabel();

        taLogger = new JTextArea();
        tblInfo = new JTable();

        loggerScrollPane = new JScrollPane(taLogger);
        tblScrollPane = new JScrollPane(tblInfo);

        this.setBackground(new Color(83, 86, 91));


        taLogger.setBackground(new Color(43, 43, 43));
        tblInfo.setBackground(new Color(83, 86, 91));

        this.setBackground(new Color(60, 63, 65));
        pnlLeft.setBackground(new Color(60, 63, 65));
        pnlRight.setBackground(new Color(60, 63, 65));


        this.setLayout(new BorderLayout());
        pnlLeft.setLayout(new FlowLayout());
        pnlRight.setLayout(new BorderLayout());

        this.setPreferredSize(new Dimension(1250, 486));
        pnlRight.setPreferredSize(new Dimension(610, 485));
        tblScrollPane.getViewport().setBackground(new Color(43, 43, 43));
        loggerScrollPane.setPreferredSize(new Dimension(605, 370));
        this.setPreferredSize(new Dimension(1250, 600));
        pnlLeft.setPreferredSize(new Dimension(800, 580));
        tblScrollPane.setPreferredSize(new Dimension(750, 370));
        lblEmpty.setPreferredSize(new Dimension(290, 40));
        tfSearch.setPreferredSize(new Dimension(200, 20));

        lblSearch.setText("Search:");
        btnDelete.setText("Delete");
        btnRegister.setText("Register");

        TitledBorder borderContact = new TitledBorder("User Information");
        TitledBorder borderLogger = new TitledBorder("Logger");
        borderContact.setTitleColor(new Color(62, 134, 160));
        borderLogger.setTitleColor(new Color(62, 134, 160));

        taLogger.setEditable(false);
        pnlLeft.setBorder(borderContact);
        pnlRight.setBorder(borderLogger);

        columns = new String[]{"First Name", "Last Name", "Address", "Zip Code", "City", "Username", "Password", "Email"};

        model.setColumnIdentifiers(columns);

        tblInfo.setRowHeight(30);
        tblInfo.setModel(model);
        tblInfo.setForeground(Color.white);

        btnRegister.setPreferredSize(new Dimension(100, 40));
        btnDelete.setPreferredSize(new Dimension(100, 40));

        taLogger.setForeground(Color.white);
        btnRegister.setBackground(new Color(43, 43, 43));
        btnDelete.setBackground(new Color(43, 43, 43));

        btnRegister.setForeground(Color.white);
        btnDelete.setForeground(Color.white);

        lblSearch.setForeground(Color.white);
        loggerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        pnlLeft.add(tblScrollPane);
        pnlLeft.add(btnRegister);
        pnlLeft.add(btnDelete);
        pnlLeft.add(lblEmpty);
        pnlLeft.add(lblSearch);
        pnlLeft.add(tfSearch);

        pnlRight.add(BorderLayout.NORTH, loggerScrollPane);
        drawPnlDateTime();

        add(pnlLeft, BorderLayout.WEST);
        add(pnlRight, BorderLayout.EAST);

        btnRegister.addActionListener(buttonListener);
        btnDelete.addActionListener(buttonListener);


        tfSearch.addKeyListener(new KeyReleasedListener());
    }



    public void setTaLogger(String text) {
        taLogger.setText(text);
    }

    public void setTableInfo(String[][] info) {

        model = new DefaultTableModel(info, columns){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblInfo.setModel(model);
        tblInfo.repaint();

    }


    public String[] getStartDate() {
        String temp = tfStartDate.getText();
        if (temp.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return tfStartDate.getText().split("-");
        } else {
            JOptionPane.showMessageDialog(null, "Use the correct format!");
            return null;
        }
    }

    public String[] getStartTime() {
        String temp = tfStartTime.getText();
        if (temp.matches("\\d{2}-\\d{2}")) {
            return tfStartTime.getText().split("-");
        } else {
            JOptionPane.showMessageDialog(null, "Use the correct format!");
            return null;
        }
    }

    public String[] getEndDate() {
        String temp = tfEndDate.getText();
        if (temp.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return tfEndDate.getText().split("-");
        } else {
            JOptionPane.showMessageDialog(null, "Use the correct format!");
            return null;
        }
    }

    public String[] getEndTime() {
        String temp = tfEndTime.getText();
        if (temp.matches("\\d{2}-\\d{2}")) {
            return tfEndTime.getText().split("-");
        } else {
            JOptionPane.showMessageDialog(null, "Use the correct format!");
            return null;
        }
    }

    public int getSelectedRow() {
        return tblInfo.getSelectedRow();
    }

    public void searchTable() {

        DefaultTableModel tableModel = (DefaultTableModel) tblInfo.getModel();
        String search = tfSearch.getText().toLowerCase();
        rowSorter = new TableRowSorter<>(tableModel);
        tblInfo.setRowSorter(rowSorter);
        rowSorter.setRowFilter(RowFilter.regexFilter(search));

    }

    public void disposeRegisterPanel() {
        registerPanel.disposePanel();
    }


    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (actionEvent.getSource() == btnRegister) {
                registerPanel = new RegisterPanel(globalServerController);
            } else if (actionEvent.getSource() == btnDelete) {
                String username = globalServerController.getUserRegister().getUser(getSelectedRow()).getUserName();
                User user = globalServerController.getUser(username);
                globalServerController.getUserRegister().deleteUser(user);
                Home home = globalServerController.getHome(username);
                globalServerController.deleteHome(username);
                globalServerController.removeHome(home);
                globalServerController.writeHomeToFileAfterDelete();
            } else if (actionEvent.getSource() == btnGetLog) {
                if (getSelectedRow() != -1 && tfSearch.getText().isEmpty()) {
                    String username = globalServerController.getUserRegister().getUser(getSelectedRow()).getUserName();
                    System.out.println(username);
                    setTaLogger(globalServerController.getClientLoggerText(username));
                } else if (getSelectedRow() != -1 && !tfSearch.getText().isEmpty()) {
                    int i = tblInfo.getRowSorter().convertRowIndexToModel(getSelectedRow());
                    String username = globalServerController.getUserRegister().getUser(i).getUserName();
                    setTaLogger(globalServerController.getClientLoggerText(username));
                } else {
                    JOptionPane.showMessageDialog(null, "You have to choose a user first!");
                }
            }
        }
    }

    private class KeyReleasedListener implements KeyListener {


        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            searchTable();
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            searchTable();
        }
    }

    public RegisterPanel getRegisterPanel() {
        return registerPanel;
    }
}
