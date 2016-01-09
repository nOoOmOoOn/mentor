package com.mentor.manager;

import java.util.List;

import com.mentor.entity.Page;

public interface PageMng {
	
	Page get(Long id);
	
	List<Page> getByArticle(Long articleId, Boolean isDraft);
	
	Page add(Long articleId, Integer number, String content, Boolean isDraft);
	
	void update(Long id, String content, Integer number, Boolean isDraft);
	
	void delete(Long id);

}