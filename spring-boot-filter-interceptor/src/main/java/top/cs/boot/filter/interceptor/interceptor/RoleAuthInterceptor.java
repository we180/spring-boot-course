package top.cs.boot.filter.interceptor.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.cs.boot.filter.interceptor.annotation.RequireRole;
import top.cs.boot.filter.interceptor.utils.JwtUtil;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class RoleAuthInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 仅拦截控制器方法,，其他请求放行
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        // 获取方法上的 @RequireRole 注解
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        // 没有 @RequireRole 注解，表示无需校验权限，直接放行
        if (requireRole == null) {
            return true;
        }
        // 从请求头 中获取 Token，验证是否有效
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            log.warn("Token为空");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"请先登录\"}");
            return false;
        }
        if (!jwtUtil.validateToken(token)) {
            log.warn("Token无效");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"Token无效\"}");
            return false;
        }
        // token 有效，获取用户角色
        String userRole = jwtUtil.getUserRoleFromToken(token);
        // 获取接口方法上的角色数组
        String[] roles = requireRole.value();
        // 将角色数组转换为列表
        List<String> roleList = Arrays.asList(roles);
        // 判断用户角色是否包含在所需角色中
        if (roleList.contains(userRole)) {
            log.info("权限验证成功");
            return true;
        } else {
            log.warn("权限不足 - 所需角色: {}, 用户角色: {}", requireRole.value(), userRole);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"权限不足,无法访问\"}");
            return false;
        }
    }
}
