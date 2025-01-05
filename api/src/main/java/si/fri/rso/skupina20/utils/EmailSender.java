package si.fri.rso.skupina20.utils;

import com.kumuluz.ee.streaming.common.annotations.StreamProducer;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@ApplicationScoped
public class EmailSender {

    private static String fromDomain = System.getenv("FROM_DOMAIN");
    private static String token = System.getenv("TOKEN");

    public static void sendEmail(String nameTo, String emailAddress, String subject, String text) {
        Email email = new Email();

        email.setFrom("Organizacija dogodkov", fromDomain);
        email.addRecipient(nameTo, emailAddress);

        email.setSubject(subject);
        email.setPlain(text);

        MailerSend ms = new MailerSend();
        ms.setToken(token);

        try {
            MailerSendResponse response = ms.emails().send(email);
            System.out.println(response.messageId);
        } catch (MailerSendException e) {
            e.printStackTrace();
        }
    }

    // send email using kafka kumuluzee producer

    @Inject
    @StreamProducer
    private Producer<String, String> producer;

    public void sendEmailKafka(String nameTo, String emailAddress, String subject, String text) {
        // Escape special characters (e.g., newline characters)
        String escapedText = text.replace("\n", "\\n").replace("\r", "\\r");

        // Format the email message
        String emailMessage = String.format("{\"name\": \"%s\", \"email\": \"%s\", \"subject\": \"%s\", \"body\": \"%s\"}",
                nameTo, emailAddress, subject, escapedText);

        producer.send(new ProducerRecord<>("test-topic", emailMessage));
        System.out.println("Email sent to kafka");
    }






}
