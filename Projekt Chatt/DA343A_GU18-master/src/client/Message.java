package client;

import javax.swing.*;
import java.io.Serializable;
import java.util.Calendar;

public class Message implements Serializable {
    private static final long serialVersionUID = 42L;
    private String messageText;
    private ImageIcon messageImage;
    private User sender;
    private User recipient;
    private Calendar timeSent;
    private Calendar timeDelivered;
    private Boolean containsImage;

    public Message(){}

    public Message(String messageText, User sender, User recipient) {
        this.messageText = messageText;
        this.sender = sender;
        this.recipient = recipient;
        this.timeSent = Calendar.getInstance();
        containsImage = false;
    }

    public Message(String messageText, ImageIcon messageImage, User sender, User recipient) {
        this.messageText = messageText;
        this.messageImage = messageImage;
        this.sender = sender;
        this.recipient = recipient;
        this.timeSent = Calendar.getInstance();
        containsImage = true;
    }

    public Message(String messageText) {
        this.messageText = messageText;
    }

    public void setTimeDelivered(Calendar timeDelivered) {
        this.timeDelivered = timeDelivered;
    }

    public String getMessageText() {
        return messageText;
    }

    public ImageIcon getMessageImage() {
        return messageImage;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public Calendar getTimeSent() {
        return timeSent;
    }

    public Calendar getTimeDelivered() {
        return timeDelivered;
    }

    public Boolean getContainsImage() {
        return containsImage;
    }
}
