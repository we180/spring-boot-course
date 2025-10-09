package top.cs.boot.filter.interceptor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.cs.boot.filter.interceptor.result.Result;

@Slf4j
@RestController
public class TestController {
    @GetMapping("/test")
    public String get() {
        log.info("进入 Controller");
        return "Manba out";
    }

    @GetMapping("/test/filter")
    public Result<String> testFilter(@RequestParam String name) {
        return Result.ok("Hello, " + name);
    }

    @GetMapping("/pay/{id}")
    public Result<String> pay(@PathVariable long id) {
        log.info("开始支付");
        return Result.ok("支付成功，订单号：" + id);
    }
}
