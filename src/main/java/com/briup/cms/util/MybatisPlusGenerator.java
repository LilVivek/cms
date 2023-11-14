package com.briup.cms.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.query.SQLQuery;

import java.util.Collections;

public class MybatisPlusGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/cms?characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useServerPrepStmts=true";

        FastAutoGenerator.create(url, "root", "123456")
                .globalConfig(builder -> {
                    builder.author("Vivek") // 设置作者
                            .disableOpenDir() //禁止打开输出目录
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\Users\\Vivek\\Desktop\\cms\\src\\main\\java"); // 指定输出目录
                })
                .dataSourceConfig(builder -> {
                    builder.databaseQueryClass(SQLQuery.class)
                            .typeConvert(new MySqlTypeConvert())
                            .dbQuery(new MySqlQuery());
                })
                .packageConfig(builder -> {
                    builder.parent("com.briup.cms") // 设置父包名
                            .entity("bean")
                            .controller("web.controller")
//                            .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\Users\\Vivek\\Desktop\\cms\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder()//实体类策略
                            .enableFileOverride()
                            .enableChainModel()
                            .enableLombok()
                            .logicDeleteColumnName("deleted")
                            .build();
                    builder.controllerBuilder()//controller策略
                            .enableRestStyle()
                            .enableFileOverride();
                    builder.serviceBuilder()//service策略
                            .enableFileOverride();
                    builder.mapperBuilder()//mapper策略
                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .enableFileOverride();
                    builder.addInclude("cms_article", "cms_category", "cms_comment", "cms_log", "cms_role", "cms_slideshow", "cms_subcomment", "cms_user") // 设置需要生成的表名
                            .addTablePrefix("cms_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}