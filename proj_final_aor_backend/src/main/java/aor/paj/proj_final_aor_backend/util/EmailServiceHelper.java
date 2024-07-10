package aor.paj.proj_final_aor_backend.util;


import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;


/**
 * This class is used to send emails from the application.
 * It uses the JavaMail API to send emails via SMTP.
 * The email address and password used to send emails are defined as class variables.
 * The SMTP server settings are defined in the constructor.
 * The sendEmail method is used to send an email with a specified recipient, subject, and body.
 *
 * @author VaniaMendes
 */
@Stateless
public class EmailServiceHelper {
    private static final Logger logger = LogManager.getLogger(EmailServiceHelper.class);

    // The email address used to send emails
    private final String email = "proj_final_aor@outlook.com";
    // The password for the email address used to send emails
    private final String password = "Password1234%";


    @Resource(name = "mail/session")
    private Session session;



    /**
     * The constructor for the EmailServiceHelper class.
     * It defines the SMTP server settings and creates a new session with these settings.
     */
    public EmailServiceHelper(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

    }


    /**
     * This method is used to send an email with a specified recipient, subject, and body.
     * It creates a new message with the specified parameters and sends it using the Transport class.
     * If the email is sent successfully, it logs a success message.
     * If there is an error while sending the email, it logs an error message and prints the stack trace.
     *
     * @param to The recipient of the email.
     * @param subject The subject of the email.
     * @param body The body of the email.
     */
    public void sendEmail(String to, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(body, "text/html");

            Transport.send(message);

            logger.info("Email sent with sucess " + to);
        } catch (MessagingException e) {
            logger.warn("Error to send email " + to);
            e.printStackTrace();
        }
    }
}
