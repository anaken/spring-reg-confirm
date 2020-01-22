package ru.test.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.test.TestBase;
import ru.test.persistence.entity.SendEmail;
import ru.test.persistence.enums.SendEmailStatus;
import ru.test.service.dto.SendEmailDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SendEmailServiceBeanTest extends TestBase {

    @After
    public void after() {
        jdbcTemplate.execute("delete from send_email");
    }

    @Test
    public void testSendEmailSuccess() {
        SendEmailDto sendEmailDto = createSendEmailDto();
        sendEmailService.sendEmailAsync(sendEmailDto);
        List<SendEmail> list = findAll();
        assertEquals(1, list.size());
        SendEmail sendEmail = list.get(0);
        assertEquals(SendEmailStatus.CREATED, sendEmail.getStatus());

        sendEmailService.sendEmails();

        sendEmail = findSendEmailById(sendEmail.getId());
        assertEquals(SendEmailStatus.SENT, sendEmail.getStatus());
        assertNotNull(sendEmail.getSent());
    }

    @Test
    public void testSendEmailFail() {
        sendEmailService.sendEmailAsync(createSendEmailDto());
        List<SendEmail> list = findAll();
        assertEquals(1, list.size());
        SendEmail sendEmail = list.get(0);

        sendEmailService.sendEmails(true);

        sendEmail = findSendEmailById(sendEmail.getId());
        assertEquals(1L, (long) sendEmail.getAttempt());
        assertEquals(SendEmailStatus.CREATED, sendEmail.getStatus());

        jdbcTemplate.update("update send_email set attempt = ? where id = ?",
                new Object[]{maxAttempts, sendEmail.getId()});

        sendEmailService.sendEmails(true);

        sendEmail = findSendEmailById(sendEmail.getId());
        assertEquals(SendEmailStatus.FAILED, sendEmail.getStatus());
    }

    private SendEmailDto createSendEmailDto() {
        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setFrom("from@mail.ru");
        sendEmailDto.setTo("to@mail.ru");
        sendEmailDto.setSubject("test subject");
        sendEmailDto.setText("test text");
        return sendEmailDto;
    }


    private SendEmail findSendEmailById(String id) {
        return jdbcTemplate.queryForObject("select * from send_email where id = ?", new Object[]{id},
                (rs, rowNum) -> map(rs));
    }

    private List<SendEmail> findAll() {
        return jdbcTemplate.query("select * from send_email", (rs, rowNum) -> map(rs));
    }

    private SendEmail map(ResultSet rs) throws SQLException {
        SendEmail sendEmail = new SendEmail();
        sendEmail.setId(rs.getString("id"));
        sendEmail.setFromEmail(rs.getString("from_email"));
        sendEmail.setToEmail(rs.getString("to_email"));
        sendEmail.setSubject(rs.getString("subject"));
        sendEmail.setBodyText(rs.getString("body_text"));
        sendEmail.setCreated(rs.getDate("created"));
        sendEmail.setSent(rs.getDate("sent"));
        sendEmail.setAttempt(rs.getInt("attempt"));
        sendEmail.setStatus(SendEmailStatus.valueOf(rs.getString("status")));
        return sendEmail;
    }
}