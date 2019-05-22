package org.mit.irb.web.IRBProtocol.service.Impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.dao.IRBActionsDao;
import org.mit.irb.web.IRBProtocol.service.IRBActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service(value = "irbActionsService")
public class IRBActionsServImpl implements IRBActionsService {

	@Autowired
	IRBActionsDao irbActionsDao;
	
	protected static Logger logger = Logger.getLogger(IRBActionsServImpl.class.getName());

	@Override
	public IRBActionsVO getPersonRight(IRBActionsVO vo) {
		 vo = irbActionsDao.getPersonRight(vo);
		return vo;
	}

	@Override
	public IRBActionsVO performProtocolActions(IRBActionsVO vo,MultipartFile[] files) {		
		switch (vo.getActionTypeCode()) {
		case "101":			
			vo = irbActionsDao.submitForReviewProtocolActions(vo);
			break;
		case "303":
		   vo = irbActionsDao.withdrawProtocolActions(vo);
			break;
		case "103":
			vo = irbActionsDao.createAmendmentProtocolActions(vo);
			break;
		case "102":
			vo = irbActionsDao.createRenewalProtocolActions(vo);
			break;
		case "992":
			if(vo.getProtocolNumber().contains("A")){
				vo.setActionTypeCode("120");
			}else if(vo.getProtocolNumber().contains("R")){
				vo.setActionTypeCode("121");
			}else{
				vo.setActionTypeCode("124");
			}
			vo = irbActionsDao.deleteProtocolAmendmentRenewalProtocolActions(vo);
			break;			
		case "116":			
			vo = irbActionsDao.notifyIRBProtocolActions(vo,files);
			break;
		case "114":
			vo = irbActionsDao.requestForDataAnalysisProtocolActions(vo,files);
			break;
		case "105":
			vo = irbActionsDao.requestForCloseProtocolActions(vo,files);
			break;
		case "108":
			vo = irbActionsDao.requestForCloseEnrollmentProtocolActions(vo,files);
			break;
		case "115":
			vo = irbActionsDao.requestForReopenEnrollmentProtocolActions(vo,files);
			break;
		case "911":
			vo = irbActionsDao.copyProtocolActions(vo);
			break;
		case "213":		
			vo=generateSqlActionDate(vo);
			vo = irbActionsDao.returnToPiAdminActions(vo);
			break;
		case "300":
			vo=generateSqlActionDate(vo);
			vo = irbActionsDao.closeAdminActions(vo);
			break;
		case "304":
			vo=generateSqlActionDate(vo);
			vo = irbActionsDao.disapproveAdminActions(vo);
			break;
		case "209":
			vo=generateSqlActionDate(vo);
			vo = irbActionsDao.irbAcknowledgementAdminActions(vo);
			break;
		case "212":
			vo=generateSqlActionDate(vo);
			vo = irbActionsDao.reOpenEnrollmentAdminActions(vo,files);
			break;
		case "211":
			vo=generateSqlActionDate(vo);
			vo = irbActionsDao.dataAnalysisOnlyAdminActions(vo);
			break;
		case "207":
			vo=generateSqlActionDate(vo);
			vo = irbActionsDao.closedForEnrollmentAdminActions(vo);
			break;
		case "301":
			vo=generateSqlActionDate(vo);
			vo = irbActionsDao.terminateAdminActions(vo);
			break;	
		case "302":
			vo=generateSqlActionDate(vo);
			vo = irbActionsDao.suspendAdminActions(vo);
			break;	
		case "109":
			vo=generateSqlActionDate(vo);
			vo = irbActionsDao.notifyCommiteeAdminActions(vo);
			break;
		case "201":
			vo=generateSqlActionDate(vo);
			vo = irbActionsDao.deferAdminActions(vo);
			break;
		}
		return vo;
	}

	@Override
	public IRBActionsVO getAmendRenwalSummary(IRBActionsVO vo) {
		IRBActionsVO irbActionsVO = new IRBActionsVO();
		try{
			ArrayList<HashMap<String, Object>> renewalModules = null;
			renewalModules = irbActionsDao.iterateAmendRenewalModule(vo,renewalModules);	
			irbActionsVO.setModuleAvailableForAmendment(renewalModules);
			irbActionsVO.setComment(irbActionsDao.getAmendRenewalSummary(vo));
			irbActionsVO.setProtocolStatus(vo.getPrevProtocolStatusCode());
			irbActionsVO.setSuccessCode(true);
		} catch (Exception e) {
			irbActionsVO.setSuccessCode(false);
			irbActionsVO.setSuccessMessage("Get AmendRenwalSummary Failed");
			logger.info("Exception in getAmendRenwalSummary:" + e);
		}
		return irbActionsVO;
	}

	@Override
	public IRBActionsVO updateAmendRenwalSummary(IRBActionsVO vo) {
		IRBActionsVO irbActionsVO = new IRBActionsVO();
		try{
 			irbActionsDao.updateAmendRenewModule(vo);
			irbActionsVO = getAmendRenwalSummary(vo);
			irbActionsVO.setSuccessCode(true);
			irbActionsVO.setSuccessMessage("Updated Amendment Renewal Summary");
		} catch (Exception e) {
			irbActionsVO.setSuccessCode(false);
			irbActionsVO.setSuccessMessage("Get AmendRenwalSummary Failed");
			logger.info("Exception in getAmendRenwalSummary:" + e);
		}
		return irbActionsVO;
	}
	
	public IRBActionsVO generateSqlActionDate(IRBActionsVO vo){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date utilActionDate = null;
		java.sql.Date sqlActionDate= null;
		if (vo.getActionDate()!= null) {
			try {
				utilActionDate = sdf.parse(vo.getActionDate());
			} catch (ParseException e) {				
				e.printStackTrace();
			}
			sqlActionDate = new java.sql.Date(utilActionDate.getTime());
			vo.setSqlActionDate(sqlActionDate);			
		}
		return vo;
	}

	@Override
	public IRBActionsVO getActionLookup(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}
}
