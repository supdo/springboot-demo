package com.supdo.sb.demo.entity;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BaseEntity {
	
	private LinkedHashMap<String, FormField> fields = new LinkedHashMap<String, FormField>();

	/**
	 * 根据Entity的@FormMeta注解生成fields成员信息，以便在View层通过模板来自动生产表单的HTML代码。
	 * @return 当前的BaseEntity实例
	 */
	public BaseEntity initForm(Class<?> group) {
		fields.clear();
		Field[] myFields =  this.getClass().getDeclaredFields();
		for (Field field : myFields) {
			FormMeta meta = field.getAnnotation(FormMeta.class);
			if(meta != null && (Arrays.asList(meta.groups()).contains(group) || group == null)) {
				FormField tmpField = new FormField();
				tmpField.setName(field.getName());
				tmpField.setType(meta.type().toString());
				tmpField.setLabel(meta.label());
				tmpField.setRequired(meta.required());
				tmpField.setPlaceholder(meta.placeholder());
				tmpField.setTips(meta.tips());
				tmpField.setAttrs(getMapFromMeta(meta.attrs()));
				tmpField.setOptions(getMapFromMeta(meta.options()));
				try {
					field.setAccessible(true);
					tmpField.setValue(field.get(this));
				} catch (Exception e) {
					e.printStackTrace();
				}
				fields.put(field.getName(), tmpField);
			}			
		}
		return this;
	}
	
	public BaseEntity initForm() {
		return this.initForm(null);
	}
	
	/**
	 * 根据注解中的字符串生成Map信息。
	 * @param str： 注解中的字符串
	 * @return Map
	 */
	protected LinkedHashMap<String, String> getMapFromMeta(String str) {
		LinkedHashMap<String, String> tmpMap = new LinkedHashMap<String, String>();
		if(str.length() == 0) {return tmpMap;}
		try {
			for (String subStr : Arrays.asList(str.split("!"))) {
				String[] myMap = subStr.split(":");
				tmpMap.put(myMap[0], myMap[1]);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return tmpMap;
	}
	protected LinkedHashMap<String, String> getMapFromMeta(String[] strList) {
		LinkedHashMap<String, String> tmpMap = new LinkedHashMap<String, String>();
		if(strList.length == 0) {return tmpMap;}
		try {
			for (String subStr : strList) {
				String[] myMap = subStr.split(":");
				tmpMap.put(myMap[0], myMap[1]);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return tmpMap;
	}
	
	/**
	 * 将bindingResult的保单验证出的错误写到FormField中
	 * @param result
	 * @return
	 */
	public BaseEntity initFieldErrors(BindingResult result) {
		for (FieldError error : result.getFieldErrors()) {
			fields.get(error.getField()).setError(error.getDefaultMessage());
		}
		return this;
	}
	
	public LinkedHashMap<String, FormField> getFields() {
		return fields;
	}

	public void setFields(LinkedHashMap<String, FormField> fields) {
		this.fields = fields;
	}
	
}