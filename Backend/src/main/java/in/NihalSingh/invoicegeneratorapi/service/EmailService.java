package in.NihalSingh.invoicegeneratorapi.service;

import in.NihalSingh.invoicegeneratorapi.entity.Invoice;
import in.NihalSingh.invoicegeneratorapi.entity.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final PdfService pdfService;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Value("${admin.email}")
    private String adminEmail;

    // ===============================
    // REGISTRATION MAIL
    // ===============================
    public void sendRegistrationMail(String to, String token) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Verify Your Account");
            helper.setText(
                    "Your registration token:\n\n" +
                            token +
                            "\n\nToken valid for 30 minutes."
            );

            mailSender.send(msg);
        } catch (Exception e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }

    // ===============================
    // WELCOME MAIL
    // ===============================
    public void sendWelcomeMail(String email) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Welcome");
            helper.setText("Your account has been successfully activated.");

            mailSender.send(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ===============================
    // ADMIN NOTIFICATION
    // ===============================
    public void notifyAdmin(User user) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);

            helper.setFrom(fromEmail);
            helper.setTo(adminEmail);
            helper.setSubject("New User Registered");

            helper.setText(
                    "Name: " + user.getFirstName() +
                            "\nEmail: " + user.getEmail()
            );

            mailSender.send(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ===============================
    // INVOICE MAIL
    // ===============================
    public void sendInvoiceEmail(String to, Invoice invoice) {
        try {
            byte[] pdf = pdfService.generateInvoicePdf(invoice);

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Invoice #" + invoice.getId());
            helper.setText("Invoice attached");

            helper.addAttachment(
                    "invoice.pdf",
                    new ByteArrayResource(pdf)
            );

            mailSender.send(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // SEND PASSWORD RESET MAIL
    public void sendPasswordResetMail(String email, String token) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Reset Password");

            helper.setText(
                    "Use this token to reset your password:\n\n" +
                            token +
                            "\n\nToken valid for 15 minutes."
            );

            mailSender.send(msg);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send reset mail", e);
        }
    }


}
