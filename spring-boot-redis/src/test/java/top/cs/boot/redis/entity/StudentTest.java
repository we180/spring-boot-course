package top.cs.boot.redis.entity;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class StudentTest {
    @Resource
    private RedisTemplate<String, Student> redisTemplate;

    @Test
    void testStudent() {
        Address address = Address.builder().province("江苏省").city("徐州市").build();
        Student student = Student.builder().id("1").name("cs").address(address).build();
        redisTemplate.opsForValue().set("stu1", student,20, TimeUnit.SECONDS);
    }
}