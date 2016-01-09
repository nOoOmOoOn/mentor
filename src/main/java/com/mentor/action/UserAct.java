package com.mentor.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.web.WebErrors;
import com.mentor.entity.NormalUser;
import com.mentor.entity.UnifiedUser;
import com.mentor.manager.NormalUserMng;
import com.wechat.entity.Partner;
import com.wechat.manager.PartnerMng;
import com.wechat.plugins.WechatConfigSvc;

@Controller
public class UserAct {
	
	/**
	 *  显性授权，为了获取用户昵称及头像
	 */
	@RequestMapping(value="/user/getWechatInfo.jhtml")
	public String getWechatInfo(HttpServletRequest request, ModelMap model, String url, UnifiedUser user){
		
		WebErrors errors = validate(request, user);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}
		
		HttpSession session = ((HttpServletRequest) request).getSession();
		NormalUser normalUser = normalUserMng.getByExternalNo(user.getId());
		
		Partner partner = partnerMng.get(new Long((long)1));
		WxMpService wxMpService = wechatConfigSvc.createWxMpService(
				partner.getAppId(), partner.getSecretKey(), partner.getToken());
		try {
			WxMpOAuth2AccessToken wxMpOAuth2AccessToken = (WxMpOAuth2AccessToken) session.getAttribute("access_token");;
			WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
			String username = wxMpUser.getNickname();
			String avatar = wxMpUser.getHeadImgUrl();
			normalUserMng.update(normalUser.getId(), null, username, avatar);
		} catch (WxErrorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return "redirect:"+url;
	}
	
	private WebErrors validate(HttpServletRequest request, UnifiedUser user) {
		WebErrors errors = WebErrors.create(request);
		if (user==null) {
			errors.addError("基本用户不存在");
			return errors;
		}
		NormalUser normalUser = normalUserMng.getByExternalNo(user.getId());
		if (normalUser==null) {
			errors.addError("用户不存在");
		}
		return errors;
	}
	
	@Autowired
	private NormalUserMng normalUserMng;
	@Autowired
	private PartnerMng partnerMng;
	@Autowired
	private WechatConfigSvc wechatConfigSvc;
	
}
