package ru.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.test.TestBase;
import ru.test.jms.model.SendConfirm;
import ru.test.persistence.entity.User;
import ru.test.persistence.enums.UserStatus;
import ru.test.web.dto.UserDto;

import javax.jms.Message;
import javax.jms.TextMessage;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceBeanTest extends TestBase {

    @Test
    public void testCreateUser() {
        UserDto userDto = createUser();
        User user = userService.createUser(userDto);
        assertNotNull(user.getId());
        assertEquals("test@mail.ru", user.getEmail());
    }

    @Test
    public void testSendToConfirmRegistrationSuccess() throws Exception {
        UserDto userDto = createUser();
        User user = userService.createUser(userDto);
        userService.sendToConfirmRegistration(user.getId());
        user = findUserById(user.getId());
        assertEquals(UserStatus.WAIT_CONFIRM, user.getStatus());
        Message message = jmsTemplate.receive(sendQueue);
        SendConfirm sendConfirm = objectMapper.readValue(((TextMessage) message).getText(), SendConfirm.class);
        assertEquals("testLogin", sendConfirm.getLogin());
    }

    @Test
    public void testSendToConfirmRegistrationFail() {
        UserDto userDto = createUser();
        User user = userService.createUser(userDto);
        try {
            userService.sendToConfirmRegistration(user.getId(), true);
            fail("Expected exception not thrown");
        } catch (Exception ignored) {
        }
        user = findUserById(user.getId());
        assertEquals(UserStatus.CREATED, user.getStatus());
    }

    private UserDto createUser() {
        UserDto userDto = new UserDto();
        userDto.setLogin("testLogin");
        userDto.setPassword("pass");
        userDto.setEmail("test@mail.ru");
        userDto.setFullName("fullname");
        return userDto;
    }

    private User findUserById(String id) {
        return jdbcTemplate.queryForObject("select * from user where id = ?", new Object[]{id}, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setLogin(rs.getString("login"));
            user.setEmail(rs.getString("email"));
            user.setFullName(rs.getString("full_name"));
            user.setStatus(UserStatus.valueOf(rs.getString("status")));
            return user;
        });
    }
}