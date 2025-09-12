package top.cs.boot.config.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.cs.boot.config.model.Mail;

@SpringBootTest
class MailServiceTest {
    @Resource
    private MailService mailService;

    @Test
    void sendSimpleMail() {
        Mail mail = new Mail();
        mail.setTo("1805825699@qq.com");
        mail.setSubject("测试邮件0721");
        mail.setText("测试邮件内容114514");
        mailService.sendSimpleMail(mail);
    }
}