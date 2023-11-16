package com.briup.cms.web.Interceptor;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.util.JwtUtil;
import com.briup.cms.util.ResultCode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 当请求方式预检请求时，不需要进行token验证
        String method = request.getMethod();
        if (method.equals("OPTIONS")) {
            return true;//通过拦截
        }

        // 1.获取请求头信息token
        String token = request.getHeader("token");//请求头里一定要有token这个key，在knife4j中全局配置里配置

        // 2.对token字符串进行验证
        if (token == null) {  //token不存在，抛出用户未登录异常
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        // 当提交了token
        try {
            JwtUtil.checkSign(token);//解析token,如果在解析过程中抛出异常，说明token验证不通过
            String userId = JwtUtil.getUserId(token);
            //专门为 /auth/user/info 提供服务
            request.setAttribute("userId",userId);
        } catch (Exception ex) {
            // 扩展：可以根据不同的异常类型 提供用户不同的错误内容
            if (ex instanceof TokenExpiredException) {
                throw new ServiceException(ResultCode.TOKEN_TIMEOUT);
            }
            // 添加多个类型的异常判断
            throw new ServiceException(ResultCode.TOKEN_VALIDATE_ERROR);
        }
        //拦截器放行到controller中
        return true;
    }
}
