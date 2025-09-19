package top.cs.boot.exception.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import top.cs.boot.exception.annotation.Phone;

@Data
public class User {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Max(value = 100, message = "年龄不能大于100岁")
    @Min(value = 1, message = "年龄不能小于1岁")
    private int age;

    @NotBlank(message = "手机号不能为空")
    @Phone
    private String phone;
}
