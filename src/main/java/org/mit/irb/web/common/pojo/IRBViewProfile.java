package org.mit.irb.web.common.pojo;

import java.util.ArrayList;
import java.util.HashMap;

public class IRBViewProfile {
	private HashMap<String, Object> irbviewHeader;
	private ArrayList<HashMap<String, Object>> irbviewProtocolPersons;
	private ArrayList<HashMap<String, Object>> irbviewProtocolFundingsource;
	private ArrayList<HashMap<String, Object>> irbviewProtocolLocation;
	private ArrayList<HashMap<String, Object>> irbviewProtocolVulnerableSubject;
	private ArrayList<HashMap<String, Object>> irbviewProtocolSpecialReview;
	private HashMap<String, Object> irbviewProtocolMITKCPersonInfo;
	private ArrayList<HashMap<String, Object>> irbviewProtocolMITKCPersonTrainingInfo;
	private ArrayList<HashMap<String, Object>> irbviewProtocolAttachmentList;
	private ArrayList<HashMap<String, Object>> irbviewProtocolHistoryGroupList;
	private ArrayList<HashMap<String, Object>> irbviewProtocolHistoryGroupDetails;
	
	public ArrayList<HashMap<String, Object>> getIrbviewProtocolHistoryGroupList() {
		return irbviewProtocolHistoryGroupList;
	}

	public void setIrbviewProtocolHistoryGroupList(ArrayList<HashMap<String, Object>> irbviewProtocolHistoryGroupList) {
		this.irbviewProtocolHistoryGroupList = irbviewProtocolHistoryGroupList;
	}

	public ArrayList<HashMap<String, Object>> getIrbviewProtocolHistoryGroupDetails() {
		return irbviewProtocolHistoryGroupDetails;
	}

	public void setIrbviewProtocolHistoryGroupDetails(
			ArrayList<HashMap<String, Object>> irbviewProtocolHistoryGroupDetails) {
		this.irbviewProtocolHistoryGroupDetails = irbviewProtocolHistoryGroupDetails;
	}

	public HashMap<String, Object> getIrbviewHeader() {
		return irbviewHeader;
	}

	public void setIrbviewHeader(HashMap<String, Object> irbviewHeader) {
		this.irbviewHeader = irbviewHeader;
	}

	public ArrayList<HashMap<String, Object>> getIrbviewProtocolPersons() {
		return irbviewProtocolPersons;
	}

	public void setIrbviewProtocolPersons(ArrayList<HashMap<String, Object>> irbviewProtocolPersons) {
		this.irbviewProtocolPersons = irbviewProtocolPersons;
	}

	public ArrayList<HashMap<String, Object>> getIrbviewProtocolFundingsource() {
		return irbviewProtocolFundingsource;
	}

	public void setIrbviewProtocolFundingsource(ArrayList<HashMap<String, Object>> irbviewProtocolFundingsource) {
		this.irbviewProtocolFundingsource = irbviewProtocolFundingsource;
	}

	public ArrayList<HashMap<String, Object>> getIrbviewProtocolLocation() {
		return irbviewProtocolLocation;
	}

	public void setIrbviewProtocolLocation(ArrayList<HashMap<String, Object>> irbviewProtocolLocation) {
		this.irbviewProtocolLocation = irbviewProtocolLocation;
	}

	public ArrayList<HashMap<String, Object>> getIrbviewProtocolVulnerableSubject() {
		return irbviewProtocolVulnerableSubject;
	}

	public void setIrbviewProtocolVulnerableSubject(
			ArrayList<HashMap<String, Object>> irbviewProtocolVulnerableSubject) {
		this.irbviewProtocolVulnerableSubject = irbviewProtocolVulnerableSubject;
	}

	public ArrayList<HashMap<String, Object>> getIrbviewProtocolSpecialReview() {
		return irbviewProtocolSpecialReview;
	}

	public void setIrbviewProtocolSpecialReview(ArrayList<HashMap<String, Object>> irbviewProtocolSpecialReview) {
		this.irbviewProtocolSpecialReview = irbviewProtocolSpecialReview;
	}

	public HashMap<String, Object> getIrbviewProtocolMITKCPersonInfo() {
		return irbviewProtocolMITKCPersonInfo;
	}

	public void setIrbviewProtocolMITKCPersonInfo(HashMap<String,Object> hashMap) {
		this.irbviewProtocolMITKCPersonInfo = hashMap;
	}

	public ArrayList<HashMap<String, Object>> getIrbviewProtocolMITKCPersonTrainingInfo() {
		return irbviewProtocolMITKCPersonTrainingInfo;
	}

	public void setIrbviewProtocolMITKCPersonTrainingInfo(
			ArrayList<HashMap<String, Object>> irbviewProtocolMITKCPersonTrainingInfo) {
		this.irbviewProtocolMITKCPersonTrainingInfo = irbviewProtocolMITKCPersonTrainingInfo;
	}

	public ArrayList<HashMap<String, Object>> getIrbviewProtocolAttachmentList() {
		return irbviewProtocolAttachmentList;
	}

	public void setIrbviewProtocolAttachmentList(ArrayList<HashMap<String, Object>> irbviewProtocolAttachmentList) {
		this.irbviewProtocolAttachmentList = irbviewProtocolAttachmentList;
	}
}
