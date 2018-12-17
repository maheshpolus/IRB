package org.mit.irb.web.codetable.dto;

public class Fields {
	private String column_name;
	private String display_name;
	private String data_type;
	private Boolean is_editable;
	private Integer length;
	private Boolean can_empty;
	private Boolean visible;
	private Boolean value_changed;
	public String getColumn_name() {
		return column_name;
	}
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public Boolean getIs_editable() {
		return is_editable;
	}
	public void setIs_editable(Boolean is_editable) {
		this.is_editable = is_editable;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Boolean getCan_empty() {
		return can_empty;
	}
	public void setCan_empty(Boolean can_empty) {
		this.can_empty = can_empty;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public Boolean getValue_changed() {
		return value_changed;
	}
	public void setValue_changed(Boolean value_changed) {
		this.value_changed = value_changed;
	}
}
