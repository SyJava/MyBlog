package com.example.blog.security;

import cn.hutool.core.util.StrUtil;
import com.example.blog.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtils jwtUtils;

    public JwtAuthenticticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(jwtUtils.getHeader());
        System.out.println(jwt);
        if(StrUtil.isBlankOrUndefined(jwt)){
            chain.doFilter(request,response);
        return;
        }
        Claims claim = jwtUtils.getClaimByToken(jwt);
        System.out.println(claim);
        if (claim==null){
            throw new JwtException("token异常");
        }
        if(jwtUtils.isTokenExpired(claim)){
            throw new JwtException("token过期");
        }
        String username = claim.getSubject();
        //获取用户的权限信息
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(username,null,null);
//        放到上下文中
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request,response);
    }
}
