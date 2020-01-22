package ru.test.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.test.jms.model.SendConfirm;
import ru.test.persistence.entity.User;

@Component
public class JmsRegistrationConfirmSender {
    private final static Logger logger = LoggerFactory.getLogger(JmsRegistrationConfirmSender.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jms.queue.confirm.send}")
    private String queue;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public void send(User user) {
        SendConfirm sendConfirm = new SendConfirm();
        sendConfirm.setUserId(user.getId());
        sendConfirm.setLogin(user.getLogin());
        sendConfirm.setEmail(user.getEmail());
        sendConfirm.setFullName(user.getFullName());
        try {
            String message = objectMapper.writeValueAsString(sendConfirm);
            if (logger.isDebugEnabled()) {
                logger.debug("Send message: " + message);
            }
            jmsTemplate.convertAndSend(queue, message);
        } catch (JsonProcessingException e) {
            logger.error("Error writing json for user: " + user.getLogin(), e);
        }
    }
}
