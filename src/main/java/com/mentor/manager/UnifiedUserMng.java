package com.mentor.manager;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import com.common.email.EmailSender;
import com.common.email.MessageTemplate;
import com.common.jdbc.page.Pagination;
import com.common.security.BadCredentialsException;
import com.common.security.DisabledException;
import com.common.security.UsernameNotFoundException;
import com.mentor.entity.UnifiedUser;


public interface UnifiedUserMng {
	/**
	 * 忘记密码
	 * 
	 * @param userId
	 *            用户ID
	 * @param email
	 *            发送者邮件信息
	 * @param tpl
	 *            邮件模板。内容模板可用变量${uid}、${username}、${resetKey}、${resetPwd}。
	 * @return
	 */
	public UnifiedUser passwordForgotten(Long userId, EmailSender email,
			MessageTemplate tpl);

	/**
	 * 重置密码
	 * 
	 * @param userId
	 * @return
	 */
	public UnifiedUser resetPassword(Long userId);

	public Integer errorRemaining(String username);

	public UnifiedUser login(String username, String password, String ip)
			throws UsernameNotFoundException, BadCredentialsException, DisabledException;

	public boolean usernameExist(String username);

	public boolean emailExist(String email);

	public UnifiedUser getByUsernamePhone(String username);
	
	public UnifiedUser getByUsername(String username);
	
	public UnifiedUser getByWechat(String wechat);
	
	public UnifiedUser getByPhone(String username);

	public List<UnifiedUser> getByEmail(String email);

	public Pagination getPage(int pageNo, int pageSize);

	public UnifiedUser findById(Long id);
	
	public long add(UnifiedUser user);

	public UnifiedUser save(String username, String email, String phone,String password,
			String ip);
	
	public UnifiedUser save(String username,String wechat, String email, String phone,String password,
			String ip);
	
	public UnifiedUser save(String username, String email, String password,
			String ip, Boolean activation, EmailSender sender, MessageTemplate msgTpl)throws UnsupportedEncodingException, MessagingException;

	/**
	 * 修改邮箱和密码
	 * 
	 * @param id
	 *            用户ID
	 * @param password
	 *            未加密密码。如果为null或空串则不修改。
	 * @return
	 */
	void update(Long id, String password);
	
	void update(Long id, String username,String phone);
	
	void update(Long id, String username,String wechat,String phone);
	
	void update(Long id, String username,boolean ifUsernameNull);
	
	void update(Long id, Boolean activation,Boolean disabled);

	/**
	 * 密码是否正确
	 * 
	 * @param id
	 *            用户ID
	 * @param password
	 *            未加密密码
	 * @return
	 */
	public boolean isPasswordValid(Long id, String password);

	public UnifiedUser deleteById(Long id);

	public UnifiedUser[] deleteByIds(Long[] ids);
	
	public UnifiedUser active(String username, String activationCode);

	public UnifiedUser activeLogin(UnifiedUser user, String ip) throws DisabledException;
}