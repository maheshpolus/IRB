package org.mit.irb.web.notification;

import java.sql.Clob;

public interface ExemptEmailNotificationservice {

	/*
	 * @param exemptFormId
	 * @param comments
	 * @param loginPersonId
	 * @param notificationNumber
	 * function to send exempt protocol notifications
	*/
	public void sendingExemptEmailNotifications(Integer exemptFormId, String comments, String loginPersonId,Integer notificationNumber);
}
