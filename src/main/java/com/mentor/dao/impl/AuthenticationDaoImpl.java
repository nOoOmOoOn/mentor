package com.mentor.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.common.jdbc.page.Pagination;
import com.mentor.dao.AuthenticationDao;
import com.mentor.entity.Authentication;

@Repository
public class AuthenticationDaoImpl extends JdbcTemplateBaseDao implements
		AuthenticationDao {

	@Override
	public int deleteExpire(Date d) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"delete from Authentication where 1=1");
		if (d != null) {
			sqlBuilder.andLessThanOrEqualTo("updateTime", d);
		}
		return update(sqlBuilder);
	}

	@Override
	public Authentication getByUserId(Long userId) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from Authentication where 1=1");
		if (userId != null) {
			sqlBuilder.andEqualTo("uid", userId);
		}
		return queryForObject(sqlBuilder);
	}

	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		SqlBuilder sqlBuilder = new SqlBuilder("select * from Authentication");
		return getPage(sqlBuilder, pageNo, pageSize);
	}

	@Override
	public Authentication findById(String id) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from Authentication where 1=1");
		if (id != null) {
			sqlBuilder.andEqualTo("authId", id);
		}
		return queryForObject(sqlBuilder);
	}

	@Override
	public void save(Authentication bean) {
		getJdbcTemplate()
				.update("insert into Authentication (authId, email, loginIp, loginTime, uid, updateTime, username)values (?, ?, ?, ?, ?,?,?)",
						new Object[] { bean.getId(), bean.getEmail(),
								bean.getLoginIp(), bean.getLoginTime(),
								bean.getUid(), bean.getUpdateTime(),
								bean.getUsername() });
	}

	@Override
	public void update(String id, Date updateTime) {
		SqlBuilder sqlBuilder = new SqlBuilder("update Authentication set authId=authId");
		if (updateTime != null) {
			sqlBuilder.set("updateTime", updateTime);
		}
		if (id != null) {
			sqlBuilder.append(" where authId='" ).append(id).append("'");
		}
		update(sqlBuilder);
	}

	@Override
	public Authentication deleteById(String id) {
		Authentication auth = findById(id);
		if (auth != null) {
			SqlBuilder sqlBuilder = new SqlBuilder(
					"delete from Authentication where 1=1");
			if (id != null) {
				sqlBuilder.andEqualTo("authId", id);
			}
			update(sqlBuilder);
		}
		return auth;
	}

	@Override
	protected Class<?> getEntityClass() {
		return Authentication.class;
	}
}
