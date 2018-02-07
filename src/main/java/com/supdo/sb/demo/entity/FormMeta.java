package com.supdo.sb.demo.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)  
@Documented 
public @interface FormMeta {
	
	public enum FormType {
		text, email, password, textarea, select
	}
	
	FormType type() default FormType.text;
	
	String label() default "Label";
	
	boolean required() default false;
	
	String placeholder() default "";
	
	String tips() default "";
	
	// 格式：{"key:val", "key:val"}
	String[] attrs() default {};
	
	// 格式：{"key:val", "key:val"}
	String[] options() default {};
	
	//分组之用
	Class<?>[] groups() default {};
}


