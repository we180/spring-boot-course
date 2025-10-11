package top.cs.schedule.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cs.schedule.job.ExportUserAccount;

@Configuration
public class QuartzConfig {
    /**
     * 定时任务详情
     */
    @Bean
    public JobDetail exportTaskDetail() {
        //通过 JobBuilder 创建了一个名为 "ExportJob" 的定时任务详情（JobDetail）实例
        // 该任务基于 ExportUserAccount 类，设置为可持久化存储，并最终调用 build() 方法完成构建。
        return JobBuilder.newJob(ExportUserAccount.class).withIdentity("ExportJob").storeDurably().build();
    }

    @Bean
    public Trigger exportTaskTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 39 14 ? * *");
        // 返回任务触发器
        return TriggerBuilder.newTrigger().forJob(exportTaskDetail()).withIdentity("ExportJob").withSchedule(scheduleBuilder).build();
    }
}