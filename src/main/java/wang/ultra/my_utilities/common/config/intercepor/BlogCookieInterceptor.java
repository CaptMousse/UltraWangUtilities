package wang.ultra.my_utilities.common.config.intercepor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;

/**
 * 博客Cookie拦截器
 */
@Component
public class BlogCookieInterceptor implements HandlerInterceptor {

    @Override
    // 原始方法调用前执行的内容
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");

        return true;
    }
}
