package wang.ultra.my_utilities.common.config.intercepor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer{
    // @Bean
    // public MyInterceptor myInterceptor() {
    //     return new MyInterceptor();
    // }

    @Autowired
    MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        List<String> addPathList = new ArrayList<>();
        addPathList.add("/fileTransfer/**");
        List<String> excludePathList = new ArrayList<>();
        
        // interceptorRegistry.addInterceptor(myInterceptor()).addPathPatterns(addPathList).excludePathPatterns();
        InterceptorRegistration interceptorRegistration = interceptorRegistry.addInterceptor(myInterceptor);
        interceptorRegistration.addPathPatterns(addPathList);
        interceptorRegistration.excludePathPatterns(excludePathList);
    }
}
