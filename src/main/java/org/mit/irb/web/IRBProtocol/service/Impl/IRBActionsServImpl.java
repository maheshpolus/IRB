package org.mit.irb.web.IRBProtocol.service.Impl;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.dao.IRBActionsDao;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolActionAttachments;
import org.mit.irb.web.IRBProtocol.service.IRBActionsService;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		case "1":
			vo = irbActionsDao.deleteProtocolAmendmentRenewalProtocolActions(vo);//11  995-deleteproto, 120,121
			break;
		case "116":			
		//	vo = irbActionsDao.notifyIRBProtocolActions(vo,attachmentobj);//4
			break;
		case "114":
			vo = irbActionsDao.requestForDataAnalysisProtocolActions(vo);//8
			break;
		case "105":
			vo = irbActionsDao.requestForCloseProtocolActions(vo);//9
			break;
		case "108":
			vo = irbActionsDao.requestForCloseEnrollmentProtocolActions(vo);//6
			break;
		case "115":
			vo = irbActionsDao.requestForReopenEnrollmentProtocolActions(vo);//7
			break;
		case "911":
			vo = irbActionsDao.copyProtocolActions(vo);//12 911-copy  910 modify amendmnt
			break;
		}
		return vo;
	}
}
