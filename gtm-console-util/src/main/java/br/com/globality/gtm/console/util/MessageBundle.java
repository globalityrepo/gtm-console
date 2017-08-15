package br.com.globality.gtm.console.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageBundle {

	private static final String FILE_MESSAGE_NAME = "message";
	
	public static String getString(String chave) {	
		final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(FILE_MESSAGE_NAME, Locale.getDefault(), new UTF8Control());
		return RESOURCE_BUNDLE.getString(chave);		
	}
	
	public static String getString(String chave, Object... params) {
		final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(FILE_MESSAGE_NAME, Locale.getDefault(), new UTF8Control());
		return MessageFormat.format(RESOURCE_BUNDLE.getString(chave), params);
	}
	
}
