package top.cs.springbootweek1.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.cs.springbootweek1.config.CloopenConfig;
import top.cs.springbootweek1.service.SmsService;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    @Resource
    private CloopenConfig cloopenConfig;

    @Override
    public void sendSms(String phone) {
        // 生成验证码
        int code = ThreadLocalRandom.current().nextInt(1000,9999);
        log.info("发送短信验证码:{}",code);

        // 获取到配置信息
        String serverIp = cloopenConfig.getServerIp();
        String port = cloopenConfig.getPort();
        String accountsId = cloopenConfig.getAccountsId();
        String accountToken = cloopenConfig.getAccountToken();
        String appId = cloopenConfig.getAppId();
        String templateId = cloopenConfig.getTemplateId();

        // 创建sdk对象
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, port);
        sdk.setAccount(accountsId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        String[] datas = {String.valueOf(code),"1"};

        // 发送短信
        HashMap<String, Object> result = sdk.sendTemplateSMS(phone, templateId, datas,"1234", UUID.randomUUID().toString());
        if ("000000".equals(result.get("statusCode"))){
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);

                log.info("{}:{}",key,object);
            }
        }else {
            log.error("错误码={} 错误信息={}",result.get("statusCode"),result.get("statusMsg"));
        }
    }
}
