package globalServer;

import model.*;

import javax.mail.MessagingException;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

/**@author Ammar Darwesh, Malek Abdul Sater  @coauthor Jens Moths, Per Blomqvist**/
public class RequestHandler {

    private EmailSender emailSender;
    private Home home;

    public RequestHandler(Home home) {
        this.home = home;
        emailSender = new EmailSender();

    }

    public void handleServerRequest(Object requestObject, Home home) {
        String customerFirstName = home.getUser().getFormattedFirstName();
        String finishingStatement = "\n\nBest regards, The team at SecureHomesMAU";

        if (requestObject instanceof String) {
            home.logger.addToLog((String) requestObject);
            home.sendToAllClients(home.logger);
            home.sendToAllClients(requestObject);

        } else if (requestObject instanceof ImageIcon) {
            home.logger.addToLog("Received an image from home");
            home.sendToAllClients(home.logger);
            home.sendToAllClients(requestObject);
            emailSender.sendPictureMail(home.getUser().getEmail(), finishingStatement,
                    "A new photo from your home", (ImageIcon) requestObject);

        } else if (requestObject instanceof Message) {

            Message message = (Message) requestObject;
            SecurityComponent securityComponent = message.getSecurityComponent();

            if (securityComponent == null) {
                home.setOffline(message.getOfflineSensors());
                home.setOnline(message.getOnlineSensors());
                home.sendToAllClients(message);
//                boolean alarm = message.isAlarmOn();
//                for (SecurityComponent s : home.getOnline()) {
//                    System.out.println("ONLINE SENSOR: " + s.getId());
//                }
//                for (SecurityComponent s : home.getOffline()) {
//                    System.out.println("OFFLINE SENSOR: " + s.getId());
//                }
//                System.out.println("Alarm status: " + alarm);
            }

            if (securityComponent instanceof MagneticSensor) {
                if (securityComponent.isOpen()) {
                    home.sendToAllClients("The magnet sensor in " + securityComponent.getLocation() +
                            " has been triggered");
                    home.logger.addToLog("The magnet sensor in " + securityComponent.getLocation() +
                            " has been triggered");
                    home.sendToAllClients(home.logger);
                    emailSender.sendMail(home.getUser().getEmail(), "SecureHomesMAU", "Hello " +
                            customerFirstName + "!\n\n" + "The magnetic sensor in " + securityComponent.getLocation() +
                            " has been triggered." + finishingStatement);

                } else if (!securityComponent.isOpen()) {
                    home.sendToAllClients("The magnet sensor in " + securityComponent.getLocation() + " is online");
                    home.logger.addToLog("The magnet sensor in " + securityComponent.getLocation() + " is online");
                    home.sendToAllClients(home.logger);
                }
            }
            if (securityComponent instanceof FireAlarm) {
                home.sendToAllClients("The fire alarm detected smoke " + securityComponent.getLocation());
                home.logger.addToLog("The fire alarm detected smoke " + securityComponent.getLocation());
                try {
                    emailSender.sendMail(home.getUser().getEmail(), "SecureHomesMAU", "Hello, " +
                            customerFirstName + "!\n\nThe fire alarm has detected smoke in " +
                            securityComponent.getLocation() + "." + finishingStatement);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                home.sendToAllClients(home.logger);
            }

            if (securityComponent instanceof ProximitySensor) {
                home.sendToAllClients("The proximity sensor detected motion in " +
                        securityComponent.getLocation());
                home.logger.addToLog("The proximity sensor detected motion in " +
                        securityComponent.getLocation());
                home.sendToAllClients(home.logger);
                emailSender.sendMail(home.getUser().getEmail(), "SecureHomesMAU", "Hello, " +
                        customerFirstName + "!\n\nThe proximity sensor detected motion in "
                        + securityComponent.getLocation() + "." +finishingStatement);
            }
        }
    }

    public Object handleClientRequest(Object clientRequest) {
        Object response = null;

        if (clientRequest instanceof Message) {

            if (((Message) clientRequest).getInfo().equals("new location")) {
                home.logger.addToLog(((Message) clientRequest).getSecurityComponent().getId() +
                        " has a new location");
                home.sendToAllClients(home.logger);
                response = clientRequest;
            }
            if (((Message) clientRequest).getInfo().equals("lock")) {
                home.logger.addToLog("Door locked");
                home.sendToAllClients(home.logger);
                ((Message) clientRequest).getSecurityComponent().setOpen(false);
                response = clientRequest;
            }
            if (((Message) clientRequest).getInfo().equals("unlock")) {
                home.logger.addToLog("Door unlocked");
                home.sendToAllClients(home.logger);
                ((Message) clientRequest).getSecurityComponent().setOpen(true);
                response = clientRequest;
            }
        }
        if (clientRequest instanceof String) {
            if ("Take photo".equals(clientRequest)) {
                response = "Take photo";
                home.logger.addToLog("Client wants a photo");
                home.sendToAllClients(home.logger);
            }
        }

        return response;
    }
}




