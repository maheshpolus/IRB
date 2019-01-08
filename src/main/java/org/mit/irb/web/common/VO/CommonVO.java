package org.mit.irb.web.common.VO;

import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.questionnaire.dto.QuestionnaireDto;

/**
 * Value object for service argument.
 *
 */
public class CommonVO {

	private String userName;

	private String password;
	
	private String personId;
	
	private String personRoleType;
	
	private String protocolNumber;
	
	private String title;
	
	private String leadunitNumber;
	
	private String protocolTypeCode;
	
	private String dashboardType;

	private String avPersonId;
	
	private String attachmentId;
	
	private String avSummaryType;
	
	private String piName;
	
	private Integer protocolId;
	
	private Integer actionId;
	
	private Integer nextGroupActionId;
	
	private Integer previousGroupActionId;
	
	private IRBExemptForm irbExemptForm;
	
	private QuestionnaireDto questionnaireDto;
	
	private PersonDTO personDTO;
	
	private String questionnaireInfobean;
	
	private String exemptMessage;
	
	private Integer questionId;
	
	private String question;
	
	private String isExemptGranted;

	private ArrayList<QuestionnaireDto> questionnaireDtos;
	
	private ArrayList<HashMap<String, Object>> exemptQuestionList;
	
	private String determination;

	private ArrayList<HashMap<String, Object>> actionLogs;
	
	private String exemptFormfacultySponsorName;
	
	private String exemptFormStartDate;
	
	private String exemptFormEndDate;
	
	private Integer protocolActionId;
	
	private String protocolActionTypecode;
	
	private String protocolStatusCode;
	
	public ArrayList<HashMap<String, Object>> getExemptQuestionList() {
		return exemptQuestionList;
	}

	public void setExemptQuestionList(ArrayList<HashMap<String, Object>> exemptQuestionList) {
		this.exemptQuestionList = exemptQuestionList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonRoleType() {
		return personRoleType;
	}

	public void setPersonRoleType(String personRoleType) {
		this.personRoleType = personRoleType;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLeadunitNumber() {
		return leadunitNumber;
	}

	public void setLeadunitNumber(String leadunitNumber) {
		this.leadunitNumber = leadunitNumber;
	}

	public String getProtocolTypeCode() {
		return protocolTypeCode;
	}

	public void setProtocolTypeCode(String protocolTypeCode) {
		this.protocolTypeCode = protocolTypeCode;
	}

	public String getDashboardType() {
		return dashboardType;
	}

	public void setDashboardType(String dashboardType) {
		this.dashboardType = dashboardType;
	}

	public String getAvPersonId() {
		return avPersonId;
	}

	public void setAvPersonId(String avPersonId) {
		this.avPersonId = avPersonId;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getAvSummaryType() {
		return avSummaryType;
	}

	public void setAvSummaryType(String avSummaryType) {
		this.avSummaryType = avSummaryType;
	}

	public String getPiName() {
		return piName;
	}

	public void setPiName(String piName) {
		this.piName = piName;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public Integer getNextGroupActionId() {
		return nextGroupActionId;
	}

	public void setNextGroupActionId(Integer nextGroupActionId) {
		this.nextGroupActionId = nextGroupActionId;
	}

	public Integer getPreviousGroupActionId() {
		return previousGroupActionId;
	}

	public void setPreviousGroupActionId(Integer previousGroupActionId) {
		this.previousGroupActionId = previousGroupActionId;
	}

	public IRBExemptForm getIrbExemptForm() {
		return irbExemptForm;
	}

	public void setIrbExemptForm(IRBExemptForm irbExemptForm) {
		this.irbExemptForm = irbExemptForm;
	}

	public QuestionnaireDto getQuestionnaireDto() {
		return questionnaireDto;
	}

	public void setQuestionnaireDto(QuestionnaireDto questionnaireDto) {
		this.questionnaireDto = questionnaireDto;
	}

	public PersonDTO getPersonDTO() {
		return personDTO;
	}

	public void setPersonDTO(PersonDTO personDTO) {
		this.personDTO = personDTO;
	}

	public String getQuestionnaireInfobean() {
		return questionnaireInfobean;
	}

	public void setQuestionnaireInfobean(String questionnaireInfobean) {
		this.questionnaireInfobean = questionnaireInfobean;
	}

	public String getExemptMessage() {
		return exemptMessage;
	}

	public void setExemptMessage(String exemptMessage) {
		this.exemptMessage = exemptMessage;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getIsExemptGranted() {
		return isExemptGranted;
	}

	public void setIsExemptGranted(String isExemptGranted) {
		this.isExemptGranted = isExemptGranted;
	}

	public ArrayList<QuestionnaireDto> getQuestionnaireDtos() {
		return questionnaireDtos;
	}

	public void setQuestionnaireDtos(ArrayList<QuestionnaireDto> questionnaireDtos) {
		this.questionnaireDtos = questionnaireDtos;
	}

	public String getDetermination() {
		return determination;
	}

	public void setDetermination(String determination) {
		this.determination = determination;
	}

	public ArrayList<HashMap<String, Object>> getActionLogs() {
		return actionLogs;
	}

	public void setActionLogs(ArrayList<HashMap<String, Object>> actionLogs) {
		this.actionLogs = actionLogs;
	}

	public String getExemptFormfacultySponsorName() {
		return exemptFormfacultySponsorName;
	}

	public void setExemptFormfacultySponsorName(String exemptFormfacultySponsorName) {
		this.exemptFormfacultySponsorName = exemptFormfacultySponsorName;
	}

	public String getExemptFormStartDate() {
		return exemptFormStartDate;
	}

	public void setExemptFormStartDate(String exemptFormStartDate) {
		this.exemptFormStartDate = exemptFormStartDate;
	}

	public String getExemptFormEndDate() {
		return exemptFormEndDate;
	}

	public void setExemptFormEndDate(String exemptFormEndDate) {
		this.exemptFormEndDate = exemptFormEndDate;
	}

	public Integer getProtocolActionId() {
		return protocolActionId;
	}

	public void setProtocolActionId(Integer protocolActionId) {
		this.protocolActionId = protocolActionId;
	}

	public String getProtocolActionTypecode() {
		return protocolActionTypecode;
	}

	public void setProtocolActionTypecode(String protocolActionTypecode) {
		this.protocolActionTypecode = protocolActionTypecode;
	}

	public String getProtocolStatusCode() {
		return protocolStatusCode;
	}

	public void setProtocolStatusCode(String protocolStatusCode) {
		this.protocolStatusCode = protocolStatusCode;
	}
}
