package top.cs.boot.filter.interceptor.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.cs.boot.filter.interceptor.dto.LoginRequest;
import top.cs.boot.filter.interceptor.entity.User;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    // 定义一个集合，用来存放用户数据,SET 可以去重
    private static final Set<User> MOCK_USERS = new HashSet<>();

    /*
     * 静态代码块，初始化用户数据，它会比构造方法先执行
     */
    static {
        MOCK_USERS.add(new User(1L, "admin", "123456", "admin@example.com", "13800000001", "ADMIN"));
        MOCK_USERS.add(new User(2L, "user", "123456", "user@example.com", "13800000002", "USER"));
    }

    /**
     * 用户登录
     */
    public User authenticate(LoginRequest loginRequest) {
        log.info("用户登录验证：{},{}", loginRequest.getUsername(), loginRequest.getPassword());
        // 使用 Java 8 的Stream API ,在集合 MOCK_USERS 中查找匹配的用户
        // 通过过滤条件用户名和密码都相等来搜索，找到第一个匹配项就返回该用户对象，如果没找到则返回 null，这是典型的用户登录验证逻辑。
        return MOCK_USERS.stream().filter(u -> u.getUsername().equals(loginRequest.getUsername()) && u.getPassword().equals(loginRequest.getPassword())).findFirst().orElse(null);
    }

    /**
     * 根据用户名获取用户信息
     */
    public User getUserByUsername(String username) {
        return MOCK_USERS.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

}
