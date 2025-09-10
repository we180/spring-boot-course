package top.cs.boot.config.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
public class Team {
    @Value("${team.name}")
    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 20, message = "团队名长度不能超过20")
    private String name;

    @Value("${team.leader}")
    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 8, message = "长度不能超过8")
    private String leader;

    @Value("${team.phone}")
    @Pattern(regexp = "[0-9]{11}$")
    private String phone;

    @Value("${team.age}")
    @Min(value = 1, message = "年龄不能小于1")
    @Max(value = 8, message = "年龄不能超过8")
    private String age;

    @Past(message = "创建时间必须是过去的时间")
    private LocalDate createTime;
}
