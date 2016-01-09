package com.mentor.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.security.UsernameNotFoundException;
import com.common.security.userdetails.UserDetailsService;
import com.mentor.entity.UnifiedUser;
import com.mentor.manager.UnifiedUserMng;

@Service
@Transactional
public class MemberMngImpl implements UserDetailsService {

	@Override
	public UserDetails loadUser(Long userId, String username)
			throws UsernameNotFoundException, DataAccessException {
		UnifiedUser user = userMng.findById(userId);
		return user == null || !user.getUsername().equals(username) ? null
				: new UserDetails(user.getId(), user.getUsername(), user.getPassword());
	}

	public static class UserDetails implements com.common.security.userdetails.UserDetails{
		
		public UserDetails(Long id,String username,String password){
			this.Id=id;
			this.username=username;
			this.password=password;
		}
		
		private Long Id;
		private String username;
		private String password;
		
		private static final long serialVersionUID = -4733663430761573473L;

		@Override
		public boolean isEnabled() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}
		
		@Override
		public Long getId() {
			return Id;
		}
		
		@Override
		public String getUsername() {
			return username;
		}
		
		@Override
		public String getPassword() {
			return password;
		}
	}
	
	@Autowired
	private UnifiedUserMng userMng;
}
