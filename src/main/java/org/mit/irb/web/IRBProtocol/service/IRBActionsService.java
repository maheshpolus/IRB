package org.mit.irb.web.IRBProtocol.service;

import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.springframework.web.multipart.MultipartFile;

public interface IRBActionsService {

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO getPersonRight(IRBActionsVO vo);

	/**
	 * @param vo
	 * @param files 
	 * @return
	 */
	IRBActionsVO performProtocolActions(IRBActionsVO vo, MultipartFile[] files);
}
