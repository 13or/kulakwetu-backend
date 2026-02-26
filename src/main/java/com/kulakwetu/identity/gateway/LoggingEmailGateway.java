package com.kulakwetu.identity.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class LoggingEmailGateway implements EmailGateway {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public LoggingEmailGateway(
            JavaMailSender mailSender,
            @Value("${app.identity.mail-from:${spring.mail.username:no-reply@kulakwetu.local}}") String fromAddress
    ) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    public void sendVerificationLink(String email, String verificationLink) {
        String subject = "Confirmez votre compte Kulakwetu";
        String htmlBody = """
                <html>
                  <body style="font-family: Arial, sans-serif; line-height: 1.5; color: #222;">
                    <h2>Bienvenue sur Kulakwetu</h2>
                    <p>Merci pour votre inscription. Cliquez sur le lien ci-dessous pour confirmer votre compte :</p>
                    <p><a href="%s">Confirmer mon compte</a></p>
                    <p>Si vous n'êtes pas à l'origine de cette demande, ignorez cet email.</p>
                  </body>
                </html>
                """.formatted(verificationLink);

        sendHtml(email, subject, htmlBody, "verification");
    }

    @Override
    public void sendPasswordResetLink(String email, String resetLink) {
        String subject = "Réinitialisation de votre mot de passe Kulakwetu";
        String htmlBody = """
                <html>
                  <body style="font-family: Arial, sans-serif; line-height: 1.5; color: #222;">
                    <h2>Réinitialiser votre mot de passe</h2>
                    <p>Nous avons reçu une demande de réinitialisation de mot de passe pour votre compte.</p>
                    <p><a href="%s">Réinitialiser mon mot de passe</a></p>
                    <p>Si vous n'êtes pas à l'origine de cette demande, ignorez cet email.</p>
                  </body>
                </html>
                """.formatted(resetLink);

        sendHtml(email, subject, htmlBody, "password reset");
    }

    private void sendHtml(String email, String subject, String htmlBody, String emailType) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            helper.setTo(email);
            helper.setFrom(fromAddress);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);

            log.info("{} email sent to {}", emailType, email);
        } catch (MailException | jakarta.mail.MessagingException e) {
            log.error("Failed to send {} email to {}", emailType, email, e);
            throw new IllegalStateException("Unable to send " + emailType + " email", e);
        }
    }
}
