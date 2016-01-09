package com.mentor.manager.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.email.EmailSender;
import com.common.email.MessageTemplate;
import com.common.util.DateUtils;
import com.mentor.dao.ConfigDao;
import com.mentor.entity.Config;
import com.mentor.entity.Config.ConfigEmailSender;
import com.mentor.entity.Config.ConfigLogin;
import com.mentor.entity.Config.ConfigMessageTemplate;
import com.mentor.entity.Config.ConfigOrder;
import com.mentor.manager.ConfigMng;


@Service
@Transactional
public class ConfigMngImpl implements ConfigMng {
	@Transactional(readOnly = true)
	public Map<String, String> getMap() {
		List<Config> list = dao.getList();
		Map<String, String> map = new HashMap<String, String>(list.size());
		for (Config config : list) {
			map.put(config.getId(), config.getValue());
		}
		return map;
	}

	@Transactional(readOnly = true)
	public String getValue(String id) {
		Config entity = dao.findById(id);
		if (entity != null) {
			return entity.getValue();
		} else {
			return null;
		}
	}

	@Transactional(readOnly = true)
	public ConfigLogin getConfigLogin() {
		return ConfigLogin.create(getMap());
	}

	@Transactional(readOnly = true)
	public EmailSender getEmailSender() {
		return ConfigEmailSender.create(getMap());
	}

	@Transactional(readOnly = true)
	public MessageTemplate getForgotPasswordMessageTemplate() {
		return ConfigMessageTemplate.createForgotPasswordMessageTemplate(getMap());
	}
	
	@Transactional(readOnly = true)
	public MessageTemplate getRegisterMessageTemplate() {
		return ConfigMessageTemplate.createRegisterMessageTemplate(getMap());
	}

	public void updateOrSave(Map<String, String> map) {
		if (map != null && map.size() > 0) {
			for (Entry<String, String> entry : map.entrySet()) {
				updateOrSave(entry.getKey(), entry.getValue());
			}
		}
	}

	public Config updateOrSave(String key, String value) {
		Config config = dao.findById(key);
		if (config != null) {
			dao.update(config.getId(), value);
		} else {
			config = new Config(key);
			config.setValue(value);
			dao.save(config);
		}
		return config;
	}

	public Config deleteById(String id) {
		Config bean = dao.deleteById(id);
		return bean;
	}

	public Config[] deleteByIds(String[] ids) {
		Config[] beans = new Config[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Override
	public ConfigOrder getConfigOrder() {
		Config config = dao.findById(ConfigOrder.ORDER_FREE_DAYS.toString());
		int free = 7;
		if (config != null) {
			free = Integer.parseInt(config.getValue());
		}
		return new ConfigOrder(DateUtils.add(new Date(), free, free));
	}
	
	private ConfigDao dao;

	@Autowired
	public void setDao(ConfigDao dao) {
		this.dao = dao;
	}
}