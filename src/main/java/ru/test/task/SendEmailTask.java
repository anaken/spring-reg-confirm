package ru.test.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.test.service.SendEmailService;

@Service
public class SendEmailTask {
    @Autowired
    private SendEmailService sendEmailService;

    /**
     * Задание по рассылке писем ожидающих отправки
     */
    @Scheduled(cron = "${email.sender.cron.expression}")
    public void sendEmails() {
        sendEmailService.sendEmails();
    }
}
