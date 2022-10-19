package globalClient;


import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**@author Ammar Darwesh, Malek Abdul Sater  @coauthor**/
public class LogInPanel extends JPanel {

    private JTextField tfUsername, tfPassword;
    private JLabel lblUsername, lblPassword;
    private JButton btnLogIn;
    private JFrame frame;
    private GlobalClientController globalClientController;

    public LogInPanel(GlobalClientController globalClientController) {
        this.globalClientController = globalClientController;
        frame = new JFrame();
        this.setLayout(new MigLayout());
        setBackground(new Color(60, 63, 65));

        btnLogIn = new JButton("Log In");
        btnLogIn.setForeground(Color.white);
        lblUsername = new JLabel("Username:");
        lblPassword = new JLabel("Password:");
        tfPassword = new JTextField();
        tfUsername = new JTextField();

        draw();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(new Dimension(220, 130));
        frame.setLocation(dim.width / 3 - frame.getSize().width / 3, dim.height / 3 - frame.getSize().height / 3);
        frame.setContentPane(this);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("LogIn");
    }

    public void draw() {
        btnLogIn.addActionListener(new ButtonListener());
        tfUsername.setPreferredSize(new Dimension(120, 20));
        lblUsername.setForeground(Color.white);
        lblPassword.setForeground(Color.white);
        btnLogIn.setBackground(new Color(43, 43, 43));
        add(lblUsername);
        add(tfUsername, "span");
        add(lblPassword);
        add(tfPassword, "span, grow");
        add(btnLogIn, "span 2, grow,  wrap");

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                globalClientController.closeSocket();
                //System.exit(0);
            }
        });
    }

    public void disposeLogInPanel() {
        frame.dispose();
    }


    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnLogIn) {
                if (!tfUsername.getText().equals("") | !tfPassword.getText().equals("")) {
                    globalClientController.send(tfUsername.getText());
                    globalClientController.send(tfPassword.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "Fill in all the fields correctly!");
                }
            }
        }
    }
}
