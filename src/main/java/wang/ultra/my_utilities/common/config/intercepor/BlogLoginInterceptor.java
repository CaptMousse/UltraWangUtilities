package wang.ultra.my_utilities.common.config.intercepor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wang.ultra.my_utilities.common.utils.StringUtils;

/**
 * 博客Cookie拦截器
 */
@Component
public class BlogLoginInterceptor implements HandlerInterceptor {

    @Override
    // 原始方法调用前执行的内容
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {


        HttpSession session = request.getSession();
        if (session == null || StringUtils.isEmpty(session.getAttribute("username"))) {
            System.out.println("当前请求状态: 未登录");
            System.out.println("当前请求路径: " + request.getServletPath());
            return false;
        }

        return true;
    }
}
