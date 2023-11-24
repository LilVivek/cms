package com.briup.cms.config;

import com.briup.cms.bean.Article;
import com.briup.cms.service.IArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

@Slf4j
@Component
@EnableScheduling//(需要写这个注解才能开启定时任务
public class ReadNumTask {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    IArticleService iArticleService;
    //redis当中阅读量的key
    private final String REDIS_KEY = "Article_Read_Num";

    // 30秒运行一次
    @Scheduled(cron = "0 0/5 * * * ?")
    public void saveReadNum() {
        //获取
        try {
            HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
            Set<String> keys = ops.keys(REDIS_KEY);//获取所有文章id集合
            ArrayList<Article> list = new ArrayList<>(100);//初始化大点，防止频繁扩容浪费性能
            for (String key : keys) {
                Long articleId = Long.valueOf(key);
                Integer readNum = (Integer) ops.get(REDIS_KEY, articleId.toString());

                Article article = new Article();
                article.setId(articleId);
                article.setReadNum(readNum);

                list.add(article);
            }
            iArticleService.updateBatchById(list);//批量更新
            log.info("阅读量更新入库完毕");
        } catch (Exception e) {
            log.info("阅读量入库失败，原因为：" + e.getMessage());
        }

    }
}
