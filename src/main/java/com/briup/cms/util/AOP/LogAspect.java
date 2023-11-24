package com.briup.cms.util.AOP;

import com.briup.cms.bean.Ip;
import com.briup.cms.bean.Log;
import com.briup.cms.mapper.LogMapper;
import com.briup.cms.util.IPUtils;
import com.briup.cms.util.JwtUtil;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description 日志切面类
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    @Autowired
    LogMapper logMapper;
    @Autowired
    Gson gson;

    //定义切入点: 当执行Controller包下的方法 并且方法上添加了日志注解 需要使用切面增强
    @Pointcut("execution(* com.briup.cms.web.controller.*.*(..))" +
            "&& @annotation(com.briup.cms.util.AOP.MyLog)")//最右边的那个*(..)是任意方法(任意参数)
    public void pt() {
    }

    @SneakyThrows//Lombok自带的异常处理
    @Around("pt()")//注解方式配置切入点
    /*环绕通知的参数类型只能是ProceedingJoinPoint*/
    public Object around(ProceedingJoinPoint joinPoint) {//连接点joinPoint就是原始方法
        long startTime = System.currentTimeMillis();
        Log myLog = new Log();
        //获取方法签名,即接口的用途描述
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //接口功能描述（自定义注解里的value)
        String businessName = method.getAnnotation(MyLog.class).value();
        myLog.setBusinessName(businessName);
        log.info("接口功能:{}", myLog.getBusinessName());

        String args = gson.toJson(joinPoint.getArgs());
        myLog.setParamsJson(args);
        log.info("请求参数为:{}", myLog.getParamsJson());

        //根据请求上下文 获取请求属性
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("token不存在");
        }
        Map<String, Object> info = JwtUtil.getInfo(token);
        String username = String.valueOf(info.get("username"));
        myLog.setUsername(username);
        log.info("当前请求的用户为:{}", myLog.getUsername());

        String requestMethod = request.getMethod();
        myLog.setRequestMethod(requestMethod);
        log.info("请求方式为:{}", myLog.getRequestMethod());

        StringBuffer requestURL = request.getRequestURL();
        myLog.setRequestUrl(String.valueOf(requestURL));
        log.info("请求URL为:{}", myLog.getRequestUrl());

        //获取ip详情
        Ip ip = IPUtils.getIP(request);
        myLog.setIp(ip.getIp());
        log.info("发送请求的ip为:{}", myLog.getIp());
        myLog.setSource(ip.getAddr());
        log.info("发送请求的ip来源地址为:{}", myLog.getSource());


        //执行请求的接口,获取到原始方法的执行结果(统一响应结果)
        /**
         * 原始方法 上面的是执行前的增强功能 下面是执行后的增强功能
         */
        Object result = joinPoint.proceed();//最原始的方法


        String resultJson = gson.toJson(result);
        myLog.setResultJson(resultJson);
        log.info("响应结果为:{}", myLog.getResultJson());

        long endTime = System.currentTimeMillis();
        long spendTime = endTime - startTime;
        myLog.setSpendTime(spendTime);
        log.info("请求耗时为:{}", myLog.getSpendTime());

//        myLog.setCreateTime(LocalDateTime.now());//可以不写 数据库自动填入了时间

        logMapper.insert(myLog);

        return result;//返回原始方法执行的结果
    }
}