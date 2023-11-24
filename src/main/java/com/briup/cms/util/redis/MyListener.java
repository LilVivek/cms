package com.briup.cms.util.redis;

import com.briup.cms.bean.Article;
import com.briup.cms.mapper.ArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/*
 * 1必须实现接口中抽象方法
 * 2当**条件 自动调用该接口的抽象方法
 * 事件监听器
 * 程序运行过程中，发生事件（项目启动 关闭 异常 对象创建 销毁） 产生事件对象
 * 对应的事件监听器中方法被自动调用
 * */
@Slf4j
@Component
public class MyListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final String REDIS_KEY = "Article_Read_Num";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("事件对象:" + event);
        //当项目启动，将磁盘的高频热点数据 加载到redis数据库中，实现读写操作
//        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
//        RedisConnection connection = connectionFactory.getConnection();
//        connection.flushDb();//启动项目时先清空redis数据库(也可以说刷新缓存)
        List<Article> articles = articleMapper.selectList(null);
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();

        for (Article article : articles) {
            Object value = ops.get(REDIS_KEY, article.getId().toString());
            if (value == null) {//如果为空，说明redis中还没有该文章阅读数据，那么就写入
                ops.put(REDIS_KEY, article.getId().toString(), article.getReadNum());//初始化所有文章的阅读数到redis
            }
        }
        log.info("所有文章阅读量初始化到redis完毕");
        //论坛历史访问人数 今日访问人数 注册会员 首页显示
    }
}
