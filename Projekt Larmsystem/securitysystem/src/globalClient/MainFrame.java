package globalClient;

import javax.swing.*;

/**@author Ammar Darwesh, Malek Abdul Sater  @coauthor**/
public class MainFrame extends JFrame {
    private MainPanel mainPanel;
    private LogInPanel logInPanel;
    private GlobalClientController clientController;

    public MainFrame(GlobalClientController clientController) {
        this.clientController = clientController;
        logInPanel = new LogInPanel(clientController);
    }

    public void showMainPanel() {
        mainPanel = new MainPanel(clientController);
    }

    public void disposeLoginPanel() {
        logInPanel.disposeLogInPanel();
    }

    public void setTextArea(String text) {
        mainPanel.setTaLogger(text);
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public void updateImageList() {
        mainPanel.updateImageList(clientController.getImages());
    }
}
