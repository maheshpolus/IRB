package org.mit.irb.web.common.utils;

import java.sql.SQLException;

public class DBException extends Exception{

    /**
     *  To store the error id
     */
    private int errorId; 
    /**
     *  To store the error message
     */
    private String userMessage;
    /**
     *  To store the exception code
     */
    private String exceptionCode;
    /**
     * DBException with just the user message. User message is passed up to parent
     * so that message can appear if caught as the parent class on getMessage method
     * errorId is 0 for the errors that are internal to DBEngine
     */
    public DBException(String userMessage) {
        super(userMessage);//=systemMessage;
        this.errorId=0;
        this.userMessage=userMessage;
    }
    /**
     * Used to create a DBException
     * error id is id for the user message
     * userMessage is Message String
     * System message usually obtained by doing getMessage on the exception caught
     */
    public DBException(int errorId,String userMessage,String systemMessage) {
        super(systemMessage+"\n"+userMessage);//=systemMessage;
        this.errorId=errorId;
        this.userMessage=userMessage;
    }
    /**
     * Used to create a DBException. This should be the one used normally
     * error id is id for the user message
     * userMessage is Message String
     * Exception passed will be normally a sql exception type
     * SQL exception are two types.One is database specific and other jdbc driver specific
     */
    public DBException(int errorId,String userMessage,  Exception sqle) {
        super(sqle);//=systemMessage;
        int err=0;
        if (sqle instanceof SQLException){
            if (((SQLException)sqle).getErrorCode()==0){
                //This is pure jdbc driver error
                err=((SQLException)sqle).getErrorCode();
            }
            else {
                //This is database error
                err=((SQLException)sqle).getErrorCode();
            }
        }
        this.errorId=errorId;
        this.userMessage=userMessage;
    }
    /**
     * Used to create a DBException. This should be the one used normally
     * error id is id for the user message
     * userMessage is Message String
     * Exception passed will be normally a sql exception type
     * SQL exception are two types.One is database specific and other jdbc driver specific
     */
    public DBException(int errorId,String userMessage,Exception sqle,String exceCode) {
        super(sqle.getMessage()+userMessage);
        this.errorId=errorId;
        this.userMessage=userMessage;
        this.exceptionCode = exceCode;
    }
    public DBException(Exception sqle) {
        super(sqle);
        this.errorId=-1;
        this.userMessage="Language Error, Contact Adminstrator";
    }
    public String getUserMessage() {
        return userMessage;
    }
    public String getSystemMessage() {
        return super.getMessage();
    }
    public int getErrorId() {
        return errorId;
    }

}