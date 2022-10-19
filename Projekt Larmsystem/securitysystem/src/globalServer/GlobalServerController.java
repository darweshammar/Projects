package globalServer;

import globalServerGUI.MainFrame;
import globalServerGUI.RegisterPanel;

import javax.swing.*;
import javax.mail.MessagingException;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**@author Ammar Darwesh, Malek Abdul Sater  @coauthor**/
public class GlobalServerController implements Observer {
    private GlobalServer globalServer;
    private HashMap<String, Home> homes;
    private MainFrame mainFrame;
    private UserRegister userRegister;
    private EmailSender emailSender;

    LinkedList<Home> homesList = new LinkedList<>();

    public GlobalServerController() {
        emailSender = new EmailSender();
        userRegister = new UserRegister();
        this.userRegister.addObserver(this);
        homes = new HashMap<>();
        mainFrame = new MainFrame(this);

        globalServer = new GlobalServer(8081, homes);
        readUserFromFile();
    }

    public HashMap<String, Home> getHomes() {
        return homes;
    }

    public Home getHome(String name) {
        return homes.get(name);
    }

    public User getUser(String userName) {
        return homes.get(userName).getUser();
    }

    public void addHome(String userName, Home home) {
        homes.put(userName, home);
    }

    public void deleteHome(String userName) {
        homes.get(userName).logger.deleteLog();
        homes.remove(userName);
    }

    public String getClientLoggerText(String username) {
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

            return homes.get(username).logger.getLog(startDateYear, startDateMonth, startDateDay, startTimeHour,
                    startTimeMin, endDateYear, endDateMonth, endDateDay, endTimeHour, endTimeMin);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void writeHomeToFile(Home home) {

        String filename = "data/HomeObject.dat";
        homesList.add(home);

        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {

            for (Home value : homesList) {
                oos.writeObject(value);
                oos.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeHomeToFileAfterDelete() {

        String filename = "data/HomeObject.dat";

        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {

            for (Home home : homesList) {

                oos.writeObject(home);
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void readUserFromFile() {

        String filename = "data/HomeObject.dat";
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {

            Home home = (Home) ois.readObject();

            while (home != null) {

                homes.put(home.getUser().getUserName(), home);
                homesList.add(home);
                userRegister.addUser(home.getUser());
                home = (Home) ois.readObject();

            }
        } catch (IOException | ClassNotFoundException ignored) {

        }
    }

    public void removeHome(Home home) {
        homesList.remove(home);
    }

    public void sendEmail(String recipient, String subject, String information) {

        emailSender.sendMail(recipient, subject, information);

    }

    public UserRegister getUserRegister() {
        return userRegister;
    }

    public void setInfo(String[][] strings) {
        mainFrame.getMainPanel().setTableInfo(strings);
    }


    public void addUserToSystem(User user) {
        getUserRegister().addUser(user);
        addHome(user.getUserName(), new Home(user));

        mainFrame.disposeRegisterPanel();

        Thread thread = new Thread(() -> {
            String loginInfo = "Hello, "+user.getFormattedFirstName()+
                    "!\n\nThe following are your login details:\nUsername: "
                    + user.getUserName() + "\nPassword: " + user.getPassword() +"\n\nThank you for choosing " +
                    "SecureHomesMAU as your security system provider.\nBest regards, The team at SecureHomesMAU!";

            sendEmail(user.getEmail(), "SecureHomesMAU", loginInfo);
            writeHomeToFile(getHomes().get(user.getUserName()));
        });

        thread.start();
    }

    @Override
    public void update(Observable observable, Object o) {
        setInfo((String[][]) o);
    }

    public static void main(String[] args) {
        new GlobalServerController();
    }
}
