package org.mit.irb.web.correspondence.service;

import javax.servlet.http.HttpServletResponse;

import org.mit.irb.web.common.VO.CommonVO;
import org.springframework.http.ResponseEntity;


public interface CorrespondenceService {

	ResponseEntity<byte[]> generateCorrespondence(HttpServletResponse response, CommonVO commonVO);
}
