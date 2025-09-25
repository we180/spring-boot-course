package top.cs.boot.redis.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.cs.boot.redis.result.Result;
import top.cs.boot.redis.service.SmsService;

@RestController
@RequestMapping("/api")
public class SmsController {
    @Resource
    private SmsService smsService;

    @GetMapping("/sms/send")
    public Result<Boolean> sendSms(@RequestParam String phone) {
        return Result.ok(smsService.sendSms(phone));
    }
}
