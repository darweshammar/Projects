package localserver.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**@author Olof Persson, Jens Moths  @coauthor**/
public class ChangeCode extends JFrame implements ActionListener {
    private JButton btnNum0, btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7,
            btnNum8, btnNum9, btnClear, btnOK;
    JPanel tfPanel;
    JPanel numPadPanel;
    private JTextField tfNumPad;
    private String pinCode = "";
    private Font font = new Font("Courier", Font.BOLD, 30);
    private Color numberPadColor = new Color(74, 77, 82);
    private MainFrame mainFrame;
    Frame ChangeCodeFrame;
    boolean pinOK = false;


    public ChangeCode(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        JFrame ChangeCodeFrame = new JFrame();
        ChangeCodeFrame.setTitle("Ändra kod");
        ChangeCodeFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setUndecorated(true);
        drawChangeCode();
        setVisible(true);

        JOptionPane.showMessageDialog(null, "Ange nuvarande pinkod");
    }

    public void drawChangeCode() {

        tfPanel = new JPanel();
        numPadPanel = new JPanel();
        tfNumPad = new JTextField();
        btnNum0 = new JButton("0");
        btnNum1 = new JButton("1");
        btnNum2 = new JButton("2");
        btnNum3 = new JButton("3");
        btnNum4 = new JButton("4");
        btnNum5 = new JButton("5");
        btnNum6 = new JButton("6");
        btnNum7 = new JButton("7");
        btnNum8 = new JButton("8");
        btnNum9 = new JButton("9");
        btnClear = new JButton("CLR");
        btnOK = new JButton("OK");

        numPadPanel.setLayout(new GridLayout(4, 3, 2, 2));
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(300, 300));
        numPadPanel.setBackground(new Color(62, 134, 160));
        tfPanel.setLayout(new BorderLayout());

        this.add(tfPanel, BorderLayout.NORTH);
        this.add(numPadPanel, BorderLayout.CENTER);

        drawNumPad();
        drawTextFieldPanel();
    }

    public void drawNumPad() {
        numPadPanel.add(btnNum1);
        numPadPanel.add(btnNum2);
        numPadPanel.add(btnNum3);
        numPadPanel.add(btnNum4);
        numPadPanel.add(btnNum5);
        numPadPanel.add(btnNum6);
        numPadPanel.add(btnNum7);
        numPadPanel.add(btnNum8);
        numPadPanel.add(btnNum9);
        numPadPanel.add(btnClear);
        numPadPanel.add(btnNum0);
        numPadPanel.add(btnOK);
        btnNum0.addActionListener(this);
        btnNum1.addActionListener(this);
        btnNum2.addActionListener(this);
        btnNum3.addActionListener(this);
        btnNum4.addActionListener(this);
        btnNum5.addActionListener(this);
        btnNum6.addActionListener(this);
        btnNum7.addActionListener(this);
        btnNum8.addActionListener(this);
        btnNum9.addActionListener(this);
        btnOK.addActionListener(this);
        btnClear.addActionListener(this);

        btnNum1.setBackground(numberPadColor);
        btnNum1.setForeground(Color.black);
        btnNum1.setFocusPainted(false);

        btnNum0.setFont(font);
        btnNum0.setBackground(numberPadColor);
        btnNum0.setForeground(Color.black);
        btnNum0.setFocusPainted(false);

        btnNum2.setFont(font);
        btnNum2.setBackground(numberPadColor);
        btnNum2.setForeground(Color.black);
        btnNum2.setFocusPainted(false);

        btnNum3.setFont(font);
        btnNum3.setBackground(numberPadColor);
        btnNum3.setForeground(Color.black);
        btnNum3.setFocusPainted(false);

        btnNum4.setFont(font);
        btnNum4.setBackground(numberPadColor);
        btnNum4.setForeground(Color.black);
        btnNum4.setFocusPainted(false);

        btnNum5.setFont(font);
        btnNum5.setBackground(numberPadColor);
        btnNum5.setForeground(Color.black);
        btnNum5.setFocusPainted(false);

        btnNum6.setFont(font);
        btnNum6.setBackground(numberPadColor);
        btnNum6.setForeground(Color.black);
        btnNum6.setFocusPainted(false);

        btnNum7.setFont(font);
        btnNum7.setBackground(numberPadColor);
        btnNum7.setForeground(Color.black);
        btnNum7.setFocusPainted(false);

        btnNum8.setFont(font);
        btnNum8.setBackground(numberPadColor);
        btnNum8.setForeground(Color.black);
        btnNum8.setFocusPainted(false);

        btnNum9.setFont(font);
        btnNum9.setBackground(numberPadColor);
        btnNum9.setForeground(Color.black);
        btnNum9.setFocusPainted(false);

        btnOK.setFont(font);
        btnOK.setBackground(numberPadColor);
        btnOK.setForeground(Color.black);
        btnOK.setFocusPainted(false);

        btnClear.setFont(font);
        btnClear.setBackground(numberPadColor);
        btnClear.setForeground(Color.black);
        btnClear.setFocusPainted(false);

        btnNum0.setFont(font);
        btnNum1.setFont(font);
        btnNum2.setFont(font);
        btnNum3.setFont(font);
        btnNum4.setFont(font);
        btnNum5.setFont(font);
        btnNum6.setFont(font);
        btnNum7.setFont(font);
        btnNum8.setFont(font);
        btnNum9.setFont(font);
        btnOK.setFont(font);
        btnClear.setFont(font);
    }

    public void drawTextFieldPanel() {
        tfPanel.add(tfNumPad, BorderLayout.CENTER);
        tfPanel.setPreferredSize(new Dimension(300, 50));
        tfNumPad.setHorizontalAlignment(JTextField.CENTER);
        tfNumPad.setEditable(false);
        tfNumPad.setFont(new Font("Courier", Font.BOLD, 35));
        tfNumPad.setForeground(Color.DARK_GRAY);
    }

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String buttonNumber = button.getText();

        if (pinCode.length() < 4 && !buttonNumber.equals("OK") && !buttonNumber.equals("CLR")) {
            pinCode += buttonNumber;
        }
        if (buttonNumber.equals("CLR")) {
            pinCode = "";
        }
        if (buttonNumber.equals("OK") && pinCode.length() == 4 && !pinOK) {
            String num = tfNumPad.getText();
            if (num.equals(mainFrame.getSystemPinCode())) {
                pinOK = true;
                pinCode = "";
                JOptionPane.showMessageDialog(null, "Ange ny pinkod");
            }
        } else if (buttonNumber.equals("OK") && pinCode.length() == 4) {
            String num = tfNumPad.getText();
            mainFrame.setSystemPinCode(num);
            JOptionPane.showMessageDialog(null, "Pinkod ändrad");
            setVisible(false);
        }
        // TODO: 2020-04-14 Lägg till kod som berättar vad som händer med OK
        else if (buttonNumber.equals("OK") && pinCode.length() < 4) {
            JOptionPane.showMessageDialog(null, "Invalid: Enter a 4 digit number");
        }
        if (pinCode.length() > 4) {
            JOptionPane.showMessageDialog(null, "Invalid: Maximum 4 digits");
        }
        tfNumPad.setText(pinCode);
    }

}
