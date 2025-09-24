package top.cs.boot.redis.service;

import org.springframework.stereotype.Service;

@Service
public interface SmsService {
    boolean sendSms(String phone);
}