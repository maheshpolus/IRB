package org.mit.irb.web.IRBProtocol.dao;

import org.mit.irb.web.common.pojo.IRBViewProfile;

public interface IRBProtocolDao {

	IRBViewProfile getIRBProtocolDetails(String protocolNumber);

}
