package top.cs.boot.redis.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.cs.boot.redis.cache.RedisCache;
import top.cs.boot.redis.cache.RedisKeys;
import top.cs.boot.redis.entity.LoginRequest;
import top.cs.boot.redis.entity.LoginResponse;
import top.cs.boot.redis.enums.ErrorCode;
import top.cs.boot.redis.exception.ServerException;
import top.cs.boot.redis.service.LoginService;
import top.cs.boot.redis.utils.CommonUtils;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final RedisCache redisCache;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String phone = loginRequest.getPhone();
        String inputCode = loginRequest.getCode();

        // 校验手机号
        if (!CommonUtils.checkPhone(phone)) {
            throw new ServerException(ErrorCode.PHONE_ERROR);
        }

        // 校验验证码是否为空
        if (inputCode == null || inputCode.trim().isEmpty()) {
            throw new ServerException("验证码不能为空");
        }

        // 从Redis中获取验证码
        String redisKey = RedisKeys.getSmsKey(phone);
        String redisCode = redisCache.get(redisKey).toString();

        // 验证码不存在或已过期
        if(redisCode ==  null){
            throw new ServerException("验证码已过期或不存在");
        }

        // 验证码不匹配
        if(!inputCode.equals(redisCode)){
            throw new ServerException("验证码错误");
        }

        // 验证成功 删除redis中的验证码
        redisCache.delete(redisKey);

        // 生成token并返回登录信息
        String token = generateToken(phone);

        log.info("用户 {} 登录成功", phone);
        return new LoginResponse("token", phone);
    }

    private String generateToken(String  phone){
        return UUID.randomUUID().toString().replace("-","") +  phone.hashCode();
    }
}
