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
    BlogLoginInterceptor blogLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        // 文件传输拦截器
        List<String> fileTransferAddPathList = new ArrayList<>();
        fileTransferAddPathList.add("/fileTransfer/**");
        List<String> fileTransferExcludePathList = new ArrayList<>();
        InterceptorRegistration fileTransferInterceptorRegistration = interceptorRegistry.addInterceptor(fileTransferInterceptor);
        fileTransferInterceptorRegistration.addPathPatterns(fileTransferAddPathList);
        fileTransferInterceptorRegistration.excludePathPatterns(fileTransferExcludePathList);

        // 博客登录拦截器
        List<String> blogLoginAddPathList = new ArrayList<>();
        blogLoginAddPathList.add("/blog/context/upload/**");
        blogLoginAddPathList.add("/blog/user/ifLogin");
        blogLoginAddPathList.add("/blog/user/logout");
        List<String> blogLoginExcludePathList = new ArrayList<>();
//        blogLoginExcludePathList.add("/blog/login");
//        blogLoginExcludePathList.add("/blog/logout");
        InterceptorRegistration blogCookieInterceptorRegistration = interceptorRegistry.addInterceptor(blogLoginInterceptor);
        blogCookieInterceptorRegistration.addPathPatterns(blogLoginAddPathList);
        blogCookieInterceptorRegistration.excludePathPatterns(blogLoginExcludePathList);
    }
}
