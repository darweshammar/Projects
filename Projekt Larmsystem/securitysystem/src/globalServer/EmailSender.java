package globalServer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

/**@author Ammar Darwesh  @coauthor Jens Moths**/
public class EmailSender {

    private Properties properties;

    public EmailSender() {

        properties = new Properties();

    }

    public void sendMail(String recipient, String subject, String text) {
        try {
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            String myAccountEmail = "securehomesmau@gmail.com";
            String password = "emailutskick456";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myAccountEmail, password);
                }
            });

            Message message = messageCreator(session, myAccountEmail, recipient, subject, text);
            Transport.send(message);

        } catch (MessagingException m) {
            m.printStackTrace();
        }
    }


    public void sendPictureMail(String recipient, String text, String subject, ImageIcon imageIcon) {

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccountEmail = "securehomesmau@gmail.com";
        String password = "emailutskick456";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });
        try {
            Message message = messagePictureCreator(session, myAccountEmail, recipient, text, subject, imageIcon);
            Transport.send(message);
        } catch (MessagingException m) {
            m.printStackTrace();
        }
    }

    public Message messageCreator(Session session, String myAccountEmail, String recipient, String subject, String text) throws MessagingException {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myAccountEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setText(text);
        return message;

    }

    public static BufferedImage convertToBufferedImage(Image image)
    {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    public Message messagePictureCreator(Session session, String myAccountEmail, String recipient, String text, String subject, ImageIcon imageIcon) throws MessagingException {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myAccountEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);

        MimeBodyPart messageMime = new MimeBodyPart();

        messageMime.setText(text);

        Multipart multipart = new MimeMultipart();

        multipart.addBodyPart(messageMime);

        BufferedImage bi = convertToBufferedImage(imageIcon.getImage());
        File file = new File("data/img.jpg");
        try {
            ImageIO.write(bi, "jpg", file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        messageMime = new MimeBodyPart();
        FileDataSource source = new FileDataSource(file);
        messageMime.setDataHandler(new DataHandler(source));
        messageMime.setFileName(file.getName());
        multipart.addBodyPart(messageMime);

        message.setContent(multipart);

        return message;
    }
}