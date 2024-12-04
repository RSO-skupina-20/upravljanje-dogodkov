package si.fri.rso.skupina20.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String email, String subject, String text) {
        final String username = "rso.organizacijadogodkov@gmail.com";
        final String password = "Rso_Projektna_Skupina_16_Geslo";

        Properties props = new Properties();
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        System.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
        System.setProperty("mail.smtp.ssl.checkserveridentity", "true");

        System.out.println("Before session creation");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        System.out.println("After session creation");

        try {
            System.out.println("Before message creation");
            Message message = new MimeMessage(session);
            System.out.println("After message creation");
            message.setFrom(new InternetAddress(username));
            System.out.println("After set from");
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email));
            System.out.println("After set recipients");
            message.setSubject(subject);
            System.out.println("After set subject");
            message.setText(text);
            System.out.println("After set text");
            Transport.send(message);
            System.out.println("Email sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
