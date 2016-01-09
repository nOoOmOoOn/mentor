package com.mentor.action;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.web.WebErrors;
import com.mentor.entity.Article;
import com.mentor.entity.NormalUser;
import com.mentor.entity.Page;
import com.mentor.entity.UnifiedUser;
import com.mentor.manager.ArticleMng;
import com.mentor.manager.NormalUserMng;
import com.mentor.manager.PageMng;
import com.wechat.manager.WechatUserMng;

@Controller
public class ArticleAct {
	
	@RequestMapping(value="/article/article.html")
	public String article(HttpServletRequest request, ModelMap model, UnifiedUser user, Long articleId, String fid){
		WebErrors errors = validate(request, user);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}
		
		Article article = articleMng.get(articleId);
		if (article==null || article.getIsDelete())
			errors.addError("作品不存在");
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}
		
		NormalUser normalUser = normalUserMng.get(user.getId());
		articleMng.addVisitor(articleId, normalUser.getId(), article.getVisitorNum());
		
		Page page = pageMng.getByArticle(articleId, false).get(0);
		model.addAttribute("article", article);
		model.addAttribute("page", page);
		
		if (fid==null) {
			model.addAttribute("gfid", article.getNormalUserId()+"");
		} else {
			model.addAttribute("gfid", fid);
		}
		model.addAttribute("fid", normalUser.getId());
		
		return "/article/article";
	}
	
	@RequestMapping(value="/not_wechat/article.html")
	public String notWechatArticle(HttpServletRequest request, ModelMap model, Long articleId, String fid, String gfid){
		WebErrors errors = WebErrors.create(request);
		Article article = articleMng.get(articleId);
		if (article==null || article.getIsDelete())
			errors.addError("作品不存在");
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}

		model.addAttribute("gfid", gfid);
		model.addAttribute("fid", fid);

		Page page = pageMng.getByArticle(articleId, false).get(0);
		model.addAttribute("article", article);
		model.addAttribute("page", page);
		
		return "/article/article";
	}
	
	@RequestMapping(value="/article/add.jhtml")
	public String add(HttpServletRequest request, ModelMap model, UnifiedUser user){
		WebErrors errors = validate(request, user);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}
		NormalUser normalUser = normalUserMng.getByExternalNo(user.getId());
		Article article = articleMng.add(normalUser.getId());
		model.addAttribute("articleId", article.getId());
		return "redirect:/article/edit";
	}
	
	@RequestMapping(value="/article/edit.html")
	public String edit(HttpServletRequest request, ModelMap model, UnifiedUser user, Long articleId){
		WebErrors errors = validateArticle(request, user, articleId);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}
		
		Article article = articleMng.get(articleId);
		if (article.getStatus().equals(Article.PublishStatus.已发布.toString())) {
			articleMng.setStatus(articleId, Article.PublishStatus.修改未发布.toString());
		}
		
		Page page = pageMng.getByArticle(articleId, true).get(0);
		model.addAttribute("article", article);
		model.addAttribute("page", page);
		return "/article/edit";
	}
	
	/**
	 * 单页保存
	 */
	@RequestMapping(value="/article/save_single_page.jhtml")
	public void saveSinglePage(HttpServletRequest request, ModelMap model, UnifiedUser user, Long pageId, String content){
		WebErrors errors = validateSaveSinglePage(request, user, pageId);
		if (errors.hasErrors()) {
			return;
		}
		pageMng.update(pageId, content, 1, null);
		return;
	}
	
	/**
	 * 保存封面
	 */
	@RequestMapping(value="/article/set_cover.jhtml")
	public void setCover(HttpServletRequest request, ModelMap model, UnifiedUser user, Long articleId, String title, String intro, String cover){
		WebErrors errors = validateArticle(request, user, articleId);
		if (errors.hasErrors()) {
			return;
		}
		articleMng.setCover(articleId, title, intro, cover);
		return;
	}
	
	/**
	 * 发布
	 */
	@RequestMapping(value="/article/publish_single_page.jhtml")
	public void publishSinglePage(HttpServletRequest request, ModelMap model, UnifiedUser user, Long articleId){
		WebErrors errors = validatePublishSinglePage(request, user, articleId);
		if (errors.hasErrors()) {
			return;
		}
		articleMng.publishSinglePage(articleId, new Timestamp(System.currentTimeMillis()));
		return;
	}
	
	/**
	 * 软删除
	 */
	@RequestMapping(value="/article/soft_delete.jhtml")
	public void softDelete(HttpServletRequest request, ModelMap model, UnifiedUser user, Long articleId){
		WebErrors errors = validateArticle(request, user, articleId);
		if (errors.hasErrors()) {
			return;
		}
		articleMng.softDelete(articleId);
		return;
	}
	
	@RequestMapping(value="/article/test.jhtml")
	public String test(HttpServletRequest request, ModelMap model, UnifiedUser user){
		model.addAttribute("userId", user.getId());
		return "/safe/test";
	}
	
	private WebErrors validateSaveSinglePage(HttpServletRequest request, UnifiedUser user, Long pageId) {
		WebErrors errors = WebErrors.create(request);
		Page page = pageMng.get(pageId);
		if (page==null) {
			errors.addError("作品页不存在");
			return errors;
		}
		 
		errors = validateArticle(request, user, page.getArticleId());
		if (errors.hasErrors()) {
			return errors;
		}

		if (!page.getIsDraft()) {
			errors.addError("作品页非草稿,不可编辑");
			return errors;
		}
		return errors;
	}
	
	private WebErrors validatePublishSinglePage(HttpServletRequest request, UnifiedUser user, Long articleId) {	
		WebErrors errors = validateArticle(request, user, articleId);
		if (errors.hasErrors()) {
			return errors;
		}
		
		List<Page> draftPages = pageMng.getByArticle(articleId, true);
		if (draftPages.size()!=1) {
			errors.addError("作品草稿页非单页");
			return errors;
		}
		
		List<Page> notDraftPages = pageMng.getByArticle(articleId, true);
		if (notDraftPages.size()>1) {
			errors.addError("作品原发布页非单页");
			return errors;
		}
		return errors;
	}
	
	private WebErrors validateArticle(HttpServletRequest request, UnifiedUser user, Long articleId) {
		WebErrors errors = validate(request, user);
		if (errors.hasErrors()) {
			return errors;
		}
		
		Article article = articleMng.get(articleId);
		if (article==null) {
			errors.addError("作品不存在");
			return errors;
		}
		if (article.getIsDelete()) {
			errors.addError("作品已删除");
			return errors;
		}
		if (article.getNormalUserId()!=normalUserMng.getByExternalNo(user.getId()).getId()) {
			errors.addError("作品与作者参数不吻合");
			return errors;
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
	private PageMng pageMng;
	@Autowired
	private ArticleMng articleMng;
	@Autowired
	private WechatUserMng wechatUserMng;
	@Autowired
	private NormalUserMng normalUserMng;
	
}
