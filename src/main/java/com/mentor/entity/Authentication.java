package com.mentor.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;

import com.mentor.entity.base.BaseAuthentication;

@Entity
public class Authentication extends BaseAuthentication{

	public Authentication(){}

	public Authentication(String id,Long uid, String username, String email,
			Date loginTime, String loginIp, Date updateTime) {
		super(id,uid, username, email, loginTime, loginIp, updateTime);
	}

	public void init() {
		Date now = new Timestamp(System.currentTimeMillis());
		setLoginTime(now);
		setUpdateTime(now);
	}
	
	private static final long serialVersionUID = -7477152212489875476L;
}
