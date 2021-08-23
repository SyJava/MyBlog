package com.example.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blog.entity.Blog;
import com.example.blog.entity.Count;
import com.example.blog.entity.User;
import com.example.blog.mapper.CountMapper;
import com.example.blog.mapper.UserMapper;
import com.example.blog.utils.RedisUtil;
import com.example.blog.vo.Const;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class BlogApplicationTests {
@Autowired
UserMapper userMapper;
/**
因为登录时，springsecurity需要获取BCryptPasswordEncoder()加密后的密码
所以可以在数据库中添加明文密码，然后运行此单元测试，将明文密码转为加密后的密码
 **/
    @Test
    void contextLoads() {
        QueryWrapper qw=new QueryWrapper();
        qw.eq("username","sy");
        User user = userMapper.selectOne(qw);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 加密
        String encodedPassword = passwordEncoder.encode(user.getPassword().trim());
        user.setPassword(encodedPassword);
        System.out.println(user.getPassword());
        userMapper.updateById(user);
    }

    /**
     * 测试是否连上了elasticsearch
     */
    @Autowired
    ElasticsearchRestTemplate restTemplate;
    @Test
    void delete(){
     Blog blog = new Blog();
     blog.setBlogId(57);
     String delete = restTemplate.delete(blog);
     System.out.println(delete);
}

    /**
     * 把myqsl中的网站信息存到redis中，避免redis中没有数据从而出现异常
     */
    @Autowired
    CountMapper countMapper;
    @Autowired
    RedisUtil redisUtil;
    @Test
    void MySqlToRedis(){
        Count count = countMapper.selectById(1);
        redisUtil.set(Const.BLOGCOUNT,count.getBlogCount());
        redisUtil.set(Const.MESSAGECOUNT,count.getMessageCount());
        redisUtil.sSet(Const.VIEWS,count.getViews());
    }


}
