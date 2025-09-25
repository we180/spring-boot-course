package top.cs.boot.redis.entity;

import lombok.Data;

@Data
public class LoginRequest {
    private String phone;
    private String code;
}
