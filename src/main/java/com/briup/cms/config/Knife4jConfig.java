package com.briup.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {
    private ApiInfo apiInfo() {
        //配置基本信息
        Contact cms = new Contact("cms", "http://www.briup.com/", "1097520696@qq.com");
        return new ApiInfoBuilder()
                //标题
                .title("看点咨询管理系统")
                .description("欢迎访问看点咨询管理系统接口文档")
                //基本信息
                .contact(cms)
                //版本号
                .version("1.0.0")
                //创建
                .build();
    }

    //配置Controller的包路径
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //传入要扫描的包结构
                .apis(RequestHandlerSelectors.basePackage("com.briup.cms.web.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}