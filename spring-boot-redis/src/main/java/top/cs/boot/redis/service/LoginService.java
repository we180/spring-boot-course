package top.cs.boot.redis.service;

import top.cs.boot.redis.entity.LoginRequest;
import top.cs.boot.redis.entity.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest  loginRequest);
}
