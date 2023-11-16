package com.briup.cms.config;

import com.briup.cms.web.Interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/auth/**")//拦截路径（只拦截这些路径)(类似黑名单)
                .excludePathPatterns("/auth/category/queryAllParent")//黑名单中的白名单(类似差集)//查询所有一级栏目及其二级栏目的接口(供前台使用)
                .excludePathPatterns("/auth/comment/queryByArticleId/{id}");//黑名单中的白名单(类似差集)//查询指定文章的评论
    }

    /*跨域映射*/
    @Override
    public void addCorsMappings(CorsRegistry registry) {


        //根据HTTP协议规定的预检请求的返回头信息进行设置
        registry.addMapping("/**")// 映射所有路径
                .allowedOriginPatterns("*")// 运行所有客户端访问
                //不允许携带cookie
                .allowCredentials(false)
                //支持的方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                //运行所有请求头字段
                .allowedHeaders("*")
                //允许客户端缓存“预检请求”中获取的信息，3600秒
                .maxAge(3600);
    }
}
