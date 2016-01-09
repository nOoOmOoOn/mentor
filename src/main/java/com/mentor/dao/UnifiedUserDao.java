package com.mentor.dao;

import java.util.Date;
import java.util.List;

import com.common.jdbc.page.Pagination;
import com.mentor.entity.UnifiedUser;

public interface UnifiedUserDao {

	public UnifiedUser getByUsername(String username);

	public List<UnifiedUser> getByEmail(String email);
	
	UnifiedUser getByPhone(String phone);
	
	UnifiedUser getByWechat(String wechat);

	public int countByEmail(String email);

	public Pagination getPage(int pageNo, int pageSize);

	void update(Long id, Date lastLoginTime, String lastLoginIp,
			Integer loginCount);

	void update(Long id, String password);

	void update(Long id, String username, String phone);
	
	void update(Long id, String username,String wechat, String phone);
	
	void update(Long id, String username,boolean ifUsernameNull);
	
	public void update(Long id, Boolean activation, Boolean disabled);

	void updatePasswordResetNullKeyAndPwd(Long id, String password,
			String resetKey, String resetPwd);

	void updateResetKeyAndPwd(Long id, String resetKey, String resetPwd);

	void update(Long id, Date errorTime, Integer errorCount, String errorIp);

	public UnifiedUser findById(Long id);

	public long add(UnifiedUser bean);

	public UnifiedUser deleteById(Long id);
}
