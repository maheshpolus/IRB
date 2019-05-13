package org.mit.irb.web.IRBProtocol.service.Impl;

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
			vo = irbActionsDao.submitForReviewProtocolActions(vo);//1,10
			break;
		case "303":
		   vo = irbActionsDao.withdrawProtocolActions(vo);//5
			break;
		case "103":
			vo = irbActionsDao.createAmendmentProtocolActions(vo);//2
			break;
		case "102":
			vo = irbActionsDao.createRenewalProtocolActions(vo);//3
			break;
		case "992":
			if(vo.getProtocolNumber().contains("A")){
				vo.setActionTypeCode("120");
			}else if(vo.getProtocolNumber().contains("R")){
				vo.setActionTypeCode("121");
			}else{
				vo.setActionTypeCode("124");
			}
			vo = irbActionsDao.deleteProtocolAmendmentRenewalProtocolActions(vo);//11  deleteproto, 120,121
			break;			
		case "116":			
			vo = irbActionsDao.notifyIRBProtocolActions(vo,files);//4
			break;
		case "114":
			vo = irbActionsDao.requestForDataAnalysisProtocolActions(vo,files);//8
			break;
		case "105":
			vo = irbActionsDao.requestForCloseProtocolActions(vo,files);//9
			break;
		case "108":
			vo = irbActionsDao.requestForCloseEnrollmentProtocolActions(vo,files);//6
			break;
		case "115":
			vo = irbActionsDao.requestForReopenEnrollmentProtocolActions(vo,files);//7
			break;
		case "911":
			vo = irbActionsDao.copyProtocolActions(vo);//12 911-copy  910 modify amendmnt
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
}
