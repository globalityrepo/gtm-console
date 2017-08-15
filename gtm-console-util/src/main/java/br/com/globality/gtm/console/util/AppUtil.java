package br.com.globality.gtm.console.util;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class AppUtil {
	
	public static String gerarSenha(int tamanho){

		String[] carct ={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String senha=""; 
		for (int i=0; i<tamanho; i++){
			int j = (int) (Math.random()*carct.length);
			senha += carct[j];
		}
		return senha;
	}
	
	public static String encodeBase64(String texto) {
		try {
	        return new String(Base64.encodeBase64(texto.getBytes("UTF-8")));
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Falha na conversão de string para Base64.", e);
	    }
	}
	
	public static void changeAppLocale(String locale) {
		if ("en_US".equals(locale)) {
			Locale.setDefault(new Locale("en","US"));
		}
		else if ("es_ES".equals(locale)) {
			Locale.setDefault(new Locale("es","ES"));
		}
		else {
			Locale.setDefault(new Locale("pt","BR"));
		}	
	}
	
	public static String generateCodigo(String prefixo, Long nextValue, int padLeft) {
		String value = StringUtils.leftPad(String.valueOf(nextValue), padLeft, "0");
		String codigo = prefixo + value;
		return codigo;
	}	
	
}
