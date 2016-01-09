package com.mentor.web;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.common.security.DisabledException;
import com.common.security.rememberme.RememberMeService;
import com.common.security.userdetails.UserDetails;
import com.common.web.RequestUtils;
import com.common.web.session.SessionProvider;
import com.common.web.springmvc.PropertyPlaceholderConfigurer;
import com.common.web.springmvc.ViewResponse;
//admin import com.rose.admin.manager.AdminUserMng;
import com.mentor.entity.UnifiedUser;
import com.mentor.manager.AuthenticationMng;
import com.mentor.manager.UnifiedUserMng;

/**
 * 上下文信息拦截器
 * 
 * 包括登录信息、权限信息、站点信息
 */
public class CoreContextInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 用户KEY
	 */
	public static final String USER_KEY = "user_key",
			CURRENT_URI = "current_uri";

	private static long timeVersion = System.currentTimeMillis();

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		UnifiedUser user = null;
		// 正常状态
		Long userId = authMng.retrieveUserIdFromSession(session, request);
		if (userId != null) {
			user = unifiedUserMng.findById(userId);
		}

		// 时间版本控制器
		if (timeVersion != 0) {
			request.setAttribute("timeVersion", timeVersion);
		}

		String uri = getURI(request), returnUrl = request
				.getParameter("returnUrl");

		try {
			// cookie自动登录
			UserDetails details = rememberMeService
					.autoLogin(request, response);
			if (userId == null && details != null) {
				user = unifiedUserMng.findById(details.getId());
				authMng.activeLogin(user, RequestUtils.getIpAddr(request),
						request, response, session);
			}
			// 当用户已经登录验证用户是否被禁止访问
			else if (user != null) {
				if (!(user.getDisabled() == null || !user.getDisabled())) {
					throw new DisabledException("account is disabled");
				}
			}
		} catch (DisabledException e) {
			session.logout(request, response);
			user = null;
		} catch (Exception e) {
			session.logout(request, response);
		}

		// 测试并发时，测试还记模拟登录所有
		if (user == null) {
			user = testLogin(request, response);
		}

		if (user != null) {
			request.setAttribute(USER_KEY, user);
		}

		// 不在验证的范围内
		if (exclude(uri)) {
			return true;
		}

		ViewResponse viewResponse = new ViewResponse(request, response);
		// 用户为null跳转到登陆页面
		//admin  if (user == null || adminUserMng.getByUser(user.getId()) != null) {
		if (user == null) {
			viewResponse.render(loginUrl, returnUrl);
			return false;
		}

		return true;
	}
	


	/**
	 * 测试还记模拟登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected UnifiedUser testLogin(HttpServletRequest request,
			HttpServletResponse response) {
		UnifiedUser user = null;
		if (!StringUtils.equals(
				PropertyPlaceholderConfigurer.getProperties("env"), "dev")) {
			return user;
		}
		Long uid = null;
		try {
			uid = Long.valueOf(request.getParameter("uid"));
		} catch (Exception e) {
			return user;
		}
		user = unifiedUserMng.findById(uid);
		try {
			authMng.activeLogin(user, RequestUtils.getIpAddr(request), request,
					response, session);
		} catch (DisabledException e) {
		}
		return user;
	}

	private boolean exclude(String uri) {
		if (excludeUrls != null) {
			for (String exc : excludeUrls) {
				if (Pattern.matches(exc, uri)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获得第三个路径分隔符的位置
	 * 
	 * @param request
	 * @throws IllegalStateException
	 *             访问路径错误，没有三(四)个'/'
	 * @throws UnsupportedEncodingException
	 */
	private static String getURI(HttpServletRequest request)
			throws IllegalStateException, UnsupportedEncodingException {
		if (request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI) != null) {
			String result = (String) request
					.getAttribute(RequestDispatcher.INCLUDE_PATH_INFO);
			if (result == null) {
				result = (String) request
						.getAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH);
			} else {
				result = (String) request
						.getAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH)
						+ result;
			}
			if ((result == null) || (result.equals(""))) {
				result = "/";
			}
			return (result);
		}

		// No, extract the desired path directly from the request
		String result = request.getPathInfo();
		if (result == null) {
			result = request.getServletPath();
		} else {
			result = request.getServletPath() + result;
		}
		if ((result == null) || (result.equals(""))) {
			result = "/";
		}
		return result;
	}
	
	private RememberMeService rememberMeService;
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	//private AdminUserMng adminUserMng;
	private SessionProvider session;
	private AuthenticationMng authMng;
	private String[] excludeUrls;

	private String loginUrl;

	@Autowired
	public void setSession(SessionProvider session) {
		this.session = session;
	}

	@Autowired
	public void setAuthMng(AuthenticationMng authMng) {
		this.authMng = authMng;
	}

	@Autowired
	public void setUnifiedUserMng(UnifiedUserMng unifiedUserMng) {
		this.unifiedUserMng = unifiedUserMng;
	}

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	@Autowired
	public void setRememberMeService(RememberMeService rememberMeService) {
		this.rememberMeService = rememberMeService;
	}
}