package ru.test.service;

import ru.test.persistence.entity.User;
import ru.test.service.dto.ConfirmRegistrationDto;
import ru.test.web.dto.UserDto;

/**
 * Сервис по работе с пользователями
 */
public interface UserService {
    /**
     * Создать нового пользователя
     * @param userDto информация о пользователе
     * @return пользователь
     */
    User createUser(UserDto userDto);

    /**
     * Отправить информацию для подтверждения регистрации пользователя
     * @param userId ID пользователя
     */
    void sendToConfirmRegistration(String userId);

    /**
     * Подтвердить регистрацию
     * @param confirm информация о подтверждении
     */
    void confirmRegistration(ConfirmRegistrationDto confirm);
}
