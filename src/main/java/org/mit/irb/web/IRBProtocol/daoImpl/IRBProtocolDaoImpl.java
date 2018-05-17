package org.mit.irb.web.IRBProtocol.daoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.common.pojo.DashboardProfile;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="iRBProtocolDao")
@Transactional
public class IRBProtocolDaoImpl implements IRBProtocolDao{

	DBEngine dbEngine;
	
	IRBProtocolDaoImpl(){
		dbEngine = new DBEngine();
	}
	
	@Override
	public IRBViewProfile getIRBProtocolDetails(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_DETAILS", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbviewHeaderMap(result);
		}
		System.out.println("irbview : "+ result);
		return irbViewProfile;
	}

}
