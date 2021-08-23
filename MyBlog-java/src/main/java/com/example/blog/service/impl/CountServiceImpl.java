package com.example.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.entity.Count;
import com.example.blog.mapper.CountMapper;
import com.example.blog.service.CountService;
import com.example.blog.utils.RedisUtil;
import com.example.blog.vo.Const;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2021-08-22
 */
@Service
public class CountServiceImpl extends ServiceImpl<CountMapper, Count> implements CountService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    CountMapper countMapper;
    /**
     * 获取网站各种信息和
     * @return
     */
    @Override
    public Result getCount() {
        int blogCount = (int) redisUtil.get(Const.BLOGCOUNT);
        int messageCount = (int)redisUtil.get(Const.MESSAGECOUNT);
        int views = (int) redisUtil.sGetSetSize(Const.VIEWS);
        Count count = new Count(blogCount,views,messageCount);
        return Result.succ(count);
    }
}
