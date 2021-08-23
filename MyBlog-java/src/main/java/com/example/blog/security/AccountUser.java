package com.example.blog.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
@Data
@ToString
@NoArgsConstructor
public class AccountUser implements Serializable, UserDetails {

	private Integer id;

	private String username;

	private String password;

	private String avatar;


	public AccountUser(Integer id, String username, String password,String avatar) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.avatar=avatar;
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
//以下方法均应返回true
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
