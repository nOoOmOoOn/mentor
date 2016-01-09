package com.mentor.dao;

import java.util.List;

import com.mentor.entity.Page;

public interface PageDao {

	long add(Page Page);
	
	Page get(Long id);
	
	List<Page> getByArticle(Long articleId, Boolean isDraft);
	
	void update(Long id, String content, Integer number, Boolean isDraft);
	
	void delete(Long id);
}
