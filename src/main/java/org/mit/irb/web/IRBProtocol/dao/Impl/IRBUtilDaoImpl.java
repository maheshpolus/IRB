package org.mit.irb.web.IRBProtocol.dao.Impl;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.dao.IRBUtilDao;
import org.mit.irb.web.common.utils.DBEngine;
import org.springframework.stereotype.Service;

@Service(value = "irbUtilDao")
public class IRBUtilDaoImpl implements IRBUtilDao{
	DBEngine dbEngine;
	
	IRBUtilDaoImpl() {
		dbEngine = new DBEngine();
	}

	Logger logger = Logger.getLogger(IRBUtilDaoImpl.class.getName());
	
}
