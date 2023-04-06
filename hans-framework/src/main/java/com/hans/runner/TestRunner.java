package com.hans.runner;

import cn.hutool.json.JSONUtil;
import com.hans.Constants.RedisConstants;
import com.hans.entity.Article;
import com.hans.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 程序启动时，执行相应的操作
 */
@Component
public class TestRunner implements CommandLineRunner {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ArticleService articleService;

    @Override
    public void run(String... args) throws Exception {
        List<Article> list = articleService.list();

//        for (Article article:list
//             ) {
//            String str = JSONUtil.toJsonStr(article);
//            stringRedisTemplate.opsForValue().set(RedisConstants.ARTICLE_RUNNER+article.getId(),str);
//        }
        stringRedisTemplate.opsForValue().set(RedisConstants.ARTICLE_RUNNER,JSONUtil.toJsonStr(list));

    }
}
