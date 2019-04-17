package org.mit.irb.web.IRBProtocol.service.Impl;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.service.IRBUtilService;
import org.springframework.stereotype.Service;

@Service(value = "irbUtilService")
public class IRBUtilServiceImpl implements IRBUtilService {
	protected static Logger logger = Logger.getLogger(IRBUtilServiceImpl.class.getName());
	
}
