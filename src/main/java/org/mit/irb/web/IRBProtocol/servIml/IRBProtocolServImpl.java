package org.mit.irb.web.IRBProtocol.servIml;

import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="irbProtocolService")
public class IRBProtocolServImpl implements IRBProtocolService{
	
	@Autowired
	IRBProtocolDao irbProtocolDao;
	
	@Override
	public IRBViewProfile getIRBProtocolDetails(String protocolNumber) {
		IRBViewProfile irbViewProfile =irbProtocolDao.getIRBProtocolDetails(protocolNumber);
		return irbViewProfile;
	}

}
