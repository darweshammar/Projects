package globalServerGUI;

import globalServer.GlobalServerController;

import javax.swing.*;
import java.awt.*;

/**@author Ammar Darwesh, Malek Abdul Sater  @coauthor**/
public class MainFrame {
    private JFrame frame = new JFrame();
    private GlobalServerController globalServerController;
    private MainPanel mainPanel;


    public MainFrame(GlobalServerController globalServerController) {
        this.globalServerController = globalServerController;
        mainPanel = new MainPanel(globalServerController);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation(dim.width / 3 - frame.getSize().width / 3, dim.height / 3 - frame.getSize().height / 3);
                frame.setSize(new Dimension(1440, 487));
                frame.setContentPane(mainPanel);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setTitle("Server");
            }
        });
    }

    public RegisterPanel getRegisterPanel() {
        return mainPanel.getRegisterPanel();
    }

    public MainPanel getMainPanel() {

        return mainPanel;

    }

    public void disposeRegisterPanel() {
        mainPanel.disposeRegisterPanel();
    }

}
