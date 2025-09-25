package top.cs.boot.redis.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Slf4j
@TestPropertySource(locations = "classpath:application.yml")
public class LoginControllerTest {
    @Test
    public void testLoginFlow() {
        log.info("登录接口测试");
        log.info("1. 先调用发送短信接口: GET /sms?phone=13800138000");
        log.info("2. 再调用登录接口: POST /login");
        log.info(" 请求体示例:");
        log.info("  {");
        log.info("  \"phone\": \"13800138000\",");
        log.info("  \"code\": \"123456\"");
        log.info("  }");
    }
}