package top.cs.springbootweek1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cs.sms.ccp")
public class CloopenConfig {
    private String serverIp;
    private String port;
    private String accountsId;
    private String accountToken;
    private String appId;
    private String templateId;
}
