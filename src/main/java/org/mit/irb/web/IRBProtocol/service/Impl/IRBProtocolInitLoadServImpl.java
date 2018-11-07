package org.mit.irb.web.IRBProtocol.service.Impl;

import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolInitLoadDao;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "irbInitProtocolDao")
@Transactional
public class IRBProtocolInitLoadServImpl implements IRBProtocolInitLoadService{

	@Autowired
	IRBProtocolInitLoadDao irbProtocolInitLoadDao;
	
	@Override
	public IRBProtocolVO loadProtocolTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolInitLoadDao.loadProtocolTypes(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadRoleTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolInitLoadDao.loadRoleTypes(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolPersonLeadunits(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolInitLoadDao.loadProtocolPersonLeadunits(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolAffiliationTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolInitLoadDao.loadProtocolAffiliationTypes(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolSubjectTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolInitLoadDao.loadProtocolSubjectTypes(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolFundingSourceTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolInitLoadDao.loadProtocolFundingSourceTypes(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolCollaboratorNames(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolInitLoadDao.loadProtocolCollaboratorNames(irbProtocolVO);
		return irbProtocolVO;
	}
	@Override
	public IRBProtocolVO loadAttachmentType() {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolInitLoadDao.loadAttachmentType();
		return irbProtocolVO;
	}
	
	@Override
	public IRBProtocolVO loadSponsorTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolInitLoadDao.loadSponsorTypes(irbProtocolVO);
		return irbProtocolVO;
	}
	
	@Override
	public IRBProtocolVO loadProtocolAgeGroups(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolInitLoadDao.loadProtocolAgeGroups(irbProtocolVO);
		return irbProtocolVO;
	}
}
