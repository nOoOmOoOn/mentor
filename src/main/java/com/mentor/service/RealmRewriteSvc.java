package com.mentor.service;

import com.mentor.entity.UnifiedUser;

/**
 * 域名绑定服务
 * 
 * @author zoro
 *
 */
public interface RealmRewriteSvc {
	
	/**
	 * 判断是否是微信
	 * @param request
	 * @return
	 */
	public boolean ifOauthWechat(Object request) throws Exception;
	
	public boolean ifNotWechat(Object request) throws Exception;
	
	public boolean ifNotUser(Object request) throws Exception;

	public boolean ifNotWechatInfo(Object request) throws Exception;
	
	public boolean checkLogin(Object request);
	
	public boolean ifWechatExists(Object request);
	
	public void registerMember(Object request, Object response);
	
	public void loginMember(Object request, Object response);
	
	public UnifiedUser retrieve(Object request);
}
