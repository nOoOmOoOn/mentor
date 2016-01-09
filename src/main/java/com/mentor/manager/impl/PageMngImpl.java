package com.mentor.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentor.dao.PageDao;
import com.mentor.entity.Page;
import com.mentor.manager.PageMng;

@Service
@Transactional
public class PageMngImpl implements PageMng {

	public Page get(Long id) {
		Page entity = dao.get(id);
		return entity;
	}
	
	public List<Page> getByArticle(Long articleId, Boolean isDraft) {
		List<Page> pages = dao.getByArticle(articleId, isDraft);
		return pages;
	}

	public Page add(Long articleId, Integer number, String content, Boolean isDraft) {
		Page page = new Page();
		page.setArticleId(articleId);
		page.setNumber(number);
		page.setContent(content);
		page.setIsDraft(isDraft);
		page.setId(dao.add(page));
		
		return page;
	}
	
	public void update(Long id, String content, Integer number, Boolean isDraft) {
		dao.update(id, content, number, isDraft);
	}
	
	public void delete(Long id) {
		dao.delete(id);
	}

	@Autowired
	private PageDao dao;

}