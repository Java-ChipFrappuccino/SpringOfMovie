package kr.co.moviespring.web.config;

import kr.co.moviespring.web.interceptor.VisitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 인터셉터를 등록하여 어떤 경로에서 적용할지를 지정해주기 위해서 필요함.
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private VisitInterceptor visitInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){

        registry.addInterceptor(visitInterceptor).addPathPatterns("/");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/upload/**")
                .addResourceLocations("file:src/main/resources/static/image/upload/");
    }
}
