package org.mit.irb.web.notification;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.springframework.stereotype.Service;

@Service(value = "exemptEmailNotificationservice")
public class ExemptProtocolEmailNotification implements ExemptEmailNotificationservice{
	
	DBEngine dbEngine;
	Logger logger = Logger.getLogger(ExemptProtocolEmailNotification.class.getName());
	public static final String notificationType = "EXEMPT";
	
	ExemptProtocolEmailNotification(){
		dbEngine = new DBEngine();
	}
	
	@Override
	public void sendingExemptEmailNotifications(Integer exemptFormId, String comments, String loginPersonId, Integer notificationNumber) {
		String functionExecuteStatus = null;
		try{
			logger.info("exemptFormId: "+exemptFormId);
			logger.info("comments: "+comments);
			logger.info("loginPersonId: "+loginPersonId);
			logger.info("notificationNumber: "+notificationNumber);
			logger.info("notificationType: "+notificationType);
			ArrayList<OutParameter> outParam = new ArrayList<>();
			ArrayList<InParameter> inputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_MODULE_ITEM_ID", DBEngineConstants.TYPE_INTEGER, exemptFormId));
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_CLOB, comments));
			inputParam.add(new InParameter("AV_SENDER_PERSON_ID", DBEngineConstants.TYPE_STRING, loginPersonId));
			inputParam.add(new InParameter("AV_NOTIFICATION_TYPE", DBEngineConstants.TYPE_STRING, notificationType));
			inputParam.add(new InParameter("AV_NOTIFICATION_NUMBER", DBEngineConstants.TYPE_INTEGER, notificationNumber));
			outParam.add(new OutParameter("status",DBEngineConstants.TYPE_STRING));
			ArrayList<HashMap<String,Object>> result = dbEngine.executeFunction(inputParam,"pkg_mitkc_mail_generic.fn_send_irb_notification",outParam);
			if(result != null && !result.isEmpty()){
				HashMap<String,Object> hmResult = result.get(0);
				functionExecuteStatus = (String) hmResult.get("status");
			}		
		}catch(Exception e){
			logger.error("Error in methord sendingExemptEmailNotifications function",e);
		}
	}
}
