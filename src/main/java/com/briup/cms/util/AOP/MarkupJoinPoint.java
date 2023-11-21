package com.briup.cms.util.AOP;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//定义两个元注解
@Target({ElementType.METHOD})//只能放在方法上
@Retention(RetentionPolicy.RUNTIME)//生效范围为运行时
public @interface MarkupJoinPoint {
}