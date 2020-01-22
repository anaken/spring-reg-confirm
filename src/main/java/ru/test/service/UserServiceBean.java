package ru.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.test.jms.JmsRegistrationConfirmSender;
import ru.test.persistence.entity.User;
import ru.test.persistence.enums.UserStatus;
import ru.test.persistence.repository.UserRepository;
import ru.test.service.dto.ConfirmRegistrationDto;
import ru.test.service.dto.SendEmailDto;
import ru.test.web.dto.UserDto;

@Service
public class UserServiceBean implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JmsRegistrationConfirmSender jmsSender;

    @Autowired
    private SendEmailServiceBean sendEmailService;

    @Value("${email.sender.from}")
    private String sendFrom;

    @Override
    @Transactional
    public User createUser(UserDto userDto) {
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setStatus(UserStatus.CREATED);
        return userRepository.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendToConfirmRegistration(String userId) {
        sendToConfirmRegistration(userId, false);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendToConfirmRegistration(String userId, boolean fail) {
        User user = userRepository.getOne(userId);
        user.setStatus(UserStatus.WAIT_CONFIRM);
        userRepository.save(user);
        jmsSender.send(user);
        if (fail) {
            throw new IllegalStateException("test");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void confirmRegistration(ConfirmRegistrationDto confirm) {
        User user = userRepository.getOne(confirm.getUserId());
        user.setStatus(confirm.isAccepted() ? UserStatus.CONFIRMED : UserStatus.REJECTED);
        userRepository.save(user);
        sendEmailService.sendEmailAsync(createSendEmailDto(user, confirm));
    }

    private SendEmailDto createSendEmailDto(User user, ConfirmRegistrationDto confirm) {
        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setFrom(sendFrom);
        sendEmailDto.setTo(user.getEmail());
        sendEmailDto.setSubject("Registration " + (confirm.isAccepted() ? "accepted" : "rejected"));
        sendEmailDto.setText("Dear user " + user.getLogin() + " thanks for using this service!");
        return sendEmailDto;
    }
}
