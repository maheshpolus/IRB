package org.mit.irb.web.correspondence.dao;

import org.mit.irb.web.common.VO.CommonVO;

public interface CorrespondenceDao{

	/**
	 * @param commonVO
	 * @return get Template for reporting
	 */
	byte[] getTemplateData(CommonVO commonVO);

	/**
	 * @param data
	 * @param commonVO
	 * @return get merged
	 * @throws Exception 
	 */
	byte[] mergePlaceHolders(byte[] data, CommonVO commonVO);

}
