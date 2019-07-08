package org.mit.irb.web.common.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * Singleton class to manage db connections.Singleton design pattern is followed.
 * @author anumole
 *
 */
public class DBConnectionManager {
	
	protected static Logger logger = Logger.getLogger(DBConnectionManager.class.getName());
	
	private static DataSource ds;
	static private DBConnectionManager instance;// The single instance
	static private int clients;
	
	/**
	 * private constructor since its a singleton class
	 * retrieves JNDI from properties and context is created to datasource
	 */
	private DBConnectionManager(){
		try {
			ds = (DataSource) new InitialContext().lookup("java:comp/env/"+IRBProperties.getProperty("DS_JNDI_NAME"));
		} catch(Exception e) {
			logger.debug("Could not get Data Source through JNDI");
		}
	}
	
	/**
	 * Returns the single instance, creating one if it's the first time this method is called.
	 * clients variable is incremented to manage the instance/connection pool
	 * @return DBConnectionManager The single instance.	 * 
	 */
	static synchronized public DBConnectionManager getInstance(){
		if (instance == null) {
			instance = new DBConnectionManager();
		}
		clients++;
	return instance;	
	}
	
	/**
	 * Returns an open connection. If no one is available, and the max
	 * number of connections has not been reached, a new connection is
	 * created.
	 *
	 * @param name The pool name as defined in the properties file
	 * @return Connection The connection or null
	 * @throws SQLException
	 * @throws IOException
	 */
	public Connection getConnection() throws SQLException, IOException{

		Connection con = null ;
		try{
			con = ds.getConnection();
			if(ds==null) throw new SQLException("Datasource is null\n" +
					"Make sure that the DS_JNDI_NAME specified in the ospgo.properties file is correct\n");
		}catch(SQLException ex){
			logger.debug("Could not get Connection from DataSource");
			throw ex;
		}
		return con;        

	}
	
	/**
	 * Returns a connection to the named pool.
	 * @param name The pool name as defined in the properties file
	 * @param con
	 * @throws SQLException
	 */
	public void freeConnection(String name, Connection con) throws SQLException{

		con.close();

	}
}