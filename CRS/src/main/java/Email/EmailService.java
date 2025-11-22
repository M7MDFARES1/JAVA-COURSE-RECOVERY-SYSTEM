package Email;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;



public class EmailService {
 
          // ------------------- CONFIG -------------------
    private static final String SENDER_EMAIL = "mohamedfares9561@gmail.com"; 
    private static final String APP_PASSWORD = "dmck qydq yhwh slhe";     

    // ------------------- SESSION CREATOR -------------------
    private Session createSession() {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });
    }

    // ------------------- BASIC SENDING METHOD -------------------
    public void sendEmail(String to, String subject, String messageText) {
        try {
            Message message = new MimeMessage(createSession());
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);
            System.out.println("EMAIL SENT SUCCESSFULLY to: " + to);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------- READY-MADE EMAIL ACTIONS -------------------

    // 1) When user registers
    public void sendRegistrationEmail(String to, String name) {
        String subject = "Welcome to CRS System";
        String msg = "Hello " + name + ",\n\nYour account has been successfully created.\n\nRegards,\nCRS System";
        sendEmail(to, subject, msg);
    }

    // 2) When user resets password
    public void sendPasswordResetEmail(String to, String name) {
        String subject = "CRS Password Reset";
        String msg = "Hello " + name + ",\n\nYour password has been reset.\nIf this was not you, please contact support.\n\n- CRS System";
        sendEmail(to, subject, msg);
    }

    // 3) When course recovery plan is updated
    public void sendCourseRecoveryUpdate(String to, String details) {
        String subject = "Course Recovery Update";
        String msg = "Your course recovery plan has been updated:\n\n" + details + "\n\nGood luck!";
        sendEmail(to, subject, msg);
    }

    // 4) When academic report is ready
    public void sendReportReadyEmail(String to) {
        String subject = "Academic Report Ready";
        String msg = "Your academic performance report is now ready.\nPlease check the CRS system.\n\n- CRS System";
        sendEmail(to, subject, msg);
    }
 
}
