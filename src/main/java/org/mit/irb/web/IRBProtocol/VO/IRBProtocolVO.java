package org.mit.irb.web.IRBProtocol.VO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.pojo.AgeGroups;
import org.mit.irb.web.IRBProtocol.pojo.CollaboratorNames;
import org.mit.irb.web.IRBProtocol.pojo.ExemptFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.IRBAttachementTypes;
import org.mit.irb.web.IRBProtocol.pojo.IRBAttachmentProtocol;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAffiliationTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaboratorAttachments;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaboratorPersons;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSourceTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonRoleTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonnelInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubject;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubjectTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolType;
import org.mit.irb.web.IRBProtocol.pojo.ScienceOfProtocol;
import org.mit.irb.web.IRBProtocol.pojo.SponsorType;
import org.mit.irb.web.committee.pojo.Unit;

/*VO Class to  fetch all the IRB Protocol information
 * @author anu
 */
public class IRBProtocolVO {
	private String protocolNumber;
	private Integer protocolId;
	private ProtocolGeneralInfo generalInfo;
	private List<ProtocolType> protocolType;
	private List<SponsorType> sponsorType;
	private List<ProtocolPersonRoleTypes> personRoleTypes;
	private List<ProtocolPersonLeadUnits> protocolPersonLeadUnits;
	private List<ProtocolAffiliationTypes> affiliationTypes;
	private List<ProtocolSubjectTypes> protocolSubjectTypes;
	private List<ProtocolFundingSourceTypes> protocolFundingSourceTypes;
	private List<IRBAttachementTypes> irbAttachementTypes;
	private List<CollaboratorNames> collaboratorNames;
	private ProtocolPersonnelInfo personnelInfo;
	private ProtocolLeadUnits protocolLeadUnits;
	private ProtocolFundingSource fundingSource;
	private ProtocolSubject protocolSubject;
	private List<ProtocolPersonnelInfo> ProtocolPersonnelInfoList;
	private List<ProtocolLeadUnits> ProtocolLeadUnitsList;
	private List<ProtocolFundingSource> protocolFundingSourceList;
	private List<ProtocolSubject> ProtocolSubjectList;
	private List<ProtocolCollaborator> protocolCollaboratorList;
	private ProtocolCollaborator protocolCollaborator;
	private List<IRBAttachmentProtocol> protocolAttachmentList;
	private ScienceOfProtocol scienceOfProtocol;
	private List<AgeGroups> ageGroups;
	private List<ProtocolCollaboratorAttachments> protocolCollaboratorAttachmentsList;
	private List<ProtocolCollaboratorPersons> protocolCollaboratorPersons;
	private Integer collaboratorId;
	private String searchString;
	private List<SponsorSearchResult> sponsorSearchResult;
	private List<HashMap<String, Object>> committeList;
	private List<HashMap<String, Object>> committeSchedulList;
	
	private ExemptFundingSource exemptFundingSource;
	private List<HashMap<String, Object>> exemptFundingSourceList;
	private List<Unit> homeUnits;
	private String updateUser;
	
	public ExemptFundingSource getExemptFundingSource() {
		return exemptFundingSource;
	}

	public void setExemptFundingSource(ExemptFundingSource exemptFundingSource) {
		this.exemptFundingSource = exemptFundingSource;
	}

	public List<HashMap<String, Object>> getExemptFundingSourceList() {
		return exemptFundingSourceList;
	}

	public void setExemptFundingSourceList(List<HashMap<String, Object>> exemptFundingSourceList) {
		this.exemptFundingSourceList = exemptFundingSourceList;
	}

	public List<Unit> getHomeUnits() {
		return homeUnits;
	}

	public void setHomeUnits(List<Unit> homeUnits) {
		this.homeUnits = homeUnits;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public ScienceOfProtocol getScienceOfProtocol() {
		return scienceOfProtocol;
	}

	public void setScienceOfProtocol(ScienceOfProtocol scienceOfProtocol) {
		this.scienceOfProtocol = scienceOfProtocol;
	}

	public List<AgeGroups> getAgeGroups() {
		return ageGroups;
	}

	public void setAgeGroups(List<AgeGroups> ageGroups) {
		this.ageGroups = ageGroups;
	}
	public ProtocolGeneralInfo getGeneralInfo() {
		return generalInfo;
	}

	public void setGeneralInfo(ProtocolGeneralInfo generalInfo) {
		this.generalInfo = generalInfo;
	}

	public List<ProtocolType> getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(List<ProtocolType> protocolType) {
		this.protocolType = protocolType;
	}

	public List<ProtocolPersonRoleTypes> getPersonRoleTypes() {
		return personRoleTypes;
	}

	public void setPersonRoleTypes(List<ProtocolPersonRoleTypes> personRoleTypes) {
		this.personRoleTypes = personRoleTypes;
	}

	public List<ProtocolPersonLeadUnits> getProtocolPersonLeadUnits() {
		return protocolPersonLeadUnits;
	}

	public void setProtocolPersonLeadUnits(List<ProtocolPersonLeadUnits> protocolPersonLeadUnits) {
		this.protocolPersonLeadUnits = protocolPersonLeadUnits;
	}

	public List<ProtocolAffiliationTypes> getAffiliationTypes() {
		return affiliationTypes;
	}

	public void setAffiliationTypes(List<ProtocolAffiliationTypes> affiliationTypes) {
		this.affiliationTypes = affiliationTypes;
	}

	public List<ProtocolSubjectTypes> getProtocolSubjectTypes() {
		return protocolSubjectTypes;
	}

	public void setProtocolSubjectTypes(List<ProtocolSubjectTypes> protocolSubjectTypes) {
		this.protocolSubjectTypes = protocolSubjectTypes;
	}

	public List<ProtocolFundingSourceTypes> getProtocolFundingSourceTypes() {
		return protocolFundingSourceTypes;
	}

	public void setProtocolFundingSourceTypes(List<ProtocolFundingSourceTypes> protocolFundingSourceTypes) {
		this.protocolFundingSourceTypes = protocolFundingSourceTypes;
	}

	public List<CollaboratorNames> getCollaboratorNames() {
		return collaboratorNames;
	}

	public void setCollaboratorNames(List<CollaboratorNames> collaboratorNames) {
		this.collaboratorNames = collaboratorNames;
	}

	public ProtocolPersonnelInfo getPersonnelInfo() {
		return personnelInfo;
	}

	public void setPersonnelInfo(ProtocolPersonnelInfo personnelInfo) {
		this.personnelInfo = personnelInfo;
	}

	public ProtocolLeadUnits getProtocolLeadUnits() {
		return protocolLeadUnits;
	}

	public void setProtocolLeadUnits(ProtocolLeadUnits protocolLeadUnits) {
		this.protocolLeadUnits = protocolLeadUnits;
	}

	public ProtocolFundingSource getFundingSource() {
		return fundingSource;
	}

	public void setFundingSource(ProtocolFundingSource fundingSource) {
		this.fundingSource = fundingSource;
	}

	public ProtocolSubject getProtocolSubject() {
		return protocolSubject;
	}

	public void setProtocolSubject(ProtocolSubject protocolSubject) {
		this.protocolSubject = protocolSubject;
	}

	public List<ProtocolPersonnelInfo> getProtocolPersonnelInfoList() {
		return ProtocolPersonnelInfoList;
	}

	public void setProtocolPersonnelInfoList(List<ProtocolPersonnelInfo> protocolPersonnelInfoList) {
		ProtocolPersonnelInfoList = protocolPersonnelInfoList;
	}

	public List<ProtocolLeadUnits> getProtocolLeadUnitsList() {
		return ProtocolLeadUnitsList;
	}

	public void setProtocolLeadUnitsList(List<ProtocolLeadUnits> protocolLeadUnitsList) {
		ProtocolLeadUnitsList = protocolLeadUnitsList;
	}

	public List<ProtocolFundingSource> getProtocolFundingSourceList() {
		return protocolFundingSourceList;
	}

	public void setProtocolFundingSourceList(List<ProtocolFundingSource> protocolFundingSourceList) {
		this.protocolFundingSourceList = protocolFundingSourceList;
	}

	public List<ProtocolSubject> getProtocolSubjectList() {
		return ProtocolSubjectList;
	}

	public void setProtocolSubjectList(List<ProtocolSubject> protocolSubjectList) {
		ProtocolSubjectList = protocolSubjectList;
	}

	public List<ProtocolCollaborator> getProtocolCollaboratorList() {
		return protocolCollaboratorList;
	}

	public void setProtocolCollaboratorList(List<ProtocolCollaborator> protocolCollaboratorList) {
		this.protocolCollaboratorList = protocolCollaboratorList;
	}

	public ProtocolCollaborator getProtocolCollaborator() {
		return protocolCollaborator;
	}

	public void setProtocolCollaborator(ProtocolCollaborator protocolCollaborator) {
		this.protocolCollaborator = protocolCollaborator;
	}

	public List<IRBAttachementTypes> getIrbAttachementTypes() {
		return irbAttachementTypes;
	}

	public void setIrbAttachementTypes(List<IRBAttachementTypes> irbAttachementTypes) {
		this.irbAttachementTypes = irbAttachementTypes;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public List<IRBAttachmentProtocol> getProtocolAttachmentList() {
		return protocolAttachmentList;
	}

	public void setProtocolAttachmentList(List<IRBAttachmentProtocol> protocolAttachmentList) {
		this.protocolAttachmentList = protocolAttachmentList;
	}

	public List<SponsorType> getSponsorType() {
		return sponsorType;
	}

	public void setSponsorType(List<SponsorType> sponsorType) {
		this.sponsorType = sponsorType;
	}

	public List<ProtocolCollaboratorAttachments> getProtocolCollaboratorAttachmentsList() {
		return protocolCollaboratorAttachmentsList;
	}

	public void setProtocolCollaboratorAttachmentsList(
			List<ProtocolCollaboratorAttachments> protocolCollaboratorAttachmentsList) {
		this.protocolCollaboratorAttachmentsList = protocolCollaboratorAttachmentsList;
	}

	public List<ProtocolCollaboratorPersons> getProtocolCollaboratorPersons() {
		return protocolCollaboratorPersons;
	}

	public void setProtocolCollaboratorPersons(List<ProtocolCollaboratorPersons> protocolCollaboratorPersons) {
		this.protocolCollaboratorPersons = protocolCollaboratorPersons;
	}

	public Integer getCollaboratorId() {
		return collaboratorId;
	}

	public void setCollaboratorId(Integer collaboratorId) {
		this.collaboratorId = collaboratorId;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public List<SponsorSearchResult> getSponsorSearchResult() {
		return sponsorSearchResult;
	}

	public void setSponsorSearchResult(List<SponsorSearchResult> sponsorSearchResult) {
		this.sponsorSearchResult = sponsorSearchResult;
	}

	public List<HashMap<String, Object>> getCommitteSchedulList() {
		return committeSchedulList;
	}

	public void setCommitteSchedulList(ArrayList<HashMap<String, Object>> result) {
		this.committeSchedulList = result;
	}

	public List<HashMap<String, Object>> getCommitteList() {
		return committeList;
	}

	public void setCommitteList(ArrayList<HashMap<String, Object>> result) {
		this.committeList = result;
	}
}
