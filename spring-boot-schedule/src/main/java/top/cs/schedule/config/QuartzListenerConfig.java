package top.cs.schedule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import top.cs.schedule.listener.GlobalJobListener;

@Configuration
public class QuartzListenerConfig {
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // 注册全局监听器
        factory.setGlobalJobListeners(new GlobalJobListener());
        return factory;
    }
}
