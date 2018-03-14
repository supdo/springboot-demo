package com.supdo.sb.demo.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.persistence.ManyToMany;

public class BaseEntity {
	
	private LinkedHashMap<String, FormField> fields = new LinkedHashMap<String, FormField>();
	private LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

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

	public BaseEntity initMap(String exclude ){
		return initMap("",exclude);
	}

	public BaseEntity initMap(String include , String exclude){
		map.clear();
		List<String> includes = Arrays.asList(include.split(","));
		List<String> excludes = Arrays.asList(exclude.split(","));
		Field[] myFields =  this.getClass().getDeclaredFields();
		if(include.length() > 0) {
			for (Field field : myFields) {
				String name = field.getName();
				if (includes.contains(name)) {
					try {
						field.setAccessible(true);
						map.put(name, field.get(this));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			for (Field field : myFields) {
				String name = field.getName();
				if (!excludes.contains(name)) {
					try {
						field.setAccessible(true);
						map.put(name, field.get(this));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return this;
	}

	/**
	 * 合并两个实例的属性，如果source的属性为不为null，则替换近this的对应属性。
	 * @param source 需要合并进来的属性对象
	 * @return
	 */
	public BaseEntity merge(BaseEntity source){
		Field[] myFields =  this.getClass().getDeclaredFields();
		//List<String> names = Arrays.asList(exclude);
//		Field[] sourceFields =  source.getClass().getDeclaredFields();
//		for(int i=0; i<myFields.length; i++){
//			Field myField = sourceFields[i];
//			Field sourceField = sourceFields[i];
//			myField.setAccessible(true);
//			sourceField.setAccessible(true);
//			try {
//				if(sourceField.get(source) != null){
//					myField.set(this, sourceField.get(source));
//                }
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}

		for (Field field : myFields) {
			String name = field.getName();
			//ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
			//ManyToMany注释字段不处理；
//			if(manyToMany == null) {
//
//			}
			field.setAccessible(true);
			try {
				if(!Modifier.isStatic(field.getModifiers())
						&& !Modifier.isFinal(field.getModifiers())
						&& field.get(source) != null ){
					field.set(this, field.get(source));
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return this;
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
			if(fields.containsKey(error.getField())) {
				fields.get(error.getField()).setError(error.getDefaultMessage());
			}
		}
		return this;
	}

	public boolean bindingHasError(BindingResult bindingResult, Class<?> group){
		boolean result = false;
		List<String> groupFields = new ArrayList<>();
		Field[] myFields =  this.getClass().getDeclaredFields();
		for (Field field : myFields) {
			FormMeta meta = field.getAnnotation(FormMeta.class);
			if(meta != null && (Arrays.asList(meta.groups()).contains(group) || group == null)){
				groupFields.add(field.getName());
			}
		}
		for (FieldError error : bindingResult.getFieldErrors()) {
			if(groupFields.contains(error.getField())){
				result = true;
				break;
			}
		}
		return result;
	}


	public LinkedHashMap<String, FormField> getFields() {
		return fields;
	}

	public void setFields(LinkedHashMap<String, FormField> fields) {
		this.fields = fields;
	}

	public LinkedHashMap<String, Object> getMap() {
		return map;
	}

	public void setMap(LinkedHashMap<String, Object> map) {
		this.map = map;
	}
}