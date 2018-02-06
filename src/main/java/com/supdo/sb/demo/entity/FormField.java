package com.supdo.sb.demo.entity;

import java.util.LinkedHashMap;
import org.springframework.validation.FieldError;

public class FormField {
	
	private String name;
	private Object value;
	private String type;
	private String label;
	private boolean required;
	private String placeholder;
	private String tips;
	private LinkedHashMap<String, String> attrs;
	private LinkedHashMap<String, String> options;
	private String error;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getPlaceholder() {
		return placeholder;
	}
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public LinkedHashMap<String, String> getAttrs() {
		return attrs;
	}
	public void setAttrs(LinkedHashMap<String, String> attrs) {
		this.attrs = attrs;
	}
	public LinkedHashMap<String, String> getOptions() {
		return options;
	}
	public void setOptions(LinkedHashMap<String, String> options) {
		this.options = options;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
}