package com.hans.timer.job;

import cn.hutool.json.JSONUtil;
import com.hans.Constants.RedisConstants;
import com.hans.entity.Article;
import com.hans.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TimeJob {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ArticleService articleService;


    @Scheduled(cron = "0 0/10 * * * ?")
    public void TestJob(){
        //要执行的代码
//        Set<String> set = stringRedisTemplate.keys(RedisConstants.ARTICLE_RUNNER+"*");
//        List<String> list = set.stream()
//                .collect(Collectors.toList());
//
//        List<Article> articles = new ArrayList<>();
//        for (String articleId : list) {
//            String jsonStr = stringRedisTemplate.opsForValue().get(articleId);
//            Article article = JSONUtil.toBean(jsonStr, Article.class);
//            articles.add(article);
//        }

        //redis存入数据库
        String articleListJson = stringRedisTemplate.opsForValue().get(RedisConstants.ARTICLE_RUNNER);
        List<Article> articles = JSONUtil.toList(articleListJson, Article.class);

        articleService.saveOrUpdateBatch(articles);
    }
}
