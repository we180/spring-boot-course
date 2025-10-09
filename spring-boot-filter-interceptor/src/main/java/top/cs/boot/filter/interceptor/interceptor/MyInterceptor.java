package top.cs.boot.filter.interceptor.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

@Component
@Slf4j
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("Myinterceptor 在请求接口之前执行的逻辑：{}", requestURI);
        LocalDateTime begintime = LocalDateTime.now();
        request.setAttribute("begintime", begintime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Object begintime = request.getAttribute("begintime");
        log.info("being Time:{}", begintime);
        LocalDateTime endtime = LocalDateTime.now();
        String requestURI = request.getRequestURI();
        log.info("拦截器结束:{},{}", requestURI, endtime);
        Duration duration = Duration.between((Temporal) begintime,endtime);
        long millis = duration.toMillis();
        log.info("接口耗时：{}ms", millis);
    }
}
