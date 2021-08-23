package com.example.blog.config;

import com.example.blog.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity//加载Security的一些安全策略
@EnableGlobalMethodSecurity(prePostEnabled = true)//在post请求之前进行权限的校验
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Autowired
    LoginSuccessHandler loginSuccessHandler;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    CaptchaFilter captchaFilter;
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;
    @Bean
    JwtAuthenticticationFilter jwtAuthenticticationFilter() throws Exception {
        return new JwtAuthenticticationFilter(authenticationManager());
    }
   @Bean
   BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
}

public static final String[] URL_WHITELIST={ //定义访问白名单，不被拦截器拦截
        "/captcha",
        "/logout",
        "/blog/vuefindByPage","/blog/getByBlogId",
        "/type/getAllType","/tag/getAllTag","/blog/getByTitle",
        "/comment/saveComments","/comment/getComments/{blogId}",
        "/blog/countBlog","/comment/getCommentByPage",
        "/blog/search",
        "/links/getAllLink","/blog/vuefindHotBlog",
        "/getCount"
};
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                //登录配置,获取表单格式数据，而不是json格式
                .formLogin()
                .loginProcessingUrl("/login")
                //登录失败处理器
                       .failureHandler(loginFailureHandler)
                //登录成功处理器
                       .successHandler(loginSuccessHandler)
                //退出配置
                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)
                //禁用session
                .and()
                      .sessionManagement()
                      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//定义session生成策略为不生成
                //配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()//不被拦截的白名单
                .anyRequest().authenticated()

                 //配置异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                 //配置自定义的验证码过滤器
                .and()
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)//在验证账号前先走验证码过滤器
                .addFilter(jwtAuthenticticationFilter());

    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
}
