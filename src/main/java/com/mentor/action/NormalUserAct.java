package com.mentor.action;

import java.util.List;

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
import com.mentor.entity.Article;
import com.mentor.entity.NormalUser;
import com.mentor.entity.Tree;
import com.mentor.entity.UnifiedUser;
import com.mentor.manager.ArticleMng;
import com.mentor.manager.NormalUserMng;
import com.mentor.manager.TreeMng;
import com.wechat.entity.Partner;
import com.wechat.manager.PartnerMng;
import com.wechat.manager.WechatUserMng;
import com.wechat.plugins.WechatConfigSvc;

@Controller
public class NormalUserAct {
	
	@RequestMapping(value="/member/relationship.html")
	public String relationship(HttpServletRequest request, ModelMap model, UnifiedUser user){
		
		WebErrors errors = validate(request, user);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}
		
		NormalUser normalUser = normalUserMng.getByExternalNo(user.getId());
		
		Tree tree = treeMng.getByUsername(normalUser.getId()+"");
		Integer leftNum = tree.getLeftNum();
		Integer rightNum = tree.getRightNum();
		Integer rankNum = tree.getRankNum();
		
		model.addAttribute("id", normalUser.getId());
		model.addAttribute("senior_list", treeMng.getFathersByRank(leftNum, rightNum, rankNum, 3));
		model.addAttribute("junior_1_list", treeMng.getChildrenByRank(leftNum, rightNum, rankNum, 1));
		model.addAttribute("junior_2_list", treeMng.getChildrenByRank(leftNum, rightNum, rankNum, 2));
		model.addAttribute("junior_3_num", treeMng.getChildrenNumByRank(leftNum, rightNum, rankNum, 3));
		
		return "/member/relationship";
	}
	
	@RequestMapping(value="/member/relationship_details.html")
	public String relationshipDetails(HttpServletRequest request, ModelMap model, UnifiedUser user, Integer rank){
		
		WebErrors errors = validateRelationshipDetails(request, user, rank);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}
		
		NormalUser normalUser = normalUserMng.getByExternalNo(user.getId());
		
		Tree tree = treeMng.getByUsername(normalUser.getId()+"");
		Integer leftNum = tree.getLeftNum();
		Integer rightNum = tree.getRightNum();
		Integer rankNum = tree.getRankNum();
		
		model.addAttribute("id", normalUser.getId());
		model.addAttribute("rank", rank);
		
		if (rank==0) {
			model.addAttribute("list", treeMng.getFathersByRank(leftNum, rightNum, rankNum, 10));
		} else {
			model.addAttribute("list", treeMng.getChildrenByRank(leftNum, rightNum, rankNum, rank));
		}
		
		return "/member/relationship_details";
	}
	
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
	
	@RequestMapping(value="/member/homepage.html")
	public String homepage(HttpServletRequest request, ModelMap model, UnifiedUser user){
		
		WebErrors errors = validate(request, user);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}
		
		NormalUser normalUser = normalUserMng.getByExternalNo(user.getId());
		
		List<Article> articleList = articleMng.getByNormalUser(normalUser.getId());
		
		model.addAttribute("normalUser", normalUser);
		model.addAttribute("articleList", articleList);
		
		return "/member/homepage";
	}
	
	private WebErrors validateRelationshipDetails(HttpServletRequest request, UnifiedUser user, Integer rank) {
		WebErrors errors = validate(request, user);
		if (errors.hasErrors()) {
			return errors;
		}
		if (rank==null) {
			errors.addError("级数不存在");
		}
		if (rank<0 || rank>10) {
			errors.addError("级数越级");
		}
		return errors;
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
	private ArticleMng articleMng;
	@Autowired
	private TreeMng treeMng;
	@Autowired
	private WechatUserMng wechatUserMng;
	@Autowired
	private NormalUserMng normalUserMng;
	@Autowired
	private PartnerMng partnerMng;
	@Autowired
	private WechatConfigSvc wechatConfigSvc;
	
}
