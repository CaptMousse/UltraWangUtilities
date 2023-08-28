package wang.ultra.my_utilities.common.config.intercepor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wang.ultra.my_utilities.common.cache.username.UserLoginCacheMap;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

import java.io.IOException;

/**
 * 博客Cookie拦截器
 */
@Component
public class BlogLoginInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(BlogLoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String loginToken = request.getHeader("LoginToken");

        if (loginToken == null) {
            String returnStr = AjaxUtils.failedJsonString("异常请求, 请重新登录");
            response.getWriter().write(returnStr);
            return false;
        }

        UserLoginCacheMap userLoginCacheMap = new UserLoginCacheMap();
        boolean ifLogin = userLoginCacheMap.hasUsername(loginToken);
        if (ifLogin) {
            LOG.info("已登录的路径请求: " + request.getServletPath());

            userLoginCacheMap.userLoginUpdate(loginToken);
            userLoginCacheMap.getLoginUserList();

            return true;
        } else {
            LOG.info("未登录的路径请求: " + request.getServletPath());
            String returnStr = AjaxUtils.failedJsonString("未登录");
            response.getWriter().write(returnStr);
            return false;
        }
    }
}
