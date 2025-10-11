package top.cs.websocket.handler;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.platform.windows.WindowsHardwareAbstractionLayer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SimpleTimeWebSocketHandler implements WebSocketHandler {
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private HardwareAbstractionLayer hardware = new SystemInfo().getHardware();


    // 从配置文件注入参数
    @Value("${weather.api.key}")
    private String weatherApiKey;

    @Value("${weather.api.city}")
    private String cityId;

    @Value("${weather.api.url}")
    private String weatherApiUrl;




    // 使用线程安全的 ConcurrentHashMap 存储 WebSocket 会话
    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    // 日期时间格式化器
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SESSIONS.put(session.getId(), session);
        log.info("新的WebSocket连接建立，会话ID: {}, 当前连接数: {}", session.getId(), SESSIONS.size());
        // 连接建立后立即发送一条欢迎消息
        String welcomeMessage = "🎉 欢迎连接时间推送服务+天气信息+CPU/内存占用率！\n";
        sendMsg(session, welcomeMessage);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 处理客户端发送的消息
        String payload = message.getPayload().toString();
        log.info("收到客户端消息: {}, 会话ID: {}", payload, session.getId());
        if ("ping".equalsIgnoreCase(payload.trim())) {
            sendMsg(session, "pong");
        } else {
            String response = "收到消息: " + payload + "\n发送 'ping' 测试连接";
            sendMsg(session, response);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误，会话ID: {}", session.getId(), exception);
        SESSIONS.remove(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        SESSIONS.remove(session.getId());
        log.info("WebSocket连接关闭，会话ID: {}, 关闭状态: {}, 当前连接数: {}", session.getId(), closeStatus, SESSIONS.size());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 定时任务：每5秒钟推送当前时间
     */
    @Scheduled(fixedRate = 5000)
    public void sendPeriodicMood() {
        if (SESSIONS.isEmpty()) {
            log.debug("当前没有活跃的WebSocket连接");
            return;
        }
        log.info("开始执行定时时间推送任务，当前连接数: {}", SESSIONS.size());
        String timeInfo = String.format("⏰ %s", LocalDateTime.now().format(timeFormatter));
        // 向所有连接的客户端推送消息
        SESSIONS.values().removeIf(session -> {
            try {
                if (session.isOpen()) {
                    // 调用下面封装的私有方法，向指定的会话发送消息
                    sendMsg(session, timeInfo);
                    // 保留会话
                    return false;
                } else {
                    log.warn("发现已关闭的会话，将其移除: {}", session.getId());
                    // 移除会话
                    return true;
                }
            } catch (Exception e) {
                log.error("发送消息失败，移除会话: {}", session.getId(), e);
                // 移除有问题的会话
                return true;
            }
        });
    }

    /**
     * 发送消息到指定的WebSocket会话
     *
     * @param session WebSocket会话
     * @param message 要发送的消息
     */
    private void sendMsg(WebSocketSession session, String message) {
        try {
            if (session.isOpen()) {
                TextMessage textMessage = new TextMessage(message);
                session.sendMessage(textMessage);
                log.debug("消息发送成功，会话ID: {}", session.getId());
            }
        } catch (Exception e) {
            log.error("发送消息失败，会话ID: {}", session.getId(), e);
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendDailyWeather() {
        // 向所有连接的客户端推送消息


        try {
            // 1. 调用和风天气API获取天气预报
            String weatherResponse = getWeatherData();
            if (weatherResponse == null) {
                log.error("【天气早报】获取天气数据失败");
                return;
            }
            // 2. 解析API响应（提取当天天气）
            JSONObject weatherJson = JSONObject.parseObject(weatherResponse);
            JSONObject todayWeather = weatherJson.getJSONArray("daily").getJSONObject(0);
            // 提取天气相关信息
            // 日期
            String date = todayWeather.getString("fxDate");
            // 最高温
            String tempMax = todayWeather.getString("tempMax");
            // 最低温
            String tempMin = todayWeather.getString("tempMin");
            // 白天天气（如“晴”）
            String textDay = todayWeather.getString("textDay");
            // 白天风向
            String windDirDay = todayWeather.getString("windDirDay");
            // 白天风力
            String windScaleDay = todayWeather.getString("windScaleDay");

            // 3. 构造邮件内容
            if (SESSIONS.isEmpty()) {
                log.debug("当前没有活跃的WebSocket连接");
                return;
            }
            log.info("开始执行定时时间推送任务，当前连接数: {}", SESSIONS.size());
            String subject = String.format("【每日天气早报】%s 南京天气", LocalDate.parse(date).format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
            String content = String.format("""
                    📅 日期：%s
                    🌤️ 天气：%s
                    🌡️ 温度：%s℃ ~ %s℃
                    💨 风向：%s
                    🌬️ 风力：%s级
                    💡 提示：出门请根据天气增减衣物，注意交通安全！""", date, textDay, tempMin, tempMax, windDirDay, windScaleDay);

            SESSIONS.values().removeIf(session -> {
                try {
                    if (session.isOpen()) {
                        // 调用下面封装的私有方法，向指定的会话发送消息
                        sendMsg(session, subject);
                        sendMsg(session, content);
                        // 保留会话
                        return false;
                    } else {
                        log.warn("发现已关闭的会话，将其移除: {}", session.getId());
                        // 移除会话
                        return true;
                    }
                } catch (Exception e) {
                    log.error("发送消息失败，移除会话: {}", session.getId(), e);
                    // 移除有问题的会话
                    return true;
                }
            });

        } catch (Exception e) {
            log.error("【天气早报】执行失败", e);
        }

    }


    /**
     * 调用和风天气API,获取3天天气预报
     *
     * @return API响应（JSON格式）
     */
    private String getWeatherData() throws IOException {
        // 构造API请求URL（含城市ID和API密钥）
        String requestUrl = String.format("%s?location=%s&key=%s", weatherApiUrl, cityId, weatherApiKey);
//        log.info("【天气早报】API请求URL：{}", requestUrl);
        Request request = new Request.Builder().url(requestUrl).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // 只读取一次 responseBody
                String responseBody = response.body().string();
                log.info("【天气早报】API响应：{}", responseBody);
                return responseBody;
            }
            log.error("【天气早报】API请求失败，状态码：{}", response.code());
            return null;
        }
    }


    // 格式化保留2位小数
    private final DecimalFormat df = new DecimalFormat("#.00");

    @Value("${server.monitor.cpu-threshold}")
    private double cpuThreshold;

    @Value("${server.monitor.memory-threshold}")
    private double memoryThreshold;

    /**
     * 初始化服务器硬件信息
     */
    public void ServerMonitorService() {
        // Oshi：初始化硬件抽象层（获取CPU/内存信息）
        // Windows 用：WindowsHardwareAbstractionLayer，Linux用：LinuxHardwareAbstractionLayer
        this.hardware = new WindowsHardwareAbstractionLayer();
    }

    /**
     * 服务器监控任务，每隔5分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * ?")
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
            if (SESSIONS.isEmpty()) {
                log.debug("当前没有活跃的WebSocket连接");
                return;
            }
            log.info("开始执行定时时间推送任务，当前连接数: {}", SESSIONS.size());
            String cpuInfo = String.format("CPU使用率：%s%%", df.format(cpuUsage));
            String memoryInfo = String.format("内存使用率：%s%%", df.format(memoryUsage));
            SESSIONS.values().removeIf(session -> {
                try {
                    if (session.isOpen()) {
                        // 调用下面封装的私有方法，向指定的会话发送消息
                        sendMsg(session, cpuInfo);
                        sendMsg(session, memoryInfo);
                        // 保留会话
                        return false;
                    } else {
                        log.warn("发现已关闭的会话，将其移除: {}", session.getId());
                        // 移除会话
                        return true;
                    }
                } catch (Exception e) {
                    log.error("发送消息失败，移除会话: {}", session.getId(), e);
                    // 移除有问题的会话
                    return true;
                }
            });
            // 实际可以发送邮件、短信，或通过钉钉机器人自动告警
//            if (cpuUsage > cpuThreshold) {
//                log.warn("【告警】CPU使用率超过阈值！当前：{}%，阈值：{}%", df.format(cpuUsage), cpuThreshold);
//
//            }
//            if (memoryUsage > memoryThreshold) {
//                log.warn("【告警】内存使用率超过阈值！当前：{}%，阈值：{}%", df.format(memoryUsage), memoryThreshold);
//            }

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
