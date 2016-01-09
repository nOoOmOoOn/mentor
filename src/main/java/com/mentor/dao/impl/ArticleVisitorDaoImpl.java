package com.mentor.dao.impl;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.mentor.dao.ArticleVisitorDao;
import com.mentor.entity.ArticleVisitor;

@Repository
public class ArticleVisitorDaoImpl extends JdbcTemplateBaseDao implements ArticleVisitorDao {

	@Override
	protected Class<?> getEntityClass() {
		return ArticleVisitor.class;
	}
	
	public long add(ArticleVisitor articleVisitor) {
		return super.add(articleVisitor);
	}

	public ArticleVisitor get(Long id) {
		return super.queryForObject(id);
	}
	
	public ArticleVisitor getByDetails(Long articleId, Long normalUserId) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from ArticleVisitor where 1=1");
		if (articleId != null) {
			sqlBuilder.andEqualTo("articleId", articleId);
		}
		if (normalUserId != null) {
			sqlBuilder.andEqualTo("normalUserId", normalUserId);
		}
		return queryForObject(sqlBuilder);
	}
	
}
