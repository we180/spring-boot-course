package top.cs.boot.filter.interceptor.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class TimeStatInterceptor implements HandlerInterceptor {

    // 存放请求开始时间的属性名
    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);
        // 放行请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 控制器执行后调用（此时还未返回响应）
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 响应返回前调用：计算耗时
        long startTime = (long) request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        long cost = endTime - startTime;
        // 打印日志（handler包含控制器方法信息）
        log.info("接口耗时 - 方法: {}, 耗时: {}ms", handler.getClass().getSimpleName(), cost);
    }
}
