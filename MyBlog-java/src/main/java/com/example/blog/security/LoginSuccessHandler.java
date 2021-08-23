package com.example.blog.security;

import cn.hutool.json.JSONUtil;
import com.example.blog.entity.User;
import com.example.blog.utils.JwtUtils;
import com.example.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class LoginSuccessHandler implements AuthenticationSuccessHandler {//定义登录成功处理器
@Autowired
JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream=httpServletResponse.getOutputStream();

        //生成jwt，并放置到请求头中
        String jwt = jwtUtils.generateToken(authentication.getName());//获取用户名
        httpServletResponse.setHeader(jwtUtils.getHeader(),jwt);//将jwt放在请求头中
        AccountUser accountUser = (AccountUser) authentication.getPrincipal();//从authentication中获取登录用户
        User user = new User(accountUser.getId(), accountUser.getUsername(), accountUser.getAvatar());
        Result result= Result.succ(user);//将user部分信息返回
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}



