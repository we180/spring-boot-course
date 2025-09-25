package top.cs.boot.redis.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.cs.boot.redis.cache.RedisCache;
import top.cs.boot.redis.cache.RedisKeys;
import top.cs.boot.redis.config.CloopenConfig;
import top.cs.boot.redis.enums.ErrorCode;
import top.cs.boot.redis.exception.ServerException;
import top.cs.boot.redis.service.SmsService;
import top.cs.boot.redis.utils.CommonUtils;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class SmsServiceImpl implements SmsService {
    private final CloopenConfig configmss;
    private final RedisCache redisCache;

    @Override
    public boolean sendSms(String phone) {
        // 校验手机号码
        if (!CommonUtils.checkPhone(phone)) {
            throw new ServerException(ErrorCode.PHONE_ERROR);
        }

        // 生成验证码，存入Redis中, 有效期1分钟
        int code = CommonUtils.generateCode();
        redisCache.set(RedisKeys.getSmsKey(phone), code, 60);
        log.info("发送短信验证码:{}", code);
        boolean flag=true;
//        flag = send(phone, code);
        return flag;
    }

    public boolean send(String phone, int code){
        // 获取配置信息
        String serverIp = configmss.getServerIp();
        String serverport = configmss.getPort();
        String accountsId = configmss.getAccountsId();
        String accountToken = configmss.getAccountToken();
        String appId = configmss.getAppId();
        String templateId = configmss.getTemplateId();

        // 创建SDK对象
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverport);
        sdk.setAccount(accountsId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        String[] datas = {String.valueOf(code), "1"};

        // 发送短信
        HashMap<String, Object> reslt = sdk.sendTemplateSMS(phone, templateId, datas, "1234", UUID.randomUUID().toString());
        if ("000000".equals(reslt.get("statusCode"))) {
            HashMap<String, Object> data = (HashMap<String, Object>) reslt.get("data");
            Set<String> keySet = reslt.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                log.info("{}={}", key, object);
            }
        } else {
            log.error("错误码={} 错误信息={}", reslt.get("statusCode"), reslt.get("statusMsg"));
            return false;
        }
        return true;
    }
}
