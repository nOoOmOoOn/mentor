package com.mentor.web;

import static com.mentor.manager.AuthenticationMng.AUTH_KEY;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.common.web.session.SessionProvider;
import com.mentor.entity.Authentication;
import com.mentor.entity.UnifiedUser;
import com.mentor.manager.AuthenticationMng;
import com.mentor.manager.UnifiedUserMng;

/**
 * Spring mvc 方法参数处理器 <br>
 * method 参数包含 UnifiedUser <br>
 * 都用进过 resolveArgument方法处理填充数据
 * 
 * @author zoro
 */
public class CoreMethodArgumentResolver implements
		HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return UnifiedUser.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
			String authId = (String) session.getAttribute(
					(HttpServletRequest) webRequest.getNativeRequest(),
					AUTH_KEY);
			if (authId != null) {
				Authentication auth = authMng.retrieve(authId);
				return unifiedUserMng.findById(auth.getUid());
			}
		return null;
	}

	private String module;

	public void setModule(String module) {
		this.module = module;
	}

	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private AuthenticationMng authMng;
}
