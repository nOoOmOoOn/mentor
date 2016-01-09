package com.mentor.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentor.dao.ArticleDao;
import com.mentor.entity.Article;
import com.mentor.entity.Page;
import com.mentor.manager.ArticleMng;
import com.mentor.manager.ArticleVisitorMng;
import com.mentor.manager.PageMng;

@Service
@Transactional
public class ArticleMngImpl implements ArticleMng {

	public Article get(Long id) {
		Article entity = dao.get(id);
		return entity;
	}
	
	public List<Article> getByUser(Long userId) {
		List<Article> articles = dao.getByUser(userId);
		return articles;
	}

	public Article add(Long userId) {
		Article article = new Article();
		article.setUserId(userId);
		article.setStatus(Article.PublishStatus.未发布.toString());
		article.init();
		
		Long articleId = dao.add(article); 
		article.setId(articleId);
		
		pageMng.add(articleId, 1, null, true);
		
		return article;
	}
	
	/**
	 * 只有单页能调用
	 */
	public void publishSinglePage(Long id, Date publishTime) {
		List<Page> notDraftPages = pageMng.getByArticle(id, false);
		List<Page> draftPages = pageMng.getByArticle(id, true);
		if (notDraftPages.size()>0) {
			pageMng.update(notDraftPages.get(0).getArticleId(), draftPages.get(0).getContent(), null, null);
		} else {
			pageMng.add(draftPages.get(0).getArticleId(), 1, draftPages.get(0).getContent(), false);
		}
		
		dao.update(id, Article.PublishStatus.已发布.toString(), null, null, null, publishTime, null);
		
	}
	
	public void setStatus(Long id, String status) {
		dao.update(id, status, null, null, null, null, null);
	}
	
	public void setCover(Long id, String title, String intro, String cover) {
		dao.update(id, null, title, intro, cover, null, null);
	}
	
	public void softDelete(Long id) {
		dao.softDelete(id);
	}
	
	public void addVisitor(Long articleId, Long userId, Integer visitorNum) {
		if (articleVisitorMng.getByDetails(articleId, userId)!=null) {
			articleVisitorMng.add(articleId, userId);
			dao.update(articleId, null, null, null, null, null, visitorNum+1);
		}
	}

	@Autowired
	private ArticleDao dao;
	@Autowired
	private ArticleVisitorMng articleVisitorMng;
	@Autowired
	private PageMng pageMng;

}