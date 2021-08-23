package com.example.blog.controller;


import com.example.blog.entity.User;
import com.example.blog.service.UserService;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 *
 * @author sy
 * @since 2021-07-21
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
//    修改密码
    @PutMapping("/updatePass")
    public Result updatePass(String currentPass, String password, Principal principal) {

        User user = userService.getByUsername(principal.getName());

        boolean matches = passwordEncoder.matches(currentPass, user.getPassword());
        if (!matches) {
            return Result.fail("旧密码不正确");
        }
        user.setPassword(passwordEncoder.encode(password));
        userService.updateById(user);
        return Result.succ("密码修改成功,请重新登录");
    }
}

