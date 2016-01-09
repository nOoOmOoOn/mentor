package com.mentor.manager.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.email.EmailSendTool;
import com.common.email.EmailSender;
import com.common.email.MessageTemplate;
import com.common.jdbc.page.Pagination;
import com.common.security.BadCredentialsException;
import com.common.security.DisabledException;
import com.common.security.UsernameNotFoundException;
import com.common.security.encoder.PwdEncoder;
import com.mentor.dao.UnifiedUserDao;
import com.mentor.entity.UnifiedUser;
import com.mentor.entity.Config.ConfigLogin;
import com.mentor.manager.ConfigMng;
import com.mentor.manager.UnifiedUserMng;

@Service
@Transactional
public class UnifiedUserMngImpl implements UnifiedUserMng {

	public UnifiedUser passwordForgotten(Long userId, EmailSender email,
			MessageTemplate tpl) {
		UnifiedUser user = findById(userId);
		String uuid = StringUtils.remove(UUID.randomUUID().toString(), '-');
		user.setResetKey(uuid);
		String resetPwd = RandomStringUtils.randomNumeric(10);
		user.setResetPwd(resetPwd);
		dao.updateResetKeyAndPwd(user.getId(), user.getResetKey(),
				user.getResetPwd());
		senderEmail(user.getId(), user.getUsername(), user.getEmail(),
				user.getResetKey(), user.getResetPwd(), email, tpl);
		return user;
	}

	private void senderEmail(final Long uid, final String username,
			final String to, final String resetKey, final String resetPwd,
			final EmailSender email, final MessageTemplate tpl) {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(email.getHost());
		sender.setUsername(email.getUsername());
		sender.setPassword(email.getPassword());
		sender.send(new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage)
					throws MessagingException, UnsupportedEncodingException {
				MimeMessageHelper msg = new MimeMessageHelper(mimeMessage,
						false, email.getEncoding());
				msg.setSubject(tpl.getForgotPasswordSubject());
				msg.setTo(to);
				msg.setFrom(email.getUsername(), email.getPersonal());
				String text = tpl.getForgotPasswordText();
				text = StringUtils.replace(text, "${uid}", String.valueOf(uid));
				text = StringUtils.replace(text, "${username}", username);
				text = StringUtils.replace(text, "${resetKey}", resetKey);
				text = StringUtils.replace(text, "${resetPwd}", resetPwd);
				msg.setText(text, true);
			}
		});
	}

	private void senderEmail(final String username, final String to,
			final String activationCode, final EmailSender email,
			final MessageTemplate tpl) throws UnsupportedEncodingException,
			MessagingException {
		/*
		 * JavaMailSenderImpl sender = new JavaMailSenderImpl();
		 * sender.setHost(email.getHost());
		 * sender.setUsername(email.getUsername());
		 * sender.setPassword(email.getPassword()); sender.send(new
		 * MimeMessagePreparator() { public void prepare(MimeMessage
		 * mimeMessage) throws MessagingException, UnsupportedEncodingException
		 * { MimeMessageHelper msg; msg = new MimeMessageHelper(mimeMessage,
		 * false, email.getEncoding());
		 * msg.setSubject(tpl.getRegisterSubject()); msg.setTo(to);
		 * msg.setFrom(email.getUsername(), email.getPersonal()); String text =
		 * tpl.getRegisterText(); text = StringUtils.replace(text,
		 * "${username}", username); text = StringUtils.replace(text,
		 * "${activationCode}", activationCode); msg.setText(text); } });
		 */
		String text = tpl.getRegisterText();
		text = StringUtils.replace(text, "${username}", username);
		text = StringUtils.replace(text, "${activationCode}", activationCode);
		EmailSendTool sendEmail = new EmailSendTool(email.getHost(),
				email.getUsername(), email.getPassword(), to,
				tpl.getRegisterSubject(), text, email.getPersonal(), "", "");
		sendEmail.send();
	}

	public UnifiedUser resetPassword(Long userId) {
		UnifiedUser user = findById(userId);
		user.setPassword(pwdEncoder.encodePassword(user.getResetPwd()));
		user.setResetKey(null);
		user.setResetPwd(null);
		dao.updatePasswordResetNullKeyAndPwd(user.getId(), user.getPassword(),
				user.getResetKey(), user.getResetPwd());
		return user;
	}

	public Integer errorRemaining(String username) {
		if (StringUtils.isBlank(username)) {
			return null;
		}
		UnifiedUser user = getByUsername(username);
		if (user == null) {
			return null;
		}
		long now = System.currentTimeMillis();
		ConfigLogin configLogin = configMng.getConfigLogin();
		int maxErrorTimes = configLogin.getErrorTimes();
		int maxErrorInterval = configLogin.getErrorInterval() * 60 * 1000;
		Integer errorCount = user.getErrorCount();
		Date errorTime = user.getErrorTime();
		if (errorCount <= 0 || errorTime == null
				|| errorTime.getTime() + maxErrorInterval < now) {
			return maxErrorTimes;
		}
		return maxErrorTimes - errorCount;
	}

	public UnifiedUser login(String username, String password, String ip)
			throws UsernameNotFoundException, BadCredentialsException,
			DisabledException {
		UnifiedUser user = getByUsername(username);
		if (user == null) {
			user = getByPhone(username);
		}
		if (user == null) {
			throw new UsernameNotFoundException("username not found: "
					+ username);
		}
		if (!pwdEncoder.isPasswordValid(user.getPassword(), password)) {
			updateLoginError(user.getId(), ip);
			throw new BadCredentialsException("password invalid");
		}
		if (isDisabled(username)) {
			throw new DisabledException("account is disabled");
		}
		updateLoginSuccess(user.getId(), ip);
		return user;
	}

	// 用户禁用返回true 未查找到用户或者非禁用返回false
	private boolean isDisabled(String username) {
		UnifiedUser user = dao.getByUsername(username);
		if (user == null) {
			user = getByPhone(username);
		}
		if (user != null) {
			if (user.getDisabled() != null && user.getDisabled()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void updateLoginSuccess(Long userId, String ip) {
		UnifiedUser user = findById(userId);
		Date now = new Timestamp(System.currentTimeMillis());

		user.setLoginCount(user.getLoginCount() + 1);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);

		user.setErrorCount(0);
		user.setErrorTime(null);
		user.setErrorIp(null);
		dao.update(userId, user.getLastLoginTime(), ip, user.getLoginCount());
	}

	public void updateLoginError(Long userId, String ip) {
		UnifiedUser user = findById(userId);
		Date now = new Timestamp(System.currentTimeMillis());
		ConfigLogin configLogin = configMng.getConfigLogin();
		int errorInterval = configLogin.getErrorInterval();
		Date errorTime = user.getErrorTime();

		user.setErrorIp(ip);
		if (errorTime == null
				|| errorTime.getTime() + errorInterval * 60 * 1000 < now
						.getTime()) {
			user.setErrorTime(now);
			user.setErrorCount(1);
		} else {
			user.setErrorCount(user.getErrorCount() + 1);
		}
		dao.update(userId, user.getErrorTime(), user.getErrorCount(), ip);
	}

	public boolean usernameExist(String username) {
		return getByUsername(username) != null;
	}

	public boolean emailExist(String email) {
		return dao.countByEmail(email) > 0;
	}

	@Override
	public UnifiedUser getByUsernamePhone(String username) {
		UnifiedUser user = getByUsername(username);
		if (user == null) {
			user = getByPhone(username);
		}
		return user;
	}

	public UnifiedUser getByUsername(String username) {
		return dao.getByUsername(username);
	}

	@Override
	public UnifiedUser getByPhone(String phone) {
		return dao.getByPhone(phone);
	}

	@Override
	public UnifiedUser getByWechat(String wechat) {
		return dao.getByWechat(wechat);
	}

	public List<UnifiedUser> getByEmail(String email) {
		return dao.getByEmail(email);
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public UnifiedUser findById(Long id) {
		UnifiedUser entity = dao.findById(id);
		return entity;
	}
	
	public long add(UnifiedUser user) {
		return dao.add(user);
	}
	
	public UnifiedUser save(String username, String email, String phone,
			String password, String ip) {
		Date now = new Timestamp(System.currentTimeMillis());
		UnifiedUser user = new UnifiedUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPhone(phone);
		user.setPassword(pwdEncoder.encodePassword(password));
		user.setRegisterIp(ip);
		user.setRegisterTime(now);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);
		user.setDisabled(false);
		
		// 不强制验证邮箱直接激活
		user.setActivation(true);
		user.init();
		user.setId(dao.add(user));
		return user;
	}

	@Override
	public UnifiedUser save(String username, String wechat, String email,
			String phone, String password, String ip) {
		Date now = new Timestamp(System.currentTimeMillis());
		UnifiedUser user = new UnifiedUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPhone(phone);
		user.setPassword(pwdEncoder.encodePassword(password));
		user.setRegisterIp(ip);
		user.setRegisterTime(now);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);
		user.setWechat(wechat);
		// 不强制验证邮箱直接激活
		user.setActivation(true);
		user.init();
		user.setId(dao.add(user));
		return user;
	}

	public UnifiedUser save(String username, String email, String password,
			String ip, Boolean activation, EmailSender sender,
			MessageTemplate msgTpl) throws UnsupportedEncodingException,
			MessagingException {
		Date now = new Timestamp(System.currentTimeMillis());
		UnifiedUser user = new UnifiedUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(pwdEncoder.encodePassword(password));
		user.setRegisterIp(ip);
		user.setRegisterTime(now);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);
		user.setActivation(activation);
		user.init();
		user.setId(dao.add(user));
		if (!activation) {
			String uuid = StringUtils.remove(UUID.randomUUID().toString(), '-');
			user.setActivationCode(uuid);
			senderEmail(username, email, uuid, sender, msgTpl);
		}
		return user;
	}

	/**
	 * @see UnifiedUserMng#update(Integer, String, String)
	 */
	public void update(Long id, String password) {
		dao.update(id, pwdEncoder.encodePassword(password));
	}

	@Override
	public void update(Long id, String username, String phone) {
		dao.update(id, username, phone);
	}

	@Override
	public void update(Long id, Boolean activation, Boolean disabled) {
		dao.update(id, activation, disabled);
	}

	@Override
	public void update(Long id, String username, String wechat, String phone) {
		dao.update(id, username, wechat, phone);
	}

	public boolean isPasswordValid(Long id, String password) {
		UnifiedUser user = findById(id);
		return pwdEncoder.isPasswordValid(user.getPassword(), password);
	}

	public UnifiedUser deleteById(Long id) {
		UnifiedUser bean = dao.deleteById(id);
		return bean;
	}

	public UnifiedUser[] deleteByIds(Long[] ids) {
		UnifiedUser[] beans = new UnifiedUser[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	public UnifiedUser active(String username, String activationCode) {
		UnifiedUser bean = getByUsername(username);
		bean.setActivation(true);
		bean.setActivationCode(null);
		return bean;
	}

	public UnifiedUser activeLogin(UnifiedUser user, String ip) throws DisabledException {
		if (isDisabled(user.getUsername())) {
				throw new DisabledException("account is disabled");
		}
		updateLoginSuccess(user.getId(), ip);
		return user;
	}

	@Override
	public void update(Long id, String username, boolean ifUsernameNull) {
		dao.update(id, username, ifUsernameNull);
	}

	private ConfigMng configMng;
	private PwdEncoder pwdEncoder;
	private UnifiedUserDao dao;

	@Autowired
	public void setConfigMng(ConfigMng configMng) {
		this.configMng = configMng;
	}

	@Autowired
	public void setPwdEncoder(PwdEncoder pwdEncoder) {
		this.pwdEncoder = pwdEncoder;
	}

	@Autowired
	public void setDao(UnifiedUserDao dao) {
		this.dao = dao;
	}
	
}