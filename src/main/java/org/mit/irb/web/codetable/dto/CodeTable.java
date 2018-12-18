package org.mit.irb.web.codetable.dto;

import java.util.HashMap;
import java.util.List;

public class CodeTable {
	private String group;
	private String codetable_name;
	private String description;
	private String database_table_name;
	private List<Fields> fields;
	private List<String> primary_key;
	private List<HashMap<String, Object>> dependency;
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Fields> getFields() {
		return fields;
	}
	public void setFields(List<Fields> fields) {
		this.fields = fields;
	}
	public List<HashMap<String, Object>> getDependency() {
		return dependency;
	}
	public void setDependency(List<HashMap<String, Object>> dependency) {
		this.dependency = dependency;
	}
	public String getCodetable_name() {
		return codetable_name;
	}
	public void setCodetable_name(String codetable_name) {
		this.codetable_name = codetable_name;
	}
	public String getDatabase_table_name() {
		return database_table_name;
	}
	public void setDatabase_table_name(String database_table_name) {
		this.database_table_name = database_table_name;
	}
	public List<String> getPrimary_key() {
		return primary_key;
	}
	public void setPrimary_key(List<String> primary_key) {
		this.primary_key = primary_key;
	}
}
