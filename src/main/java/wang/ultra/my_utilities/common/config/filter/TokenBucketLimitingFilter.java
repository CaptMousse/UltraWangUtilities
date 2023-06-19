package wang.ultra.my_utilities.common.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.AjaxUtils;

import java.io.IOException;

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
        // 然后比对 容量 和 剩余令牌+添加多少令牌 取最小值就是当前剩余令牌
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

        boolean tokenBucketResult = tokenBucketCore();

        if (tokenBucketResult) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String returnStr = AjaxUtils.failedJsonString("当前访问过多, 请稍后再试! ");

            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Type", "application/json; charset=utf-8");
            response.getWriter().write(returnStr);
        }
    }
}
