package wang.ultra.my_utilities.common.config.intercepor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    // 原始方法调用前执行的内容
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestServerName = request.getServerName();
        String myServerName = ConstantFromFile.getHostname() + "." + ConstantFromFile.getDomain();

        // 判断是否使用IPv6网络
//        if (!requestServerName.equals(myServerName)) {
//            String returnStr = AjaxUtils.failedJsonString("当前访问仅限IPv6地址! ");
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Content-Type", "application/json; charset=utf-8");
//            response.getWriter().write(returnStr);
//            response.setContentType("text/html; charset=utf-8");
//            response.getWriter().write(AjaxUtils.failedJsonString("当前访问仅限IPv6地址! "));
//            return false; // 测试环境注掉
//        }
        return true;
    }
}
