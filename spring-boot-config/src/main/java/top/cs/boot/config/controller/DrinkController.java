package top.cs.boot.config.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.cs.boot.config.enums.DrinkType;

@RestController
@RequestMapping("/drink")
public class DrinkController {
    @GetMapping("/{type}")
    public String getDrink(@PathVariable DrinkType type) {
        return "当前饮料是" + type.getType() + "，价格是" + type.getPrice();
    }

}
