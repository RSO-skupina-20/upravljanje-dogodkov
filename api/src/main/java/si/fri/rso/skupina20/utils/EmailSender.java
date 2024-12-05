package si.fri.rso.skupina20.utils;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;
import io.github.cdimascio.dotenv.Dotenv;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String nameTo, String emailAddress, String subject, String text) {
        Dotenv dotenv = Dotenv.load();
        Email email = new Email();
        final String fromDomain = dotenv.get("FROM_DOMAIN");

        email.setFrom("Organizacija dogodkov", fromDomain);
        email.addRecipient(nameTo, emailAddress);

        email.setSubject(subject);
        email.setPlain(text);

        MailerSend ms = new MailerSend();
        ms.setToken(dotenv.get("TOKEN"));

        try {
            MailerSendResponse response = ms.emails().send(email);
            System.out.println(response.messageId);
        } catch (MailerSendException e) {
            e.printStackTrace();
        }
    }
}
