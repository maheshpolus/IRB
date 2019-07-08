package org.mit.irb.web.correspondence.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.apache.log4j.Logger;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.correspondence.dao.CorrespondenceDao;
import org.mit.irb.web.dbengine.DBEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;

@Service(value = "correspondenceService")
public class CorrespondenceServiceImpl implements CorrespondenceService{
	
	@Autowired 
	private CorrespondenceDao correspondenceDao;

	protected static Logger logger = Logger.getLogger(CorrespondenceServiceImpl.class.getName());
	private DBEngine dbEngine;
	
	public CorrespondenceServiceImpl() {
		dbEngine = new DBEngine();
	}

	@Override
	public ResponseEntity<byte[]> generateCorrespondence(HttpServletResponse response, CommonVO commonVO) {
		ResponseEntity<byte[]> attachmentData = null;
		try{
			byte[] data = correspondenceDao.getTemplateData(commonVO);
			byte[] mergedOutput = correspondenceDao.mergePlaceHolders(data,commonVO);
			String generatedFileName = "Result"+System.nanoTime()+".pdf";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			headers.setContentDispositionFormData(generatedFileName, generatedFileName);
			headers.setContentLength(mergedOutput.length);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setPragma("public");
			attachmentData = new ResponseEntity<byte[]>(mergedOutput, headers, HttpStatus.OK);				
		}catch (Exception e) {
			logger.error("Exception in generateCorrespondence"+ e.getMessage());
		}
		return attachmentData;
	}

}
