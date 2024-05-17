package aor.paj.proj_final_aor_backend.utils;


import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

@Stateless
public class EmailService {
    private static final Logger logger = LogManager.getLogger(EmailService.class);

    private final String email = "proj_final_aor@outlook.com";
    private final String password = "Password1234%";


    @Resource(name = "mail/session")
    private Session session;


    public EmailService(){
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

    public void sendEmail(String to, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            logger.info("Email sent with sucess " + to);
        } catch (MessagingException e) {
            logger.warn("Error to send email " + to);
            e.printStackTrace();
        }
    }
}
