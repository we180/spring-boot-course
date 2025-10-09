package top.cs.boot.filter.interceptor.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 允许的源，如果指定为*则放行所有（生产环境需指定具体域名）
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        // 允许的请求方法
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        // 允许的请求头，防止什么都往请求头里面加
        httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        // 允许携带的凭证（如Cookie）
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        // 预检请求缓存时间（秒）
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        // 处理预检请求（OPTIONS方法）
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(request, response);
    }
}
