package com.mentor.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.common.jdbc.page.Pagination;
import com.mentor.dao.UnifiedUserDao;
import com.mentor.entity.UnifiedUser;

@Repository
public class UnifiedUserDaoImpl extends JdbcTemplateBaseDao implements
		UnifiedUserDao {

	@Override
	public UnifiedUser getByUsername(String username) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from UnifiedUser where 1=1");
		if (username != null) {
			sqlBuilder.andEqualTo("username", username);
		}
		return queryForObject(sqlBuilder);
	}

	@Override
	public List<UnifiedUser> getByEmail(String email) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from UnifiedUser where 1=1");
		if (email != null) {
			sqlBuilder.andEqualTo("email", email);
		}
		return query(sqlBuilder);
	}

	@Override
	public UnifiedUser getByPhone(String phone) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from UnifiedUser where 1=1");
		if (phone != null) {
			sqlBuilder.andEqualTo("phone", phone);
		}
		return queryForObject(sqlBuilder);
	}

	@Override
	public UnifiedUser getByWechat(String wechat) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from UnifiedUser where 1=1");
		if (wechat != null) {
			sqlBuilder.andEqualTo("wechat", wechat);
		}
		return queryForObject(sqlBuilder);
	}

	@Override
	public int countByEmail(String email) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select count(*) from UnifiedUser where 1=1");
		if (email != null) {
			sqlBuilder.andEqualTo("email", email);
		}
		return super.queryForPrimitive(sqlBuilder, Integer.class);
	}

	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from UnifiedUser where 1=1");
		return getPage(sqlBuilder, pageNo, pageSize);
	}

	@Override
	public UnifiedUser findById(Long id) {
		return super.queryForObject(id);
	}

	@Override
	public long add(UnifiedUser bean) {
		return super.add(bean);
	}

	@Override
	public UnifiedUser deleteById(Long id) {
		UnifiedUser unifiedUser = queryForObject(id);
		if (unifiedUser != null) {
			SqlBuilder sqlBuilder = new SqlBuilder(
					"delete from UnifiedUser where 1=1");
			if (id != null) {
				sqlBuilder.andEqualTo("id", id);
			}
			update(sqlBuilder);
		}
		return unifiedUser;
	}

	@Override
	protected Class<?> getEntityClass() {
		return UnifiedUser.class;
	}

	@Override
	public void update(Long id, Date lastLoginTime, String lastLoginIp,
			Integer loginCount) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update UnifiedUser set gmtModify=current_timestamp()");
		if (lastLoginTime != null) {
			sqlBuilder.set("lastLoginTime", lastLoginTime);
		}
		if (lastLoginIp != null) {
			sqlBuilder.set("lastLoginIp", lastLoginIp);
		}
		if (loginCount != null) {
			sqlBuilder.set("loginCount", loginCount);
		}
		update(id, sqlBuilder);
	}

	@Override
	public void update(Long id, Date errorTime, Integer errorCount,
			String errorIp) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update UnifiedUser set gmtModify=current_timestamp()");
		if (errorTime != null) {
			sqlBuilder.set("errorTime", errorTime);
		}

		if (errorCount != null) {
			sqlBuilder.set("errorCount", errorCount);
		}
		if (errorIp != null) {
			sqlBuilder.set("errorIp", errorIp);
		}
		update(id, sqlBuilder);
	}

	@Override
	public void update(Long id, String password) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update UnifiedUser set gmtModify=current_timestamp()");
		if (password != null) {
			sqlBuilder.set("password", password);
		}
		update(id, sqlBuilder);
	}

	@Override
	public void update(Long id, String username, String phone) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update UnifiedUser set gmtModify=current_timestamp()");
		if (username != null) {
			sqlBuilder.set("username", username);
		}
		if (phone != null) {
			sqlBuilder.set("phone", phone);
		}
		update(id, sqlBuilder);
	}

	@Override
	public void update(Long id, String username, String wechat, String phone) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update UnifiedUser set gmtModify=current_timestamp()");
		if (username != null) {
			sqlBuilder.set("username", username);
		}
		if (wechat != null) {
			sqlBuilder.set("wechat", wechat);
		}
		if (phone != null) {
			sqlBuilder.set("phone", phone);
		}
		update(id, sqlBuilder);
	}

	@Override
	public void update(Long id, String username, boolean ifUsernameNull) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update UnifiedUser set gmtModify=current_timestamp()");
		if (username != null || ifUsernameNull) {
			sqlBuilder.set("username", username);
		}
		update(id, sqlBuilder);
	}
	
	@Override
	public void update(Long id, Boolean activation, Boolean disabled) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update UnifiedUser set gmtModify=current_timestamp()");
		if (sqlBuilder.ifNotNull(activation)) {
			sqlBuilder.set("activation", activation);
		}
		if (sqlBuilder.ifNotNull(disabled)) {
			sqlBuilder.set("disabled", disabled);
		}
		super.update(id, sqlBuilder);
	}

	@Override
	public void updateResetKeyAndPwd(Long id, String resetKey, String resetPwd) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update UnifiedUser set gmtModify=current_timestamp()");
		if (resetKey != null) {
			sqlBuilder.set("resetKey", resetKey);
		}
		if (resetPwd != null) {
			sqlBuilder.set("resetPwd", resetPwd);
		}
		update(id, sqlBuilder);
	}

	@Override
	public void updatePasswordResetNullKeyAndPwd(Long id, String password,
			String resetKey, String resetPwd) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update UnifiedUser set gmtModify=current_timestamp()");
		if (password != null) {
			sqlBuilder.set("password", password);
		}
		sqlBuilder.set("resetKey", resetKey);
		sqlBuilder.set("resetPwd", resetPwd);
		update(id, sqlBuilder);
	}
}
