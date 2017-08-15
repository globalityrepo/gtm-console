package br.com.globality.gtm.console.web.api.interceptor;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import br.com.globality.gtm.console.util.AppUtil;

/**
 * Este interceptor define o locale da aplicação.
 * */
@Provider
@ServerInterceptor
public class LocaleInterceptor implements PreProcessInterceptor
{
	private static final String LOCALE_DEFAULT = "pt_BR";
	private static final String LANGUAGE_PROPERTY = "Language";
	private static final String LOCALE_SCHEME = "Locale";
		
	@Override
	public ServerResponse preProcess(HttpRequest request, ResourceMethod methodInvoked) throws Failure, WebApplicationException
	{
		
		//Recuperando o header da aplicação.
		final HttpHeaders headers = request.getHttpHeaders();
		
		//Recuperando a propriedade de idioma do header da aplicação.
	    final List<String> language = headers.getRequestHeader(LANGUAGE_PROPERTY);
	    
	    if(language == null || language.isEmpty()) {
	    	AppUtil.changeAppLocale(LOCALE_DEFAULT);
	    }
	    else {	    
		    //Recuperando o locale informado no header.
		    final String localeParam = language.get(0).replaceFirst(LOCALE_SCHEME + " ", "");		    
		    if(StringUtils.isNotBlank(localeParam)) {
		    	AppUtil.changeAppLocale(localeParam);
		    }
		    else {
		    	AppUtil.changeAppLocale(LOCALE_DEFAULT);
		    }
	    }
	    
		return null;
	}
}
