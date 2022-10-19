package globalClient;

import globalServer.Logger;
import model.Message;
import model.SecurityComponent;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**@author Ammar Darwesh, Malek Abdul Sater  @coauthor**/
public class GlobalClientController {
    private GlobalClient globalClient;
    private MainFrame mainFrame;
    private Logger logger;
    private LinkedList<ImageIcon> images = new LinkedList<>();

    public GlobalClientController() {
        globalClient = new GlobalClient("localhost", 8081, this);
        mainFrame = new MainFrame(this);
    }

    public void authenticateUser() {
        mainFrame.disposeLoginPanel();
        mainFrame.showMainPanel();
    }

    public void send(String string) {
        globalClient.send(string);
    }

    public void send(Message msg) {
        globalClient.send(msg);
    }

    public void closeSocket() {
        globalClient.closeSocket();
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getClientLoggerText() {
        String[] startDateArray = mainFrame.getMainPanel().getStartDate();
        String[] startTimeArray = mainFrame.getMainPanel().getStartTime();
        String[] endDateArray = mainFrame.getMainPanel().getEndDate();
        String[] endTimeArray = mainFrame.getMainPanel().getEndTime();
        try {
            int startDateYear = Integer.parseInt(startDateArray[0]);
            int startDateMonth = Integer.parseInt(startDateArray[1]);
            int startDateDay = Integer.parseInt(startDateArray[2]);
            int startTimeHour = Integer.parseInt(startTimeArray[0]);
            int startTimeMin = Integer.parseInt(startTimeArray[1]);
            int endDateYear = Integer.parseInt(endDateArray[0]);
            int endDateMonth = Integer.parseInt(endDateArray[1]);
            int endDateDay = Integer.parseInt(endDateArray[2]);
            int endTimeHour = Integer.parseInt(endTimeArray[0]);
            int endTimeMin = Integer.parseInt(endTimeArray[1]);

            return logger.getLog(startDateYear, startDateMonth, startDateDay, startTimeHour,
                    startTimeMin, endDateYear, endDateMonth, endDateDay, endTimeHour, endTimeMin);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }

    public LinkedList<ImageIcon> getImages() {
        return images;
    }

    public void addImage(ImageIcon imageIcon) {
        images.add(imageIcon);
        mainFrame.updateImageList();
    }

    public void removeImage(ImageIcon imageIcon) {
        images.remove(imageIcon);
        mainFrame.updateImageList();
    }

    public void setOnlinesensor(ArrayList<SecurityComponent> rey) {
        mainFrame.getMainPanel().setOnlineSensor(rey);
    }

    public void setOfflinesensor(ArrayList<SecurityComponent> rey) {
        mainFrame.getMainPanel().setOfflineSensor(rey);
    }

    public void clearList() {
        mainFrame.getMainPanel().clearList();
    }

    public static void main(String[] args) {
        new GlobalClientController();
    }
}
