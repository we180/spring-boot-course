package top.cs.springbootweek1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class SpringBootWeek1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWeek1Application.class, args);
    }

//    @GetMapping("/hello")
//    public String hello(){
//        return "hello world666";
//    }

    @GetMapping("/strings")
    public List<String> strings(){
        return List.of("Man,","what can i say");
    }
}
