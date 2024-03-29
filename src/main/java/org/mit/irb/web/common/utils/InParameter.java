package org.mit.irb.web.common.utils;

public class InParameter {


	public String parameterName;
	public String parameterType;
	public Object parameterValue;
	
	public InParameter(String name,String type,Object value){
		setParameterName(name);
		setParameterType(type);
		setParameterValue(value);
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
	public Object getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(Object parameterValue) {
		this.parameterValue = parameterValue;
	}
}
