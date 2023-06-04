package wang.ultra.my_utilities.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@CrossOrigin
@WebFilter(filterName = "currentLimitingFilter", urlPatterns = {"/*"})
public class CurrentLimitingFilter implements Filter {

    private static long timeLimiting = 0;

    private final static long initTimeLimiting = 1000;


    public static long getTimeLimiting() {
        return timeLimiting;
    }

    public static void setTimeLimiting(long timeLimiting) {
        CurrentLimitingFilter.timeLimiting = timeLimiting;
    }

    public static long getInitTimeLimiting() {
        return initTimeLimiting;
    }

    public static void init() {
        CurrentLimitingFilter.timeLimiting = initTimeLimiting;
    }

    public static boolean getStatus() {
        if (timeLimiting > 0) {
            timeLimiting = initTimeLimiting;
            return true;
        }
        timeLimiting = initTimeLimiting;
        return false;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        boolean currentLimitingFlag = CurrentLimitingFilter.getStatus();

        if (currentLimitingFlag == false) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            Map<String, Object> returnMap = new LinkedHashMap<>();
            returnMap.put("status", 429);
            returnMap.put("error", HttpStatus.TOO_MANY_REQUESTS);
            returnMap.put("msg", "当前访问过多, 请稍后再试!");
            ObjectMapper objectMapper = new ObjectMapper();
            String returnStr = objectMapper.writeValueAsString(returnMap);

            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Type", "application/json; charset=utf-8");
            response.getWriter().write(returnStr);
        }


    }

    public void currentLimitingMonitorThreadStart() {
        CurrentLimitingMonitorRunnable runnable = new CurrentLimitingMonitorRunnable();
        Thread thread = new Thread(runnable, "CurrentLimitingMonitorFilter");
        thread.start();
    }

    @RequestMapping
    private static ResponseEntity<Map<String, String>> responseController(HttpServletRequest request) {
        Map<String, String> returnMap = new LinkedHashMap<>();
        returnMap.put("error", "当前请求太多, 请稍后再试. ");
        HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;
        return new ResponseEntity<>(returnMap, httpStatus);
    }
}
