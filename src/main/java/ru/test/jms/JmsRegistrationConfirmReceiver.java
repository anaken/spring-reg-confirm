package ru.test.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.test.jms.model.ReceiveConfirm;
import ru.test.service.UserService;
import ru.test.service.dto.ConfirmRegistrationDto;

@Component
public class JmsRegistrationConfirmReceiver {
    private final static Logger logger = LoggerFactory.getLogger(JmsRegistrationConfirmReceiver.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @JmsListener(destination = "receive")
    public void receive(String confirmJson) {
        if (logger.isDebugEnabled()) {
            logger.debug("Receive message: " + confirmJson);
        }
        ConfirmRegistrationDto confirmDto;
        try {
            confirmDto = getConfirmRegistrationDto(confirmJson);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error reading incoming message: " + confirmJson, e);
        }
        userService.confirmRegistration(confirmDto);
    }

    private ConfirmRegistrationDto getConfirmRegistrationDto(String confirmJson) throws JsonProcessingException {
        ReceiveConfirm confirm = objectMapper.readValue(confirmJson, ReceiveConfirm.class);
        ConfirmRegistrationDto confirmDto = new ConfirmRegistrationDto();
        confirmDto.setUserId(confirm.getUserId());
        confirmDto.setAccepted(Boolean.TRUE.equals(confirm.getAccepted()));
        return confirmDto;
    }
}
