package top.cs.springbootweek1.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Value("${my.feature.helloSwitch}")
    private boolean isHelloEnable;

    @Value("${my.feature.closeMsg}")
    private String closeMessage;

    @GetMapping("/hello")
    public String hello(){
        if(isHelloEnable){
            return "接口开放中！Man！";
        } else {
            return closeMessage;
        }
    }
}
