package org.mit.irb.web.login.dao;

import java.util.ArrayList;
import java.util.HashMap;

public interface LoginDao {

	public ArrayList<HashMap<String, Object>> checkIRBUserRole(String personId);
}
