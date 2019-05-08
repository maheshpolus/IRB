package org.mit.irb.web.IRBProtocol.service;

import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;

public interface IRBActionsService {

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO getPersonRight(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO performProtocolActions(IRBActionsVO vo);
}
