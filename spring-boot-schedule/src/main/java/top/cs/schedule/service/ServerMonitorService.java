package top.cs.schedule.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.platform.windows.WindowsHardwareAbstractionLayer;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class ServerMonitorService {

    private final HardwareAbstractionLayer hardware;
    // 格式化保留2位小数
    private final DecimalFormat df = new DecimalFormat("#.00");

    @Value("${server.monitor.cpu-threshold}")
    private double cpuThreshold;

    @Value("${server.monitor.memory-threshold}")
    private double memoryThreshold;

    /**
     * 初始化服务器硬件信息
     */
    public ServerMonitorService() {
        // Oshi：初始化硬件抽象层（获取CPU/内存信息）
        // Windows 用：WindowsHardwareAbstractionLayer，Linux用：LinuxHardwareAbstractionLayer
        this.hardware = new WindowsHardwareAbstractionLayer();
    }

    /**
     * 服务器监控任务，每隔5分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void monitorServerHealth() {
        try {
            // 1. 获取CPU使用率（%）
            double cpuUsage = getCpuUsage();
            // 2. 获取内存使用率（%）
            double memoryUsage = getMemoryUsage();
            // 3. 打印监控日志
            String monitorLog = String.format("【服务器监控】时间：%s，CPU使用率：%s%%，内存使用率：%s%%，阈值：CPU<%s%%、内存<%s%%", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), df.format(cpuUsage), df.format(memoryUsage), cpuThreshold, memoryThreshold);
            log.info(monitorLog);
            // 4. 检查是否超过阈值，若超过则发送告警
            // 实际可以发送邮件、短信，或通过钉钉机器人自动告警
            if (cpuUsage > cpuThreshold) {
                log.warn("【告警】CPU使用率超过阈值！当前：{}%，阈值：{}%", df.format(cpuUsage), cpuThreshold);
            }
            if (memoryUsage > memoryThreshold) {
                log.warn("【告警】内存使用率超过阈值！当前：{}%，阈值：{}%", df.format(memoryUsage), memoryThreshold);
            }
        } catch (Exception e) {
            log.error("【服务器监控】执行失败", e);
        }
    }

    /**
     * 获取CPU使用率（Oshi工具）
     *
     * @return CPU使用率（%）
     */
    private double getCpuUsage() {
        // 获取CPU信息
        CentralProcessor processor = hardware.getProcessor();
        // 获取CPU使用率，delay的作用是获取CPU使用率时，CPU空闲时间间隔
        double systemCpuLoad = processor.getSystemCpuLoad(1000);
        // 返回CPU使用率（%）
        return systemCpuLoad * 100;
    }

    /**
     * 获取内存使用率（Oshi工具）
     *
     * @return 内存使用率（%）
     */
    private double getMemoryUsage() {
        // 获取内存信息
        GlobalMemory memory = hardware.getMemory();
        // 总内存（字节）
        long totalMemory = memory.getTotal();
        // 已使用内存（字节）
        long usedMemory = totalMemory - memory.getAvailable();
        // 计算使用率（%）
        return (double) usedMemory / totalMemory * 100;
    }

}
