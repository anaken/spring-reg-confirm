package ru.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import ru.test.persistence.repository.SendEmailRepository;
import ru.test.service.SendEmailServiceBean;
import ru.test.service.UserServiceBean;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class TestBase {

    @Autowired
    protected ApplicationContext context;

    @Autowired
    protected PlatformTransactionManager transactionManager;

    @Autowired
    protected UserServiceBean userService;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected JmsTemplate jmsTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected SendEmailServiceBean sendEmailService;

    @Autowired
    protected SendEmailRepository sendEmailRepository;

    @Value("${email.sender.maxAttempts}")
    protected int maxAttempts;

    @Value("${jms.queue.confirm.send}")
    protected String sendQueue;

//    @Test
//    public void testContext() {
//    }
}
