package com.mentor.entity;

import java.util.Date;

import javax.persistence.Entity;

import com.mentor.entity.base.BaseUnifiedUser;

@Entity
public class UnifiedUser extends BaseUnifiedUser{

	public UnifiedUser(){}
	
	public UnifiedUser(String username, String email,String phone, String password,
			Date registerTime, String registerIp, Date lastLoginTime,
			String lastLoginIp, Integer loginCount, String resetKey,
			String resetPwd, Date errorTime, Integer errorCount,
			String errorIp, Boolean activation, String activationCode,String wecaht) {
		super(username, email,phone, password, registerTime, registerIp, lastLoginTime,
				lastLoginIp, loginCount, resetKey, resetPwd, errorTime, errorCount,
				errorIp, activation, activationCode,wecaht);
	}
	
	public void init(){
		if(getLoginCount()==null){
			setLoginCount(0);
		}
		if(getErrorCount()==null){
			setErrorCount(0);
		}
	}

	private static final long serialVersionUID = -8074012954781665491L;
}
