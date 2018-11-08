package org.mit.irb.web.IRBProtocol.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IRBExemptProtocolDao {
	IRBViewProfile getPersonExemptFormList(String personID, String personRoleType, String title, String piName,
			String determination, String facultySponsorname, String exemptStartDate, String exemptEndDate)
			throws ParseException;

	void savePersonExemptForm(IRBExemptForm irbExemptForm, String actype) throws ParseException;

	ArrayList<HashMap<String, Object>> getExemptMsg(IRBExemptForm irbExemptForm);

	Integer getNextExemptId();

	IRBViewProfile getPersonExemptForm(Integer exemptFormId, String string);

	ArrayList<HashMap<String, Object>> getLeadunitAutoCompleteList();

	void irbExemptFormActionLog(Integer formId, String actionTypeCode, String comment, String exemptstatusCode,
			String updateUser, Integer notificationNumber, PersonDTO personDTO);

	void addExemptProtocolAttachments(MultipartFile[] files, IRBExemptForm jsonObj);

	void updateExemptprotocolAttachments(IRBExemptForm jsonObj);

	ArrayList<HashMap<String, Object>> getExemptProtocolActivityLogs(Integer exemptFormID);

	ResponseEntity<byte[]> downloadExemptProtocolAttachments(String checkListId);

	ArrayList<HashMap<String, Object>> getExemptProtocolAttachmentList(Integer exemptFormID);
}
