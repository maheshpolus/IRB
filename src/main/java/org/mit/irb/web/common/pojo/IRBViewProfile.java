package org.mit.irb.web.common.pojo;

/**
 * Class with all details about IRB protocol
 * 
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	private ArrayList<HashMap<String, Object>> irbViewProtocolHistoryGroupDetails;	
	private List<IRBExemptForm> irbExemptFormList;
	private ArrayList<HashMap<String, Object>> irbProtocolRoleMap;
	private ArrayList<HashMap<String, Object>> irbProtocolFundingSourceMap;
	private ArrayList<HashMap<String, Object>> irbProtocolSubjectMap;		
	private ArrayList<HashMap<String, Object>> irbProtocolLeadUnitMap;
	private ArrayList<HashMap<String, Object>> irbProtocolAffiliateMap;	
	private ArrayList<HashMap<String, Object>> irbProtocolTitleMap;		
	
	public ArrayList<HashMap<String, Object>> getIrbViewProtocolHistoryGroupList() {
		return irbViewProtocolHistoryGroupList;
	}

	public void setIrbViewProtocolHistoryGroupList(ArrayList<HashMap<String, Object>> irbviewProtocolHistoryGroupList) {
		this.irbViewProtocolHistoryGroupList = irbviewProtocolHistoryGroupList;
	}

	public ArrayList<HashMap<String, Object>> getIrbViewProtocolHistoryGroupDetails() {
		return irbViewProtocolHistoryGroupDetails;
	}

	public void setIrbViewProtocolHistoryGroupDetails(
			ArrayList<HashMap<String, Object>> irbViewProtocolHistoryGroupDetails) {
		this.irbViewProtocolHistoryGroupDetails = irbViewProtocolHistoryGroupDetails;
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
	
}
