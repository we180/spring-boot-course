package top.cs.schedule.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

@Slf4j
public class GlobalJobListener implements JobListener {
    @Override
    public String getName() {
        // 监听器名称
        return "globalJobListener";
    }

    // 任务执行前调用
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("【任务监听】任务即将执行：{}", context.getJobDetail().getKey());
    }

    // 任务执行后调用（无论成功/失败）
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        if (jobException != null) {
            log.error("【任务监听】任务执行失败：{}，异常：{}", context.getJobDetail().getKey(), jobException.getMessage());
        }
    }

    // 任务被否决时调用（如并发控制）
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.warn("【任务监听】任务被否决执行：{}", context.getJobDetail().getKey());
    }
}
