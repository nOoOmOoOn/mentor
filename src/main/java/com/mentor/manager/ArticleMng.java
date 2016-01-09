package com.mentor.manager;

import java.util.Date;
import java.util.List;

import com.mentor.entity.Article;

public interface ArticleMng {
	
	Article get(Long id);
	
	List<Article> getByNormalUser(Long normalUserId);
	
	Article add(Long normalUserId);
	
	void publishSinglePage(Long id, Date publishTime);
	
	void setStatus(Long id, String status);
	
	void setCover(Long id, String title, String intro, String cover);
	
	void softDelete(Long id);
	
	void addVisitor(Long articleId, Long normalUserId, Integer visitorNum);

}