package wang.ultra.my_utilities.common.config.intercepor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
    // @Bean
    // public MyInterceptor myInterceptor() {
    //     return new MyInterceptor();
    // }

    @Autowired
    FileTransferInterceptor fileTransferInterceptor;

    @Autowired
    BlogCookieInterceptor blogCookieInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        // 文件传输拦截器
        List<String> fileTransferAddPathList = new ArrayList<>();
        fileTransferAddPathList.add("/fileTransfer/**");
        List<String> fileTransferExcludePathList = new ArrayList<>();
        InterceptorRegistration fileTransferInterceptorRegistration = interceptorRegistry.addInterceptor(fileTransferInterceptor);
        fileTransferInterceptorRegistration.addPathPatterns(fileTransferAddPathList);
        fileTransferInterceptorRegistration.excludePathPatterns(fileTransferExcludePathList);

        //博客Cookie拦截器
//        List<String> blogCookieAddPathList = new ArrayList<>();
//        blogCookieAddPathList.add("/blog/**");
//        List<String> blogCookieExcludePathList = new ArrayList<>();
//        InterceptorRegistration blogCookieInterceptorRegistration = interceptorRegistry.addInterceptor(blogCookieInterceptor);
//        blogCookieInterceptorRegistration.addPathPatterns(blogCookieAddPathList);
//        blogCookieInterceptorRegistration.excludePathPatterns(blogCookieExcludePathList);
    }
}
