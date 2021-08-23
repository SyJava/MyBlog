package com.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.entity.User;
import com.example.blog.mapper.UserMapper;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2021-07-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
@Autowired
UserMapper userMapper;

QueryWrapper qw=new QueryWrapper();
    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    @Override
    public User getByUsername(String username) {
        qw.eq("username",username);
        User user = userMapper.selectOne(qw);
        qw.clear();
        return user;
    }


}
