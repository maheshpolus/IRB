package org.mit.irb.web.correspondence.dto;

public class Exempt {
	private String catagory;
	private String description;
	public String getCatagory() {
		return catagory;
	}
	public String getDescription() {
		return description;
	}
	public Exempt(String catagory, String description) {
		this.catagory = catagory;
		this.description = description;
	}

}
