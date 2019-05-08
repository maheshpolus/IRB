package org.mit.irb.web.IRBProtocol.service.Impl;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.dao.IRBActionsDao;
import org.mit.irb.web.IRBProtocol.service.IRBActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "irbActionsService")
public class IRBActionsServImpl implements IRBActionsService {

	@Autowired
	IRBActionsDao irbActionsDao;
	
	protected static Logger logger = Logger.getLogger(IRBActionsServImpl.class.getName());

	@Override
	public IRBActionsVO getPersonRight(IRBActionsVO vo) {
		 vo=irbActionsDao.getPersonRight(vo);
		return vo;
	}

	@Override
	public IRBActionsVO performProtocolActions(IRBActionsVO vo) {
		switch (vo.getActionTypeCode()) {
		case "101":
			vo = irbActionsDao.submitForReviewProtocolActions(vo);
			break;
		case "303":
		   vo = irbActionsDao.withdrawProtocolActions(vo);
			break;
		case "119":
			vo = irbActionsDao.adandonProtocolActions(vo);
			break;
		case "103":
			vo = irbActionsDao.createAmendmentProtocolActions(vo);
			break;
		case "102":
			vo = irbActionsDao.createRenewalProtocolActions(vo);
			break;
		case "1":
			vo = irbActionsDao.deleteProtocolAmendmentRenewalProtocolActions(vo);
			break;
		case "116":
			vo = irbActionsDao.notifyIRBProtocolActions(vo);
			break;
		case "211":
			vo = irbActionsDao.requestForDataAnalysisProtocolActions(vo);
			break;
		case "106":
			vo = irbActionsDao.requestForSuspensionProtocolActions(vo);
			break;
		case "104":
			vo = irbActionsDao.requestForTerminationProtocolActions(vo);
			break;
		case "105":
			vo = irbActionsDao.requestForCloseProtocolActions(vo);
			break;
		case "108":
			vo = irbActionsDao.requestForCloseEnrollmentProtocolActions(vo);
			break;
		case "115":
			vo = irbActionsDao.requestForReopenEnrollmentProtocolActions(vo);
			break;
		case "2":
			vo = irbActionsDao.modifyAmendmentSectionProtocolActions(vo);
		}
		return vo;
	}
}
