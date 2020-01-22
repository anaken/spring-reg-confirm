package ru.test.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.test.persistence.entity.User;
import ru.test.service.UserService;
import ru.test.web.dto.UserDto;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    private final static Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    @PostMapping
    public void register(@RequestBody UserDto userDto) {
        // Сохранение информации о пользователе
        User user = userService.createUser(userDto);
        try {
            // В новой транзакции попытка отправить на подтверждение и сменить статус
            userService.sendToConfirmRegistration(user.getId());
        } catch (Exception e) {
            logger.error("Error occurred while sending user to confirmation: " + e.getMessage(), e);
        }
    }
}
