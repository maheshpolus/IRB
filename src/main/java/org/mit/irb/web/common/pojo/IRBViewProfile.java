package org.mit.irb.web.common.pojo;

/**
 * Class with all details about IRB protocol
 * 
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.pojo.CollaboratorNames;

public class IRBViewProfile {
	private HashMap<String, Object> irbViewHeader;
	private ArrayList<HashMap<String, Object>> irbViewProtocolPersons;
	private ArrayList<HashMap<String, Object>> irbViewProtocolFundingsource;
	private ArrayList<HashMap<String, Object>> irbViewProtocolLocation;
	private ArrayList<HashMap<String, Object>> irbViewProtocolVulnerableSubject;
	private ArrayList<HashMap<String, Object>> irbViewProtocolSpecialReview;
	private HashMap<String, Object> irbViewProtocolMITKCPersonInfo;
	private ArrayList<HashMap<String, Object>> irbViewProtocolMITKCPersonTrainingInfo;
	private ArrayList<HashMap<String, Object>> irbViewProtocolAttachmentList;
	private ArrayList<HashMap<String, Object>> irbViewProtocolHistoryGroupList;	
	private List<IRBExemptForm> irbExemptFormList;
	private ArrayList<HashMap<String, Object>> irbProtocolRoleMap;
	private ArrayList<HashMap<String, Object>> irbProtocolFundingSourceMap;
	private ArrayList<HashMap<String, Object>> irbProtocolSubjectMap;		
	private ArrayList<HashMap<String, Object>> irbProtocolLeadUnitMap;
	private ArrayList<HashMap<String, Object>> irbProtocolAffiliateMap;	
	private ArrayList<HashMap<String, Object>> irbProtocolTitleMap;		
	private ArrayList<HashMap<String, Object>> irbProtocolHistoryActionComments;		
	private Integer userHasRightToViewProtocol;
	private String trainingStatus;
	private ArrayList<HashMap<String, Object>> irbViewProtocolUnits;
	private ArrayList<HashMap<String, Object>> irbViewProtocolAdminContact;
	private ArrayList<HashMap<String, Object>> irbViewProtocolCollaboratorPersons;
	private ArrayList<HashMap<String, Object>> irbViewProtocolCollaboratorAttachments;
	private Integer userHasRightToEditTraining;
	private List<CollaboratorNames> collaboratorList;
	private ArrayList<HashMap<String, Object>> irbProtocolHistoryGroupComments;		
	
	public ArrayList<HashMap<String, Object>> getIrbViewProtocolHistoryGroupList() {
		return irbViewProtocolHistoryGroupList;
	}

	public void setIrbViewProtocolHistoryGroupList(ArrayList<HashMap<String, Object>> irbviewProtocolHistoryGroupList) {
		this.irbViewProtocolHistoryGroupList = irbviewProtocolHistoryGroupList;
	}

	public HashMap<String, Object> getIrbViewHeader() {
		return irbViewHeader;
	}

	public void setIrbViewHeader(HashMap<String, Object> irbViewHeader) {
		this.irbViewHeader = irbViewHeader;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolPersons() {
		return irbViewProtocolPersons;
	}

	public void setIrbViewProtocolPersons(ArrayList<HashMap<String, Object>> irbViewProtocolPersons) {
		this.irbViewProtocolPersons = irbViewProtocolPersons;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolFundingsource() {
		return irbViewProtocolFundingsource;
	}

	public void setIrbViewProtocolFundingsource(ArrayList<HashMap<String, Object>> irbViewProtocolFundingsource) {
		this.irbViewProtocolFundingsource = irbViewProtocolFundingsource;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolLocation() {
		return irbViewProtocolLocation;
	}

	public void setIrbViewProtocolLocation(ArrayList<HashMap<String, Object>> irbViewProtocolLocation) {
		this.irbViewProtocolLocation = irbViewProtocolLocation;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolVulnerableSubject() {
		return irbViewProtocolVulnerableSubject;
	}

	public void setIrbViewProtocolVulnerableSubject(
			ArrayList<HashMap<String, Object>> irbViewProtocolVulnerableSubject) {
		this.irbViewProtocolVulnerableSubject = irbViewProtocolVulnerableSubject;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolSpecialReview() {
		return irbViewProtocolSpecialReview;
	}

	public void setIrbViewProtocolSpecialReview(ArrayList<HashMap<String, Object>> irbViewProtocolSpecialReview) {
		this.irbViewProtocolSpecialReview = irbViewProtocolSpecialReview;
	}

	public HashMap<String, Object> getIrbViewProtocolMITKCPersonInfo() {
		return irbViewProtocolMITKCPersonInfo;
	}

	public void setIrbViewProtocolMITKCPersonInfo(HashMap<String,Object> irbViewProtocolMITKCPersonInfo) {
		this.irbViewProtocolMITKCPersonInfo = irbViewProtocolMITKCPersonInfo;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolMITKCPersonTrainingInfo() {
		return irbViewProtocolMITKCPersonTrainingInfo;
	}

	public void setIrbViewProtocolMITKCPersonTrainingInfo(
			ArrayList<HashMap<String, Object>> irbViewProtocolMITKCPersonTrainingInfo) {
		this.irbViewProtocolMITKCPersonTrainingInfo = irbViewProtocolMITKCPersonTrainingInfo;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolAttachmentList() {
		return irbViewProtocolAttachmentList;
	}

	public void setIrbViewProtocolAttachmentList(ArrayList<HashMap<String, Object>> irbViewProtocolAttachmentList) {
		this.irbViewProtocolAttachmentList = irbViewProtocolAttachmentList;
	}

	public List<IRBExemptForm> getIrbExemptFormList() {
		return irbExemptFormList;
	}

	public void setIrbExemptFormList(List<IRBExemptForm> irbExemptFormList) {
		this.irbExemptFormList = irbExemptFormList;
	}

	public ArrayList<HashMap<String, Object>> getIrbProtocolRoleMap() {
		return irbProtocolRoleMap;
	}

	public void setIrbProtocolRoleMap(ArrayList<HashMap<String, Object>> irbProtocolRoleMap) {
		this.irbProtocolRoleMap = irbProtocolRoleMap;
	}

	public ArrayList<HashMap<String, Object>> getIrbProtocolFundingSourceMap() {
		return irbProtocolFundingSourceMap;
	}

	public void setIrbProtocolFundingSourceMap(ArrayList<HashMap<String, Object>> irbProtocolFundingSourceMap) {
		this.irbProtocolFundingSourceMap = irbProtocolFundingSourceMap;
	}

	public ArrayList<HashMap<String, Object>> getIrbProtocolSubjectMap() {
		return irbProtocolSubjectMap;
	}

	public void setIrbProtocolSubjectMap(ArrayList<HashMap<String, Object>> irbProtocolSubjectMap) {
		this.irbProtocolSubjectMap = irbProtocolSubjectMap;
	}

	public ArrayList<HashMap<String, Object>> getIrbProtocolLeadUnitMap() {
		return irbProtocolLeadUnitMap;
	}

	public void setIrbProtocolLeadUnitMap(ArrayList<HashMap<String, Object>> irbProtocolLeadUnitMap) {
		this.irbProtocolLeadUnitMap = irbProtocolLeadUnitMap;
	}

	public ArrayList<HashMap<String, Object>> getIrbProtocolAffiliateMap() {
		return irbProtocolAffiliateMap;
	}

	public void setIrbProtocolAffiliateMap(ArrayList<HashMap<String, Object>> irbProtocolAffiliateMap) {
		this.irbProtocolAffiliateMap = irbProtocolAffiliateMap;
	}

	public ArrayList<HashMap<String, Object>> getIrbProtocolTitleMap() {
		return irbProtocolTitleMap;
	}

	public void setIrbProtocolTitleMap(ArrayList<HashMap<String, Object>> irbProtocolTitleMap) {
		this.irbProtocolTitleMap = irbProtocolTitleMap;
	}

	public ArrayList<HashMap<String, Object>> getIrbProtocolHistoryActionComments() {
		return irbProtocolHistoryActionComments;
	}

	public void setIrbProtocolHistoryActionComments(ArrayList<HashMap<String, Object>> irbProtocolHistoryActionComments) {
		this.irbProtocolHistoryActionComments = irbProtocolHistoryActionComments;
	}

	public Integer getUserHasRightToViewProtocol() {
		return userHasRightToViewProtocol;
	}

	public void setUserHasRightToViewProtocol(Integer userHasRightToViewProtocol) {
		this.userHasRightToViewProtocol = userHasRightToViewProtocol;
	}

	public String getTrainingStatus() {
		return trainingStatus;
	}

	public void setTrainingStatus(String trainingStatus) {
		this.trainingStatus = trainingStatus;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolUnits() {
		return irbViewProtocolUnits;
	}

	public void setIrbViewProtocolUnits(ArrayList<HashMap<String, Object>> irbViewProtocolUnits) {
		this.irbViewProtocolUnits = irbViewProtocolUnits;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolAdminContact() {
		return irbViewProtocolAdminContact;
	}

	public void setIrbViewProtocolAdminContact(ArrayList<HashMap<String, Object>> irbViewProtocolAdminContact) {
		this.irbViewProtocolAdminContact = irbViewProtocolAdminContact;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolCollaboratorPersons() {
		return irbViewProtocolCollaboratorPersons;
	}

	public void setIrbViewProtocolCollaboratorPersons(
			ArrayList<HashMap<String, Object>> irbViewProtocolCollaboratorPersons) {
		this.irbViewProtocolCollaboratorPersons = irbViewProtocolCollaboratorPersons;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolCollaboratorAttachments() {
		return irbViewProtocolCollaboratorAttachments;
	}

	public void setIrbViewProtocolCollaboratorAttachments(
			ArrayList<HashMap<String, Object>> irbViewProtocolCollaboratorAttachments) {
		this.irbViewProtocolCollaboratorAttachments = irbViewProtocolCollaboratorAttachments;
	}

	public Integer getUserHasRightToEditTraining() {
		return userHasRightToEditTraining;
	}

	public void setUserHasRightToEditTraining(Integer userHasRightToEditTraining) {
		this.userHasRightToEditTraining = userHasRightToEditTraining;
	}

	public List<CollaboratorNames> getCollaboratorList() {
		return collaboratorList;
	}

	public void setCollaboratorList(List<CollaboratorNames> collaboratorList) {
		this.collaboratorList = collaboratorList;
	}

	public ArrayList<HashMap<String, Object>> getIrbProtocolHistoryGroupComments() {
		return irbProtocolHistoryGroupComments;
	}

	public void setIrbProtocolHistoryGroupComments(ArrayList<HashMap<String, Object>> irbProtocolHistoryGroupComments) {
		this.irbProtocolHistoryGroupComments = irbProtocolHistoryGroupComments;
	}	
}
