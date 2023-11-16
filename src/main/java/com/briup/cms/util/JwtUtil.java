package com.briup.cms.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * 过期时间，单位：分钟
     * 但token字符串超时后，无法通过验证
     */
    private static final int EXPIRE_TIME_MINUTE = 60 * 24 * 5;//五天过期
    /**
     * jwt 密钥
     * 实现对数据的加密操作。通过该字符串对token字符串是否有效验证
     */
    private static final String SECRET = "briup_jwt_secret";

    /**
     * 生成JWT字符串
     *
     * @param userId 用户id
     * @param info   附带的其他信息
     *               Map的value只能存放的值的类型为：Map, List, Boolean, Integer, Long,  Double, String and Date
     *               注意，map的value如果是null的话，也会报错
     * @return jwtToken
     */
    //第一个参数用来设置Audience受众,第二个参数用来存放信息(放在声明claim里),这两个参数都放在载荷payload里
    public static String sign(Long userId, Map<String, Object> info) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, EXPIRE_TIME_MINUTE);
            Date date = new Date(calendar.getTimeInMillis());//设置过期时间(五天过期)
            Algorithm algorithm = Algorithm.HMAC256(SECRET);//把密钥加密
            return JWT.create()//token的header放加密类型JWT和加密算法Algorithm
                    // 将 userId 保存到payLoad的Audience里面,设置受众，确保它只能被特定的接收者使用(一般在JWT拦截器里用到)
                    .withAudience(String.valueOf(userId))
                    // 存放自定义数据(声明claim)
                    .withClaim("info", info)//(这三个with方法都放在了载荷payload里)
                    // token过期时间(也在载荷payLoad里)
                    .withExpiresAt(date)
                    // token 的密钥
                    .sign(algorithm);//签名 = HMACSHA256(base64UrlEncode(header)+"."+base64UrlEncode(payload), secret);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 校验token
     *
     * @param token
     * @return
     */
    public static boolean checkSign(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);//加密密钥256bit
        JWTVerifier verifier = JWT.require(algorithm).build();//根据加密过的密钥解析token
        verifier.verify(token);
        return true;
    }


    /**
     * 根据token获取受众里的userId
     *
     * @param token
     * @return
     */
    public static String getUserId(String token) {//获取token里的payload里的受众audience
        try {
            String userId = JWT.decode(token).getAudience().get(0);
            return userId;
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 根据token获取自定义数据info
     *
     * @param token
     * @return
     */
    public static Map<String, Object> getInfo(String token) {//获取token中载荷payload里的声明claim
        try {
            return JWT.decode(token).getClaim("info").asMap();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

}
