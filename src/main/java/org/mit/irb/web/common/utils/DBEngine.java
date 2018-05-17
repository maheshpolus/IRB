package org.mit.irb.web.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import oracle.jdbc.OracleTypes;
import oracle.sql.CLOB;

import org.apache.log4j.Logger;
import org.mit.irb.web.common.utils.*;

public class DBEngine {
	
	//protected static Logger logger = Logger.getLogger(DBEngine.class.getName());
	
	DBConnectionManager dbConnectionManager;
	private Hashtable errorList;
	/**
	 * dbConnectionManager instance is created in the DBEngine constructor
	 */
	public DBEngine(){
		dbConnectionManager = DBConnectionManager.getInstance();
	}
	
	private Connection getConnection() throws DBException, IOException{
		Connection conn = null;					
		try {
			conn = dbConnectionManager.getConnection();
		} catch (SQLException sqlEx) {
			throwDBException(sqlEx);
		}		
		return conn;
	}
	private void throwDBException(Exception ex) throws DBException{
		try{
			//            Commented for case#4197
			//            UtilFactory.log(ex.getMessage(),ex,"DBEngineImpl","throwDBException");
			/*
            if the exception is of type SQLException, there are possibly two sources
            One is jdbc error(error code is 0), second is database error(error code >0)
            if the exception is JDBC related, then DBException has errorId of 1,
            User Message is "Driver error".
            if the exception is database related, error id is Oracode and 
            then these are possible scenarios
              1.ORA code <20000, it is internal datbase error, 
                then userMessage is "Internal database error"
              2.ORA code  >20000, then it is application related and 
                user Message is obtained from user message table for the application
			 */
			if (ex instanceof SQLException) {
				int oraCode=((SQLException)ex).getErrorCode();
				//Get the user message from user_error_message table
				if(oraCode ==0){
					throw new DBException(oraCode,"Contact Administrator, Driver error",ex);
				}
				//Added for case#3183 - Proposal Hierarchy - starts
				//To display the error message that is thrown from DB.
				//If the exception code is set as 20000 with some custom message,
				//that message alone will be displayed to the user.
				else if(oraCode == 20000){
					String message = ex.getMessage();
					String code = new Integer(oraCode).toString()+":";
					message = message.substring(message.indexOf(code)+ code.length()); 
					String userMessage= message.substring(0, message.indexOf("ORA")).trim();
					throw new DBException(oraCode,userMessage,ex);
				}
				//Added for case#3183 - Proposal Hierarchy - ends
				else if(oraCode >= -20100 & oraCode < 20100){

					//To set the error code in case Review Comments are modified by another user
					throw new DBException(oraCode, ex.getMessage() ,ex);
				}else if (oraCode >20000){
					String userMessage= errorList==null?ex.getMessage():(String)errorList.get(""+oraCode);
					throw new DBException(oraCode,userMessage,ex);
				}else if(oraCode==2292) {
					throw new DBException(oraCode,"dbEngine_intl_error_child_record_exceptionCode.3333",ex);
				}else{
					throw new DBException(oraCode,"Contact Adminstrator,Internal Database Error",ex);
				}
			}else {
				throw new DBException(0,"Contact Adminstrator,DB Engine Application error",ex);
			}
		}catch(DBException e){
			throw e;
		}catch(Exception e){
			throw new DBException(0,"At DBEngine.throwException.Fatal error,Unknown error \n",e);
		}
	}
	
	public Connection beginTxn() throws DBException, IOException{
		Connection conn = null;
		try{
			conn = getConnection();
			conn.setAutoCommit(false);             
		}catch(SQLException sqlEx){
			throwDBException(sqlEx);
		}
		return conn;
	}
	public void commit(Connection conn) throws DBException{
		try{

			if(conn != null && !conn.isClosed()){
				conn.commit();                
				closeConnection(conn);                
			}
		}catch(SQLException sqlEx){
			throwDBException(sqlEx);
		}
	}
	public void rollback(Connection conn) throws DBException{
		try{
			if(conn != null && !conn.isClosed()){                         
				conn.rollback();                
				closeConnection(conn);
			}
		}catch(SQLException sqlEx){
			throwDBException(sqlEx);
		}
	}
	private void closeConnection(Connection conn) {
		try{
			if(conn != null && !conn.isClosed()){             
				conn.setAutoCommit(true);
				dbConnectionManager.freeConnection("Epaws", conn);
			}
		}catch(SQLException sqlEx){

		}
	} 
	public void endTxn(Connection conn) throws DBException{
		try{
			if(conn != null && !conn.isClosed()){
				conn.commit();                 
				closeConnection(conn);
			}
		}catch(SQLException sqlEx){
			throwDBException(sqlEx);
		}
	}
	public ArrayList<HashMap<String,Object>> executeProcedure(String procedureName,ArrayList<OutParameter> outParam) throws DBException, IOException, SQLException {
		ArrayList<InParameter> inParam = new ArrayList<InParameter>();	
		return executeProcedure(inParam,procedureName,outParam);
	}
	
	public ArrayList<HashMap<String,Object>> executeProcedure(ArrayList<InParameter> inParam,String procedureName) throws DBException, IOException, SQLException {
		ArrayList<OutParameter> outParam = new ArrayList<OutParameter>();	
		return executeProcedure(inParam,procedureName,outParam);
	}
	
	public ArrayList<HashMap<String,Object>> executeProcedure(String procedureName) throws DBException, IOException, SQLException {
		ArrayList<InParameter> inParam = new ArrayList<InParameter>();	
		ArrayList<OutParameter> outParam = new ArrayList<OutParameter>();	
		return executeProcedure(inParam,procedureName,outParam);
	}
	
	public ArrayList<HashMap<String,Object>> executeProcedure(ArrayList<InParameter> inParam,
			 String procedureName,ArrayList<OutParameter> outParam) throws DBException, IOException, SQLException {
				int totSize = inParam.size() + outParam.size();				
				String callableString = "{ call "+procedureName+"("+getArgumentString(totSize)+")}";
				ArrayList<HashMap<String,Object>> result = new ArrayList<HashMap<String, Object>>();
				Connection conn = null;
				try{
					 conn = beginTxn();
					 result = executeRequest(inParam,outParam,callableString,conn);
				}catch(DBException ex){
		            rollback(conn);
		            throw ex;
			    }catch(Exception ex){
			            rollback(conn);
			            throw new DBException(0,"executeFunction "+procedureName,ex);
			    }finally{
			            endTxn(conn);
			    }
			    return result;
		        
		       
	}
	
	public ArrayList<HashMap<String,Object>> executeFunction(String functionName,ArrayList<OutParameter> outParam) throws DBException, IOException, SQLException {
		ArrayList<InParameter> inParam = new ArrayList<InParameter>();	
		return executeFunction(inParam,functionName,outParam);
	}
	public ArrayList<HashMap<String,Object>> executeFunction(ArrayList<InParameter> inParam,String functionName) throws DBException, IOException, SQLException {
		ArrayList<OutParameter> outParam = new ArrayList<OutParameter>();	
		return executeFunction(inParam,functionName,outParam);
	}
	public ArrayList<HashMap<String,Object>> executeFunction(String functionName) throws DBException, IOException, SQLException {
		ArrayList<InParameter> inParam = new ArrayList<InParameter>();	
		ArrayList<OutParameter> outParam = new ArrayList<OutParameter>();	
		return executeFunction(inParam,functionName,outParam);
	}
	public ArrayList<HashMap<String,Object>> executeFunction(ArrayList<InParameter> inParam,
			 String functionName,ArrayList<OutParameter> outParam) throws DBException, IOException, SQLException {
			String callableString = "{"+getArgumentString(outParam.size())+" = call "
											+functionName+"("+getArgumentString(inParam.size())+")}";	
			 ArrayList<HashMap<String,Object>> result = new ArrayList<HashMap<String, Object>>();
			 Connection conn = null;
			 try{
				 conn = beginTxn();
				 result = executeFunctionRequest(inParam,outParam,callableString,conn);
			 }catch(DBException ex){
		            rollback(conn);
		            throw ex;
		     }catch(Exception ex){
		            rollback(conn);
		            throw new DBException(0,"executeFunction "+functionName,ex);
		     }finally{
		            endTxn(conn);
		     }			
			
		    return result;
		       
	}
	private ArrayList<HashMap<String,Object>> executeFunctionRequest(ArrayList<InParameter> inParam,
			ArrayList<OutParameter> outParam, String callableString, Connection conn) throws DBException, IOException, SQLException {
			ResultSet resultSet = null;
			HashMap<String,Object> htResultSet = new HashMap<String, Object>();
			ArrayList<HashMap<String,Object>> result = new ArrayList<HashMap<String, Object>>();
			CallableStatement callableStatement = null;
			callableStatement = conn.prepareCall(callableString);		
			int paramterId = 0;
			
			for(OutParameter outParameter : outParam){
				
				if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_STRING) ){
					callableStatement.registerOutParameter(++paramterId,Types.VARCHAR);
	                
	            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_INTEGER) ) {
	            	callableStatement.registerOutParameter(++paramterId,Types.INTEGER);
	                
	            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_DOUBLE) ) {
	            	callableStatement.registerOutParameter(++paramterId,Types.DOUBLE);
	                
	            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_FLOAT) ) {
	            	callableStatement.registerOutParameter(++paramterId,Types.FLOAT);
	                
	            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_TIMESTAMP) ) {
	            	callableStatement.registerOutParameter(++paramterId,Types.TIMESTAMP);
	                
	            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_DATE) ) {
	            	callableStatement.registerOutParameter(++paramterId,Types.DATE);
	                
	            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_TIME) ) {
	            	callableStatement.registerOutParameter(++paramterId,Types.TIME);
	                
	            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_RESULTSET) ) {
	            	callableStatement.registerOutParameter(++paramterId,OracleTypes.CURSOR);
	                
	            }
				
			}
			
			for(InParameter inParameter : inParam){			
				if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_STRING)){
					callableStatement.setString(++paramterId, getStrValue(inParameter.getParameterValue()));				
				
				}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_INT)){
					callableStatement.setInt(++paramterId, getIntValue(inParameter.getParameterValue()));
					
				}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_INTEGER)){
					
					if(getIntegerValue(inParameter.getParameterValue()) == null){				
						callableStatement.setNull(++paramterId, java.sql.Types.INTEGER);
						
					}else{				 
					 callableStatement.setInt(++paramterId, getIntegerValue(inParameter.getParameterValue()).intValue());
					
					}
					
				}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_FLOAT)){				
					callableStatement.setFloat(++paramterId, getFloatValue(inParameter.getParameterValue()));				
					
				}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_DOUBLE)){				
					callableStatement.setDouble(++paramterId, getDoubleValue(inParameter.getParameterValue()));				
					
				}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_DATE)){				
					callableStatement.setDate(++paramterId, getDateValue(inParameter.getParameterValue()));				
					
				}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_TIMESTAMP)){
					callableStatement.setTimestamp(++paramterId, getTimestampValue(inParameter.getParameterValue()));
				
				}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_TIME)){
					callableStatement.setTime(++paramterId, getTimeValue(inParameter.getParameterValue()));
					
				}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_CLOB)){
					
					 String clobData = getClobValue(inParameter.getParameterValue());
	                 if (clobData != null) {
	                     ByteArrayInputStream bais = new ByteArrayInputStream(clobData.getBytes());
	                     callableStatement.setAsciiStream(++paramterId, bais, bais.available());
	                     bais.close();
	                 } else {
	                	 callableStatement.setClob(++paramterId, CLOB.empty_lob());
	                 }
	                 
				}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_BLOB)){
					  byte[] buffer = null;
	                  buffer = (byte[])getBlobValue(inParameter.getParameterValue());
	                  InputStream inputStream ;
	                  inputStream = new ByteArrayInputStream(buffer);                  
	                  callableStatement.setBinaryStream(++paramterId,inputStream, inputStream.available());
	                  inputStream.close();
	                  
				}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_LONG)){
					 byte[] buffer = null;
	                 String strValue = (String)inParameter.getParameterValue();
	                 buffer = strValue.getBytes();
	                 InputStream inputStream ;
	                 inputStream = new ByteArrayInputStream(buffer);
	                 callableStatement.setAsciiStream(++paramterId,inputStream, inputStream.available());
	                 inputStream.close();
				}
				
				
			}
			
			callableStatement.execute();
				
				int outParaIndex = 0;
				
				if(outParam != null && outParam.isEmpty()){
					result = new ArrayList<HashMap<String, Object>>();
				}else{
					for(OutParameter outResult : outParam){
						if(outResult.getParameterType().equals(DBEngineConstants.TYPE_RESULTSET)){
							resultSet = (ResultSet)callableStatement.getObject(++outParaIndex);
							result = packageResult(resultSet);
	                        break;
							
						}else if(outResult.getParameterType().equals(DBEngineConstants.TYPE_STRING)){
							String tempStr = callableStatement.getString(++outParaIndex);
	                        htResultSet.put(outResult.getParameterName(),tempStr);
	                        result=packageResult(htResultSet);
							
						}else if(outResult.getParameterType().equals(DBEngineConstants.TYPE_INTEGER)){
							htResultSet.put(outResult.getParameterName(),""+callableStatement.getInt(++outParaIndex));
	                        result = packageResult(htResultSet);
							
						}else if(outResult.getParameterType().equals(DBEngineConstants.TYPE_FLOAT)){
							htResultSet.put(outResult.getParameterName(),""+callableStatement.getDouble(++outParaIndex));
	                        result = packageResult(htResultSet);
							
						}
						
					}
					
				}
							
			
			return result;
			
		}
	
	private ArrayList<HashMap<String,Object>> executeRequest(ArrayList<InParameter> inParam,
		ArrayList<OutParameter> outParam, String callableString, Connection conn) throws DBException, IOException, SQLException {
		ResultSet resultSet = null;
		HashMap<String,Object> htResultSet = new HashMap<String, Object>();
		ArrayList<HashMap<String,Object>> result = new ArrayList<HashMap<String, Object>>();
		CallableStatement callableStatement = null;
		callableStatement = conn.prepareCall(callableString);		
		int paramterId = 0;
		
		for(InParameter inParameter : inParam){			
			if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_STRING)){
				callableStatement.setString(++paramterId, getStrValue(inParameter.getParameterValue()));				
			
			}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_INT)){
				callableStatement.setInt(++paramterId, getIntValue(inParameter.getParameterValue()));
				
			}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_INTEGER)){
				
				if(getIntegerValue(inParameter.getParameterValue()) == null){				
					callableStatement.setNull(++paramterId, java.sql.Types.INTEGER);
					
				}else{				 
				 callableStatement.setInt(++paramterId, getIntegerValue(inParameter.getParameterValue()).intValue());
				
				}
				
			}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_FLOAT)){				
				callableStatement.setFloat(++paramterId, getFloatValue(inParameter.getParameterValue()));				
				
			}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_DOUBLE)){				
				callableStatement.setDouble(++paramterId, getDoubleValue(inParameter.getParameterValue()));				
				
			}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_DATE)){				
				callableStatement.setDate(++paramterId, getDateValue(inParameter.getParameterValue()));				
				
			}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_TIMESTAMP)){
				callableStatement.setTimestamp(++paramterId, getTimestampValue(inParameter.getParameterValue()));
			
			}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_TIME)){
				callableStatement.setTime(++paramterId, getTimeValue(inParameter.getParameterValue()));
				
			}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_CLOB)){
				
				 String clobData = getClobValue(inParameter.getParameterValue());
                 if (clobData != null) {
                     ByteArrayInputStream bais = new ByteArrayInputStream(clobData.getBytes());
                     callableStatement.setAsciiStream(++paramterId, bais, bais.available());
                     bais.close();
                 } else {
                	 //callableStatement.setClob(++paramterId, CLOB.empty_lob());
                 }
                 
			}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_BLOB)){
				  byte[] buffer = null;
                  buffer = (byte[])getBlobValue(inParameter.getParameterValue());
                  InputStream inputStream ;
                  inputStream = new ByteArrayInputStream(buffer);                  
                  callableStatement.setBinaryStream(++paramterId,inputStream, inputStream.available());
                  inputStream.close();
                  
			}else if(inParameter.getParameterType().equals(DBEngineConstants.TYPE_LONG)){
				 byte[] buffer = null;
                 String strValue = (String)inParameter.getParameterValue();
                 buffer = strValue.getBytes();
                 InputStream inputStream ;
                 inputStream = new ByteArrayInputStream(buffer);
                 callableStatement.setAsciiStream(++paramterId,inputStream, inputStream.available());
                 inputStream.close();
			}
			
			
		}
		for(OutParameter outParameter : outParam){
			
			if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_STRING) ){
				callableStatement.registerOutParameter(++paramterId,Types.VARCHAR);
                
            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_INTEGER) ) {
            	callableStatement.registerOutParameter(++paramterId,Types.INTEGER);
                
            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_DOUBLE) ) {
            	callableStatement.registerOutParameter(++paramterId,Types.DOUBLE);
                
            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_FLOAT) ) {
            	callableStatement.registerOutParameter(++paramterId,Types.FLOAT);
                
            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_TIMESTAMP) ) {
            	callableStatement.registerOutParameter(++paramterId,Types.TIMESTAMP);
                
            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_DATE) ) {
            	callableStatement.registerOutParameter(++paramterId,Types.DATE);
                
            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_TIME) ) {
            	callableStatement.registerOutParameter(++paramterId,Types.TIME);
                
            }else if (outParameter.getParameterType().equalsIgnoreCase(DBEngineConstants.TYPE_RESULTSET) ) {
            	callableStatement.registerOutParameter(++paramterId,OracleTypes.CURSOR);
                
            }
			
		}
		
		callableStatement.execute();
			
			int inputParmSize = inParam.size();
			int outParaIndex = inputParmSize;
			
			if(outParam != null && outParam.isEmpty()){
				result = new ArrayList<HashMap<String, Object>>();
			}else{
				for(OutParameter outResult : outParam){
					if(outResult.getParameterType().equals(DBEngineConstants.TYPE_RESULTSET)){
						resultSet = (ResultSet)callableStatement.getObject(++outParaIndex);
						result = packageResult(resultSet);
                        break;
						
					}else if(outResult.getParameterType().equals(DBEngineConstants.TYPE_STRING)){
						String tempStr = callableStatement.getString(++outParaIndex);
                        htResultSet.put(outResult.getParameterName(),tempStr);
                        result=packageResult(htResultSet);
						
					}else if(outResult.getParameterType().equals(DBEngineConstants.TYPE_INTEGER)){
						htResultSet.put(outResult.getParameterName(),""+callableStatement.getInt(++outParaIndex));
                        result = packageResult(htResultSet);
						
					}else if(outResult.getParameterType().equals(DBEngineConstants.TYPE_FLOAT)){
						htResultSet.put(outResult.getParameterName(),""+callableStatement.getDouble(++outParaIndex));
                        result = packageResult(htResultSet);
						
					}
					
				}
				
			}
						
		
		return result;
		
	}
	
	private ArrayList<HashMap<String,Object>> packageResult(HashMap<String,Object> htRset){
		ArrayList<HashMap<String,Object>> alResSet = new ArrayList<HashMap<String, Object>>();
		alResSet.add(htRset);
        return alResSet;
    }
	
    private ArrayList<HashMap<String,Object>> packageResult(ResultSet rset) throws DBException{
    	ArrayList<HashMap<String,Object>> alResSet = new ArrayList<HashMap<String, Object>>();
        try{
            java.sql.ResultSetMetaData metaData= rset.getMetaData();
            int colCount = metaData.getColumnCount();
            String colName = null;
            Object colValue = null;
            while(rset.next()){
                HashMap<String,Object> htRow = new HashMap<String, Object>();
                for(int i=1;i<=colCount;i++){
                    colName = metaData.getColumnName(i);                    
                    int colDatatype = metaData.getColumnType(i);                    
                    switch(colDatatype) {                       
                        case java.sql.Types.BLOB:
                            try {
                                ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
                                if(colDatatype == java.sql.Types.LONGVARCHAR) { // data type is LONG RAW
                                    
                                }else { // data type is BLOB
                                    Blob binaryData = null;
                                    binaryData = rset.getBlob(i);
                                    //Null check code added for blob data.
                                    if(binaryData != null){
                                        bytesOut.write(binaryData.getBytes(1, (int) binaryData.length()));
                                    }
                                }
                                bytesOut.flush();
                                bytesOut.close();
                                colValue = bytesOut;
                                break;
                            }catch(Exception Ex){
                               System.out.println(Ex);
                            }
                        case java.sql.Types.CLOB:
                            try {
                                java.sql.Clob characterData = null;
                                characterData = rset.getClob(i);
                                if(characterData != null && characterData.length()>0) {
                                    char charData[] = new char[(int)characterData.length()];
                                    characterData.getCharacterStream().read(charData);
                                    colValue = new String(charData);
                                }else {
                                    colValue = null;
                                }
                                break;
                            }catch(Exception Ex){
                            	 System.out.println(Ex);
                            }
                            break;
                        case java.sql.Types.DATE:
                            colValue = rset.getTimestamp(i);
                            break;
                        case java.sql.Types.TIMESTAMP:
                            colValue = rset.getTimestamp(i);
                            break;
                        default: // data type OTHERS
                            colValue = rset.getObject(i);
                            break;
                    }
                    htRow.put(colName,colValue);
                }
                alResSet.add(htRow);
            }
        }catch(SQLException sqlEx){
        	 System.out.println(sqlEx);
        }finally{
            try{
                if(rset!=null){
                    rset.close();
                }
            }catch(SQLException sqlEx){
            	 System.out.println(sqlEx);
            }
        }
        return alResSet;
    }
	
	private static String getArgumentString(int totArg){
		if(totArg == 0){
			return "";
		}		
		String arg = "";
		while((totArg - 1) > 0){
			arg = arg + "?,";
			totArg--;
		}			
		return arg + "?";
	}
	
   
    /**
     *  Get the long value if the type is "Long"
     *  @return double Long value
     */
    public double getLongValue(Object value) {
        if (value!=null && value instanceof String ){
            return (Long.valueOf((String)value)).longValue();
        }
        else
            return -1;
    }
    /**
     *  Get the String value if the type is "String"
     *  @return String value
     */
    public String getStrValue(Object value){
        if (value!=null && value instanceof String )
            return (String)value;
        else
            return null;        
    }
    
    /**
     *  Get the <code>java.sql.Date</code> value if the type is "Date"
     *  @return Date value
     */
    public java.sql.Date getDateValue(Object value) throws DBException{
        if (value != null ){
            /*if(value instanceof java.sql.Timestamp){
                return (java.sql.Timestamp)value;
            }else
             */
            if(value instanceof java.sql.Date){
                return (java.sql.Date)value;
            }else {
                String exMsg = "Wrong type:\n Expected type of the "+value+
                                "is : java.sql.Date\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
            }
        }else
            return null;
    }
    /**
     *  Get the <code>java.sql.Timestamp</code> value if the type is "Timestamp"
     *  @return Timestamp Timestamp value
     */
    public java.sql.Timestamp getTimestampValue(Object value) throws DBException{
        if (value!=null ){
            if(value instanceof java.sql.Timestamp){
                return (java.sql.Timestamp)value;
            }else {
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                "is : java.sql.TimeStamp\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
            }
        }else
            return null;
    }
    /**
     *  Get the <code>java.sql.Time</code> value if the type is "Time"
     *  @return Time Time value
     */
    public java.sql.Time getTimeValue(Object value) throws DBException{
        if (value!=null){
            if(value instanceof java.sql.Time){
                return (java.sql.Time)value;
            }else {
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                "is : java.sql.Time\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
            }
        }else
            return null;
    }
    /**
     *  Get the int value if the type is "int"
     *  @return int value
     */
    public int getIntValue(Object value) throws DBException{
        if(value==null)
            return -1;
        if ((value instanceof Integer || value instanceof String)){
            return Integer.parseInt(value.toString());
        }else{
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                " is :java.lang.String or java.lang.Integer\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
        }   
//        return -1;
    }
    /**
     *  Get the float value if the type is "float"
     *  @return float value
     */
    public float getFloatValue(Object value) throws DBException{
        if(value==null)
            return 0;
        if ((value instanceof Float  || value instanceof String)){
            return (Float.valueOf(value.toString())).floatValue();
        }else{
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                " is :java.lang.String or java.lang.Float\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
        }
//        return -1;
    }
    /**
     *  Get the Float value if the type is "Float"
     *  @return Float value
     */
    public Float getFloatObjValue(Object value) throws DBException{
        if(value==null)
            return null;
        if (value instanceof Float){
            return (Float)value;
        }else{
                String exMsg = "Type is wrong:\n Expected type of the "+value+
                                " is :java.lang.Float\n"+
                                "Passed value type is : "+value.getClass();
                throw new DBException(exMsg);
        }
    }
    /**
     *  Get the double value if the type is "double"
     *  @return double value
     */
    public double getDoubleValue(Object value) throws DBException{
        if(value==null)
            return 0;
        if ((value instanceof Double || value instanceof String )){
            return (Double.valueOf(value.toString())).doubleValue();
        }else{
            String exMsg = "Type is wrong:\n Expected type of the "+value+
                            " is :java.lang.String or java.lang.Double\n"+
                            "Passed value type is : "+value.getClass();
            throw new DBException(exMsg);
        }
    }
    /**
     *  Get the Double value if the type is "Double"
     *  @return Double value
     */
    public Double getDoubleObjValue(Object value) throws DBException{
        if(value==null)
            return null;
        if (value instanceof Double){
            return (Double)value;
        }else{
            String exMsg = "Type is wrong:\n Expected type of the "+value+
                            " is :java.lang.Double\n"+
                            "Passed value type is : "+value.getClass();
            throw new DBException(exMsg);
        }
    }
    /**
     *  Get the Integer value if the type is "Integer"
     *  @return Integer value
     */
    public Integer getIntegerValue(Object value) throws DBException{
        if(value==null)
            return null;
        if (value instanceof Integer){
            return (Integer)value;
        }else{
            String exMsg = "Type is wrong:\n Expected type of the "+value+
                            " is :java.lang.Integer\n"+
                            "Passed value type is : "+value.getClass();
            throw new DBException(exMsg);
        }
    }
    /**
     *  Get the Object if the type is "Blob"
     *  @return Object value
     */
    public Object  getBlobValue(Object value) {
        if (value!=null && !(value instanceof String)  ){
            return value;
        }
        else
            return null;
    }
    /**
     *  Get the Object if the type is "Clob"
     *  @return Object value
     */
    public String  getClobValue(Object value) {
        if (value!=null){
            return value.toString();
        }else
            return null;
    }
    /**
     *  Get the Object if the type is "Binary"
     *  @return Object value
     */
    public Object  getBinaryValue(Object value) {
        if (value!=null && !(value instanceof String)  ){
            return value;
        }
        else
            return null;
    }
}