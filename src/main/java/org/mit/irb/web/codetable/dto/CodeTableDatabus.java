package org.mit.irb.web.codetable.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeTableDatabus {
	private CodeTable codetable;
	private Map<String, Object> configFile;
	private List<HashMap<String, Object>> tableData;
	private String selectedColumnForDownload;
	private String updatedUser;
	private String promptMessage;
	private Integer promptCode; //If 1 then successful else If 0 then error found
	
	public CodeTable getCodetable() {
		return codetable;
	}

	public void setCodetable(CodeTable codetable) {
		this.codetable = codetable;
	}

	public Map<String, Object> getConfigFile() {
		return configFile;
	}

	public void setConfigFile(Map<String, Object> configFile) {
		this.configFile = configFile;
	}

	public List<HashMap<String, Object>> getTableData() {
		return tableData;
	}

	public void setTableData(List<HashMap<String, Object>> tableData) {
		this.tableData = tableData;
	}

	public String getPromptMessage() {
		return promptMessage;
	}

	public void setPromptMessage(String promptMessage) {
		this.promptMessage = promptMessage;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	public Integer getPromptCode() {
		return promptCode;
	}

	public void setPromptCode(Integer promptCode) {
		this.promptCode = promptCode;
	}

	public String getSelectedColumnForDownload() {
		return selectedColumnForDownload;
	}

	public void setSelectedColumnForDownload(String selectedColumnForDownload) {
		this.selectedColumnForDownload = selectedColumnForDownload;
	}
}
