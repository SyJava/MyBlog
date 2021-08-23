package com.example.blog.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blog.entity.Count;
import com.example.blog.mapper.CountMapper;
import com.example.blog.vo.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 公众号：饮亿杯Java
 *
 * @author SY
 * @date 2021/8/22 13:14
 */
@Component
@Configurable
@EnableScheduling
public class QuartzUtil {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    CountMapper countMapper;
    QueryWrapper qw =new QueryWrapper<Count>();
    Count count=new Count(1);//避免频繁创建实例
    /**
     * redis数据定时存入MySql
     * 执行此任务，需要保证redis和mysql中有数据
     */
    @Scheduled(cron = "0 0 * * * *")//每小时写入一次
    public void Endurance(){//网站访问量，博客、留言数定时存入MySql
        int blogCount = (int) redisUtil.get(Const.BLOGCOUNT);
        int messageCount = (int)redisUtil.get(Const.MESSAGECOUNT);
        int views = (int) redisUtil.sGetSetSize(Const.VIEWS);
        count.setBlogCount(blogCount);
        count.setMessageCount(messageCount);
        count.setViews(views);
        countMapper.updateById(count);
    }
}
