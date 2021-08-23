package com.example.blog.service;

import com.example.blog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2021-07-21
 */
@Service
public interface UserService extends IService<User> {
public User getByUsername(String  username);
}
