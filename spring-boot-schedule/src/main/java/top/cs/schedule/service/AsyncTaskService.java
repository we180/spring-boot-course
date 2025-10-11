package top.cs.schedule.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AsyncTaskService {

    // @Async：指定使用异步线程池执行任务
    @Async("asyncTaskExecutor")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void asyncScheduledTask() {
        log.info("【异步定时任务】执行线程：{}，时间：{}",
                Thread.currentThread().getName(), LocalDateTime.now());
        // 模拟耗时操作（10秒）
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
