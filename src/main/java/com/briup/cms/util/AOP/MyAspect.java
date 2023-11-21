package com.briup.cms.util.AOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 第二个切面类
 * 1配置注解
 * 2选用通知
 * 3配置切入点(一组连接点的集合)
 * 3.1execution表达式,表达式的写法和表达式的封装
 * 表达式的写法：让范围在切入点中尽可能的小
 * <p>
 * 3.2自定义标识注解，
 * 注解的编写，
 * 注解的使用
 */
@Component
@Aspect
@Slf4j
public class MyAspect {
    /*假设这五个通知用的是同一个切入点*/  /*切点+通知=切面 放切面的是切面类*/
    /*可以封装为方法，方便其他调用*/     /*一般切到service层 这里只有前两个切面用到了pt()*/
    @Pointcut("execution(* com.briup.cms.service.*.*(..))")//最右边的那个*(..)是任意方法(任意参数)
    public void pt() {
    }

    @Before("pt()")
    public void before() {
        System.out.println("前置通知启动");
    }

    @After("pt()")
    public void after() {
        System.out.println("后置通知启动");
    }

    @AfterReturning("execution(* com.briup.cms.service.*.get*(..))")//只在get方法上生效
    public void afterReturning(JoinPoint joinPoint) {
        //可以通过joinPoint获得一些信息
        Object aThis = joinPoint.getThis();
        System.out.println(aThis.getClass());
    }

    @AfterThrowing("execution(* com.briup.cms.service.*.get*(..))")//只在get方法上生效
    public void afterThrow() {
        System.out.println("出现异常后的操作");
    }

    @Around("@annotation(com.briup.cms.util.AOP.MarkupJoinPoint)")//注解方式配置切入点
    /*环绕通知的参数类型只能是ProceedingJoinPoint*/
    public Object around(ProceedingJoinPoint joinPoint){//连接点joinPoint就是原始方法
        //调用通知中间的方法
        System.out.println("环绕前的切面");
        //需要将返回值传递出去，否则就拿不到返回值了
        Object result = null;
        try {
            result = joinPoint.proceed();//原始方法不管有没有抛出异常，joinPoint的proceed()方法一定会抛出异常,需要进行处理
        } catch (Throwable throwable) {
            throwable.printStackTrace();//打印异常栈追踪
        }

        System.out.println("环绕后的切面");
        return result;
    }
}
