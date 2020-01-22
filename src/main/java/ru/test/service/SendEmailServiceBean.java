package ru.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.test.persistence.entity.SendEmail;
import ru.test.persistence.enums.SendEmailStatus;
import ru.test.persistence.repository.SendEmailRepository;
import ru.test.service.dto.SendEmailDto;

import java.util.Date;
import java.util.List;

@Service
public class SendEmailServiceBean implements SendEmailService {
    private final static Logger logger = LoggerFactory.getLogger(SendEmailServiceBean.class);

    @Autowired
    private SendEmailRepository sendEmailRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.sender.maxAttempts}")
    private int maxAttempts;

    @Override
    public void sendEmailAsync(SendEmailDto emailDto) {
        SendEmail sendEmail = new SendEmail();
        sendEmail.setAttempt(0);
        sendEmail.setStatus(SendEmailStatus.CREATED);
        sendEmail.setCreated(new Date());
        sendEmail.setFromEmail(emailDto.getFrom());
        sendEmail.setToEmail(emailDto.getTo());
        sendEmail.setSubject(emailDto.getSubject());
        sendEmail.setBodyText(emailDto.getText());
        sendEmailRepository.save(sendEmail);
    }

    @Override
    public void sendEmails() {
        sendEmails(false);
    }

    public void sendEmails(boolean fail) {
        Pageable topTen = PageRequest.of(0, 10, Sort.Direction.ASC, "created");
        List<SendEmail> list = sendEmailRepository.findNotSent(topTen);
        for (SendEmail sendEmail : list) {
            try {
                if (fail) {
                    throw new IllegalStateException("fail");
                }
                mailSender.send(createEmailMessage(sendEmail));
                sendEmail.setSent(new Date());
                sendEmail.setStatus(SendEmailStatus.SENT);
                sendEmailRepository.save(sendEmail);
            } catch (Exception e) {
                logger.warn(String.format("Error occurred while sending email to %s", sendEmail.getToEmail()), e);
                int attempt = sendEmail.getAttempt() + 1;
                sendEmail.setAttempt(attempt);
                if (attempt >= maxAttempts) {
                    sendEmail.setStatus(SendEmailStatus.FAILED);
                }
                sendEmailRepository.save(sendEmail);
            }
        }
    }

    private SimpleMailMessage createEmailMessage(SendEmail sendEmail) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(sendEmail.getToEmail());
        email.setSubject(sendEmail.getSubject());
        email.setText(sendEmail.getBodyText());
        email.setFrom(sendEmail.getFromEmail());
        return email;
    }
}
