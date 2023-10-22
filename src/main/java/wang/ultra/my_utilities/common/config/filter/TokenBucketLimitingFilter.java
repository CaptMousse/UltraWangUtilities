package wang.ultra.my_utilities.common.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.SendMailUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter(filterName = "tokenBucketLimitingFilter", urlPatterns = {"/*"})
public class TokenBucketLimitingFilter implements Filter {

    private static long refreshTime = System.currentTimeMillis();

    // 初始容量
    private static long currentRemaining = 0L;

    private boolean tokenBucketCore() {

        long peak = ConstantFromFile.getLimitingPeak(); // 峰值
        long qps = ConstantFromFile.getLimitingQPS();   // QPS
        long currentTime = System.currentTimeMillis();

        // 原理
        // 每次调用的时候, 通过时间间隔获取总共要添加多少令牌
        // 然后在 容量 和 剩余令牌+添加多少令牌 之间取最小值 就是当前剩余令牌
        long generateToken = (currentTime - refreshTime) / 1000 * qps;
        currentRemaining = Math.min(peak, currentRemaining + generateToken);
        refreshTime = currentTime; // 记录访问时间

        if (currentRemaining > 0) {
            currentRemaining--;
            return true;
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        List<String> excludedUriList = new ArrayList<>();
        excludedUriList.add("/blog/context/downloadImage");
        excludedUriList.add("/game2048/");
        for (String excludedUri : excludedUriList) {
            if (request.getRequestURI().contains(excludedUri)) {
//                System.out.println("URI过滤排除 = " + request.getRequestURI());
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        List<String> excludedMethodList = new ArrayList<>();
        excludedMethodList.add("OPTIONS");
        for (String excludedMethod : excludedMethodList) {
            if (request.getMethod().contains(excludedMethod)) {
                System.out.println("Method过滤排除 = " + request.getMethod());
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        if (tokenBucketCore()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String returnStr = AjaxUtils.failedJsonString("当前访问过多, 请稍后再试! ");
            response.getWriter().write(returnStr);

            String mailTo = ConstantFromFile.getMailTo();
            String mailSubject = "【UltraWang监控提醒】在" + DateConverter.getSimpleTime() + "过滤器限流已启动";
            String mailContent = "<h1 style=\"text-align: center;\">歪比巴卜</h1>";
            SendMailUtils sendMailUtils = new SendMailUtils();
            sendMailUtils.sendMail(mailTo, mailSubject, mailContent);
        }
    }
}
