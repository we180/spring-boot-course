package top.cs.boot.filter.interceptor.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RateLimitFilter implements Filter {
    private static final int Limit_count = 3;
    private static final int Limit_seconds = 60;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        String userId = httpServletRequest.getHeader("userId");
        if(userId == null || userId.isEmpty()){
            httpServletResponse.getWriter().write("{\"code\":400,\"msg\":\"userId不能为空\"}");
            return;
        }
        String limitKey = "rateLimit:" + userId + ":" + httpServletRequest.getRequestURI();

        String countStr = stringRedisTemplate.opsForValue().get(limitKey);
        int count = countStr == null ? 0 : Integer.parseInt(countStr);

        if(count >= Limit_count){
            httpServletResponse.setStatus(429);
            httpServletResponse.getWriter().write("{\"code\":429,\"msg\":\"请求过于频繁，请1分钟后再试\"}");
            return;
        }

        if(count == 0){
            stringRedisTemplate.opsForValue().set(limitKey, "1", Limit_seconds, TimeUnit.SECONDS);
        } else {
            stringRedisTemplate.opsForValue().increment(limitKey);
        }

        chain.doFilter(request, response);
    }
}
