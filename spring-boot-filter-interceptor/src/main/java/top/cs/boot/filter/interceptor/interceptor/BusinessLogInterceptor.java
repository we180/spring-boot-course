package top.cs.boot.filter.interceptor.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@ControllerAdvice
public class BusinessLogInterceptor implements HandlerInterceptor, ResponseBodyAdvice<Object> {

    // 记录控制器方法名
    private String methodName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取控制器方法名
        if (handler instanceof HandlerMethod handlerMethod) {
            methodName = handlerMethod.getBeanType().getName() + "." + handlerMethod.getMethod().getName();
            log.info("业务日志 - 执行方法: {}", methodName);
        }
        return true;
    }

    // 记录响应结果
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 对所有响应生效
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.info("业务日志 - 方法: {}, 返回结果: {}", methodName, new ObjectMapper().writeValueAsString(body));
        return body;
    }
}
