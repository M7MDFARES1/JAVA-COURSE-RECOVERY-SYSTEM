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

    

    // 1) When user registers
     public void sendUserAccountCreated(
            String to,
            String username,
            String role,
            String defaultPassword
    ) {
        String subject = "Your CRS Account Has Been Created";

        String msg = "Hello " + username + ",\n\n"
                + "Your CRS system account has been successfully created.\n\n"
                + "ROLE: " + role + "\n"
                + "USERNAME: " + username + "\n"
                + "DEFAULT PASSWORD: " + defaultPassword + "\n\n"
                + "IMPORTANT:\n"
                + "Please log in using the default password and\n"
                + "CHANGE YOUR PASSWORD IMMEDIATELY after your first login.\n\n"
                + "If you have any issues, contact the system administrator.\n\n"
                + "Regards,\nCRS System";

        sendEmail(to, subject, msg);
    }
    
   public void sendUpdatedEmail(String to, String name, String updatedFields) {
    String subject = "Your CRS Account Has Been Updated";

    String msg = "Hello " + name + ",\n\n"
            + "The following changes have been made to your CRS account:\n"
            + updatedFields + "\n\n"
            + "If you did not request these changes, please contact the administrator immediately.\n\n"
            + "Regards,\nCRS System";

    sendEmail(to, subject, msg);
}

    public void sendDeactivatedEmail(String to, String name) {
        String subject = "Account removed";
        String msg = "Hello " + name + ",\n\nYour account has been removed from the system.\n\nRegards,\nCRS System";
        sendEmail(to, subject, msg);
    }

    // 2) When user resets password
    public void sendPasswordResetEmail(String to, String name) {
        String subject = "CRS Password Reset";
        String msg = "Hello " + name + ",\n\nYour password has been reset.\nIf this was not you, please contact support.\n\n- CRS System";
        sendEmail(to, subject, msg);
    }

    // 3) course recovery plan 
       public void sendRecoveryPlanDetails(
            String studentEmail,
            String studentName,
            String courseName,
            String failedComponents,
            String planDetails,
            String deadlines
    ) {
        String subject = "Your Course Recovery Plan - " + courseName;

        String msg = "Hello " + studentName + ",\n\n"
                + "Here are your recovery details for: " + courseName + "\n\n"
                + "FAILED COMPONENTS:\n" + failedComponents + "\n\n"
                + "RECOVERY PLAN:\n" + planDetails + "\n\n"
                + "DEADLINES:\n" + deadlines + "\n\n"
                + "Please follow the plan to stay on track.\n\n"
                + "Regards,\nCRS System";

        sendEmail(studentEmail, subject, msg);
    }

    // 4) When academic report is ready
   public void sendAcademicReport(
            String studentEmail,
            String studentName,
            String semester,
            String courseGrades,
            double cgpa
    ) {
        String subject = "Your Academic Performance Report (" + semester + ")";

        String msg = "Hello " + studentName + ",\n\n"
                + "Here is your academic performance report for " + semester + ":\n\n"
                + courseGrades + "\n"
                + "CGPA: " + cgpa + "\n\n"
                + "Regards,\nCRS System";

        sendEmail(studentEmail, subject, msg);
    }
   
 
}