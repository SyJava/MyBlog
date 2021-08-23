package com.example.blog.security;

import com.example.blog.entity.User;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
@Configuration
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {

        User user = userService.getByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("用户名不正确");//用这个替换了new UsernameNotFoundException("用户名或密码不正确")；
        }
        return new AccountUser(user.getId(), user.getUsername(), user.getPassword(),user.getAvatar());
//		可以在这里加权限列表，也可以在JwtAuthenticationFilter中加
    }

    /**
     * 获取用户权限信息（角色、菜单权限）,本项目暂未涉及到权限处理
     * @param userId
     * @return
     */
//    public List<GrantedAuthority> getUserAuthority(Long userId){
//
//        // 角色(ROLE_admin)、菜单操作权限 sys:user:list
//        String authority = userService.getUserAuthorityInfo(userId);  // ROLE_admin,ROLE_normal,sys:user:list,....
//
//        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
//    }
}
