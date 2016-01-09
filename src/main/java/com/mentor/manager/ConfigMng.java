package com.mentor.manager;

import java.util.Map;

import com.common.email.EmailSender;
import com.common.email.MessageTemplate;
import com.mentor.entity.Config;
import com.mentor.entity.Config.ConfigLogin;
import com.mentor.entity.Config.ConfigOrder;

public interface ConfigMng {
	public Map<String, String> getMap();

	public ConfigLogin getConfigLogin();
	
	public ConfigOrder getConfigOrder();

	public EmailSender getEmailSender();

	public MessageTemplate getForgotPasswordMessageTemplate();
	
	public MessageTemplate getRegisterMessageTemplate();

	public String getValue(String id);

	public void updateOrSave(Map<String, String> map);

	public Config updateOrSave(String key, String value);

	public Config deleteById(String id);

	public Config[] deleteByIds(String[] ids);
	
	
	public final static String B2C_RANK_ID="微店月度等级编号";
	public final static String B2B_FZ_TIME="分账间隔时间";
	public final static String B2C_SHOP_CLOSE_TIME="微店订单关闭时间";
	public final static String USER_EPITHET_TEYUE="特约称号";
	public final static String USER_EPITHET_2_NUM="二级经销商等级数";
	public final static String B2B_SCORE_REBATE_PROP_1="勋章一节返点比例";
	public final static String B2B_SCORE_REBATE_PROP_2="勋章二节返点比例";
	public final static String B2B_SCORE_REBATE_PROP_3="勋章三节返点比例";
	public final static String B2B_SCORE_REBATE_PROP_TIME="勋章特约间隔月数";
}
