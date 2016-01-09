package com.mentor.service.impl;

import static com.mentor.manager.AuthenticationMng.AUTH_KEY;
import static com.wechat.action.WechatConfigAct.WECHAT_ERROR_COUNT;
import static com.wechat.action.WechatConfigAct.WECHAT_OPEN_ID;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.common.bean.NewInstance;
import com.common.security.DisabledException;
import com.common.urlrewrite.NormalRewrittenUrl;
import com.common.web.RequestUtils;
import com.common.web.session.HttpSessionProvider;
import com.common.web.springmvc.PropertyPlaceholderConfigurer;
import com.common.web.util.WebUtils;
import com.mentor.entity.NormalUser;
import com.mentor.entity.Authentication;
import com.mentor.entity.UnifiedUser;
import com.wechat.entity.WechatUser;
import com.mentor.manager.NormalUserMng;
import com.mentor.manager.AuthenticationMng;
import com.mentor.manager.TreeMng;
import com.mentor.manager.UnifiedUserMng;
import com.wechat.manager.WechatUserMng;
import com.mentor.service.RealmRewriteSvc;
import com.wechat.service.Config;

@Service
public class RealmRewriteSvcImpl implements RealmRewriteSvc {

	private Logger log = LoggerFactory.getLogger(RealmRewriteSvcImpl.class);

	public static final int TWO_WEEKS_S = Integer.MAX_VALUE;
	public static final String DEFAULT_REALM = PropertyPlaceholderConfigurer
			.getProperties("realm");

	@Override
	public boolean ifOauthWechat(Object request) throws Exception {
		log.info("ifOauthWechat url:{}",
				RequestUtils.getLocation(((HttpServletRequest) request)));
		String userAgent = ((HttpServletRequest) request)
				.getHeader("user-agent");
		boolean ifWechatOs = StringUtils.contains(userAgent.toLowerCase(),
				"micromessenger");
		HttpSession session = ((HttpServletRequest) request).getSession();
		if (!ifWechatOs) {
			return false;
		}
		if (session.getAttribute(WECHAT_OPEN_ID) != null) {
			return false;
		}
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = (WxMpOAuth2AccessToken) session.getAttribute("access_token");
		if (wxMpOAuth2AccessToken != null && wxMpOAuth2AccessToken.getOpenId() != null) {
			session.setAttribute(WECHAT_OPEN_ID, wxMpOAuth2AccessToken.getOpenId());
			return false;
		}
		Integer errorCount = (Integer) session.getAttribute(WECHAT_ERROR_COUNT);
		if (errorCount != null && errorCount > 5) {
			return false;
		}
		String realm = WebUtils.getRealm((HttpServletRequest) request);
		((HttpServletRequest) request).setAttribute(NormalRewrittenUrl.$REALM,
				realm);
		return true;
	}

	/*
	 * 有openid的session，返回false
	 */
	@Override
	public boolean ifNotWechat(Object request) throws Exception {
		HttpSession session = ((HttpServletRequest) request).getSession();
		if (session.getAttribute(WECHAT_OPEN_ID) != null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean ifNotUser(Object request) throws Exception {
		HttpSession session = ((HttpServletRequest) request).getSession();
		if (session.getAttribute(WECHAT_OPEN_ID)==null)
			return true;
		WechatUser wechatUser = NewInstance.get(WechatUserMng.class)
				.getByOpenId(new Long((long) 1),
						session.getAttribute(WECHAT_OPEN_ID).toString());
		if (wechatUser == null)
			return true;
		return false;
	}
	
	@Override
	public boolean ifNotWechatInfo(Object request) throws Exception {
		HttpSession session = ((HttpServletRequest) request).getSession();
		WechatUser wechatUser = NewInstance.get(WechatUserMng.class)
				.getByOpenId(new Long((long) 1),
						session.getAttribute(WECHAT_OPEN_ID).toString());
		NormalUser normalUser = NewInstance.get(NormalUserMng.class)
				.getByExternalNo(wechatUser.getExternalNo());
		if (normalUser.getUsername()==null)
			return true;
		return false;
	}
	
	@Override
	public boolean checkLogin(Object request) {
		return retrieve((HttpServletRequest) request) != null;
	}
	
	@Override
	public boolean ifWechatExists(Object request) {
		HttpSession session = ((HttpServletRequest) request).getSession();
		String openId = (String) session.getAttribute(WECHAT_OPEN_ID);
		if (openId == null) {
			return false;
		}
		WechatUserMng wechatUserMng = NewInstance.get(WechatUserMng.class);
		WechatUser wechatUser = wechatUserMng.getByOpenId(
				Config.DEFAULT_PARTENR, openId);
		if (wechatUser == null) {
			return false;
		}
		return true;
	}
	
	@Override
	public void registerMember(Object request, Object response) {
		
		HttpSession session = ((HttpServletRequest) request).getSession();
		String openId = (String) session.getAttribute(WECHAT_OPEN_ID);
		if (openId == null) {
			return;
		}
		String fatherId = ((HttpServletRequest)request).getParameter("fid");
		if (fatherId == null) {
			return;
		}
		
		//add unifiedUser
		Long partnerId = new Long((long)1);
		Date now = new Timestamp(System.currentTimeMillis());
		String ip = RequestUtils.getIpAddr((HttpServletRequest) request);
		UnifiedUser user = new UnifiedUser();
		
		user.setUsername(openId);
		user.setRegisterIp(ip);
		user.setLastLoginIp(ip);
		user.setRegisterTime(now);
		user.setLastLoginTime(now);
		user.setDisabled(false);
		user.setActivation(true);
		user.init();
		
		UnifiedUserMng unifiedUserMng = NewInstance.get(UnifiedUserMng.class);
		user.setId(unifiedUserMng.add(user));
		
		//add wechatUser
		WechatUserMng wechatUserMng = NewInstance.get(WechatUserMng.class);
		wechatUserMng.add(partnerId, user.getId(), openId);
		
		//add normalUser
		NormalUser normalUser = new NormalUser();
		normalUser.setExternalNo(user.getId());
		normalUser.setUsername(null);
		normalUser.setAvatar(null);
		normalUser.setFatherId(Long.valueOf(fatherId));
		NormalUserMng normalUserMng = NewInstance.get(NormalUserMng.class);
		normalUser.setId(normalUserMng.add(normalUser));
		
		//add tree
		TreeMng treeMng = NewInstance.get(TreeMng.class);
		if (treeMng.getByUsername(fatherId)!=null)
			treeMng.add(user.getId()+"", fatherId);
	}
	
	@Override
	public void loginMember(Object request, Object response) {
		HttpSession session = ((HttpServletRequest) request).getSession();
		String openId = (String) session.getAttribute(WECHAT_OPEN_ID);
		WechatUserMng wechatUserMng = NewInstance.get(WechatUserMng.class);
		WechatUser wechatUser = wechatUserMng.getByOpenId(
				Config.DEFAULT_PARTENR, openId);
		if (wechatUser.getExternalNo() == null) {
			return;
		}
		UnifiedUserMng unifiedUserMng = NewInstance.get(UnifiedUserMng.class);
		UnifiedUser unifiedUser = unifiedUserMng.findById(wechatUser
				.getExternalNo());
		AuthenticationMng authMng = NewInstance.get(AuthenticationMng.class);
		try {
			authMng.activeLogin(unifiedUser,
					RequestUtils.getIpAddr((HttpServletRequest) request),
					(HttpServletRequest) request,
					(HttpServletResponse) response, new HttpSessionProvider());
		} catch (DisabledException e) {
			log.error("账户名:{} 已经被禁止使用", unifiedUser.getUsername());
		}
	}
	
	@Override
	public UnifiedUser retrieve(Object request) {
		AuthenticationMng authMng = NewInstance
				.get(AuthenticationMng.class);
		UnifiedUserMng unifiedUserMng = NewInstance
				.get(UnifiedUserMng.class);
		HttpSession session = ((HttpServletRequest) request).getSession();
		String authId = (String) session.getAttribute(AUTH_KEY);
		if (authId == null) {
			return null;
		}
		Authentication auth = authMng.retrieve(authId);
		return unifiedUserMng.findById(auth.getUid());
	}
}
