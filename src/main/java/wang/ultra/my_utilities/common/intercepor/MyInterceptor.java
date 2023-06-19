package wang.ultra.my_utilities.common.intercepor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    // 原始方法调用前执行的内容
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("preHandle...");
        return true;
    }

    @Override
    // 原始方法调用后执行的内容
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
                
        String requestServerName = request.getServerName();
        String myServerName = ConstantFromFile.getHostname() + "." + ConstantFromFile.getDomain();
        if (!requestServerName.equals(myServerName)) {
            return;
        }
    }

    @Override
    // 原始方法调用完成后执行的内容
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("afterCompletion...");
    }
}
