package top.cs.boot.filter.interceptor.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import top.cs.boot.filter.interceptor.dto.LoginRequest;
import top.cs.boot.filter.interceptor.result.Result;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ParamValidateInterceptor implements HandlerInterceptor {
    // 校验器（单例）
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 获取请求参数（仅处理POST JSON）
        if ("POST".equalsIgnoreCase(request.getMethod()) && "application/json".equals(request.getContentType())) {
            // 读取请求体（需使用包装类缓存流，否则控制器无法再次读取）
            CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
            String body = cachedRequest.getBody();

            if (StringUtils.hasText(body)) {
                // 2. 转换为实体类（这里简化处理，实际需根据handler判断目标类型）
                LoginRequest loginRequest = objectMapper.readValue(body, LoginRequest.class);
                // 3. 执行校验
                Set<ConstraintViolation<LoginRequest>> violations = VALIDATOR.validate(loginRequest);
                if (!violations.isEmpty()) {
                    // 4. 校验失败处理
                    String errorMsg = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; "));
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(objectMapper.writeValueAsString(Result.error(400, errorMsg)));
                    return false;
                }
            }
        }
        return true;
    }

    // 缓存请求体的包装类
    @Getter
    static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
        private final String body;

        public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            body = new String(request.getInputStream().readAllBytes());
        }
    }
}
