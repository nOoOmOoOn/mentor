package com.mentor.dao;

import java.util.Date;
import java.util.List;

import com.mentor.entity.Article;

public interface ArticleDao {

	long add(Article Article);
	
	Article get(Long id);
	
	List<Article> getByNormalUser(Long normalUserId);
	
	void update(Long id, String status, String title, String intro, String cover, Date publishTime, Integer visitorNum);
	
	void softDelete(Long id);
	
	void delete(Long id);
}
