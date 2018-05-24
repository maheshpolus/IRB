package org.mit.irb.web.common.utils;

public class OutParameter {


	public String parameterName;
	public String parameterType;
	
	public OutParameter(String name,String type){
		setParameterName(name);
		setParameterType(type);
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}	
}
