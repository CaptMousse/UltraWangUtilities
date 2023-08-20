package wang.ultra.my_utilities.common.config.intercepor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
import wang.ultra.my_utilities.common.utils.StringUtils;

import java.io.IOException;

/**
 * 博客Cookie拦截器
 */
@Component
public class BlogLoginInterceptor implements HandlerInterceptor {

    @Override
    // 原始方法调用前执行的内容
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {


        HttpSession session = request.getSession();
        String username = (String) request.getSession().getAttribute("username");
        System.out.println("username = " + username);
        if (session == null || StringUtils.isEmpty(session.getAttribute("username"))) {
            System.out.println("未登录的路径请求: " + request.getServletPath());
            String returnStr = AjaxUtils.failedJsonString("未登录");
            response.getWriter().write(returnStr);
            return false;
        }

        return true;
    }
}
