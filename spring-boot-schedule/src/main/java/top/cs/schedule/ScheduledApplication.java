package top.cs.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// 关键注解：开启定时任务支持
@EnableScheduling
@SpringBootApplication
public class ScheduledApplication {
    public static void main(String[] args) {
        System.setProperty("oshi.os.windows.cpu.load.average", "false");
        SpringApplication.run(ScheduledApplication.class, args);
    }
}