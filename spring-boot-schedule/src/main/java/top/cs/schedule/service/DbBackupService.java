package top.cs.schedule.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.cs.schedule.config.DbConfig;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class DbBackupService {
    private final DbConfig dbConfig;


    /**
     * 每日凌晨2:00执行备份
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void backupWithHutool() {
        String host = dbConfig.getHost();
        String user = dbConfig.getUsername();
        String password = dbConfig.getPassword();
        String database = dbConfig.getDbName();
        String backupFile = dbConfig.getLocalPath() + database + "_" + System.currentTimeMillis() + ".sql";

        // 构建mysqldump命令
        CommandLine command = new CommandLine("D:\\MySQL\\MySQL_Server\\bin\\mysqldump.exe");
        command.addArgument("-h", false).addArgument(host, false);
        command.addArgument("-u", false).addArgument(user, false);
        // 注意：密码直接写在命令行不安全，生产环境建议使用其他方式
        command.addArgument("-p" + password, false);
        command.addArgument(database, false);
        command.addArgument("--result-file=" + backupFile, false);

        // 执行命令
        DefaultExecutor executor = new DefaultExecutor();
        PumpStreamHandler streamHandler = new PumpStreamHandler(System.out, System.err);
        executor.setStreamHandler(streamHandler);
        try {
            executor.execute(command);
            log.info("数据库备份成功，备份文件路径：{}", backupFile);
        } catch (IOException e) {
            log.error("数据库备份失败", e);
        }
    }
}
