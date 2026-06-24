package com.example.film_service.service;

import com.example.film_service.exception.EmailSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.URLConnection;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private static final long MAX_ATTACHMENT_SIZE = 25 * 1024 * 1024L;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final JavaMailSender mailSender;

    public void sendReport(
            String toEmail,
            String subject,
            String body,
            String attachmentName,
            byte[] attachment) {

        if (toEmail == null || toEmail.isBlank()) {
            throw new IllegalArgumentException("Email получателя не может быть пустым");
        }

        if (!EMAIL_PATTERN.matcher(toEmail).matches()) {
            throw new IllegalArgumentException("Некорректный формат email: " + toEmail);
        }

        Objects.requireNonNull(attachmentName, "Имя вложения не может быть null");
        Objects.requireNonNull(attachment, "Вложение не может быть null");

        if (attachment.length > MAX_ATTACHMENT_SIZE) {
            throw new IllegalArgumentException("Размер вложения превышает " + MAX_ATTACHMENT_SIZE + " байт");
        }

        String contentType = URLConnection.guessContentTypeFromName(attachmentName);
        if (contentType == null) {
            if (attachmentName.endsWith(".csv")) {
                contentType = "text/csv; charset=UTF-8";
            } else if (attachmentName.endsWith(".xml")) {
                contentType = "application/xml; charset=UTF-8";
            } else {
                contentType = "application/octet-stream";
            }
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);
            helper.addAttachment(
                    attachmentName,
                    new ByteArrayResource(attachment),
                    contentType
            );

//            helper.addAttachment(attachmentName, new DataSource() {
//
//                @Override
//                public InputStream getInputStream() throws IOException {
//                    return new ByteArrayInputStream(attachment);
//                }
//
//                @Override
//                public OutputStream getOutputStream() throws IOException {
//                    throw new UnsupportedOperationException();
//                }
//
//                @Override
//                public String getContentType() {
//                    return "text/csv";
//                }
//
//                @Override
//                public String getName() {
//                    return attachmentName;
//                }
//            });

            mailSender.send(message);

            log.info("Отчёт успешно отправлен на {}", toEmail);

        } catch (MessagingException e) {
            log.error("Ошибка при отправке письма на {}: {}", toEmail, e.getMessage(), e);
            throw new EmailSendException("Ошибка при отправке письма", e);
        }
    }
}
