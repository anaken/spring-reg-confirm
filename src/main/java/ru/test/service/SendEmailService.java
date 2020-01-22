package ru.test.service;

import ru.test.service.dto.SendEmailDto;

/**
 * Сервис по отправке писем
 */
public interface SendEmailService {
    /**
     * Асинхронная отправка письма, чтоб иметь возможность несколько раз попытаться отправить письмо
     *
     * @param emailDto информация о письме
     */
    void sendEmailAsync(SendEmailDto emailDto);

    /**
     * Запуск отправки писем
     */
    void sendEmails();
}
