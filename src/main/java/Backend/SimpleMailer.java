/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author KT
 */
public class SimpleMailer {
    public static void sendEmail(String to, String subject, String messageText) {
        final String from = "venuthiru185@gmail.com"; // 👈 your Gmail
        final String appPassword = "dwkg ykem hcwd xnpf"; // 👈 your App Password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);
            System.out.println("✅ Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Test run
    public static void main(String[] args) {
        String re = "venuthiru185@gmail.com";
        sendEmail(re, "Your FastTrack Shipment", "Hello! Your package is on the way 🚚");
    }
    
}
