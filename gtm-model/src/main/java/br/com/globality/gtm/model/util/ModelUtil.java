package br.com.globality.gtm.model.util;

import org.apache.commons.lang.StringUtils;

import br.com.globality.gtm.util.annotation.RESTful;

public class ModelUtil {
	
	private static final String BASE_PACKAGE = "br.com.globality.gtm.model.";
	
	public static Class<?> getSuppportedClass(String entity) {
		try {
			Class<?> c = Class.forName(BASE_PACKAGE+StringUtils.capitalize(entity));
	    	if (c.isAnnotationPresent(RESTful.class)) {
	    		return c;
	    	}
	    	return null;
		}
		catch (Exception e) {
			return null;
		}
	}
	
}
