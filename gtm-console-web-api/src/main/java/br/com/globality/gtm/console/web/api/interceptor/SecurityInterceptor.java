package br.com.globality.gtm.console.web.api.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.jboss.resteasy.util.Base64;

import br.com.globality.gtm.console.model.Usuario;
import br.com.globality.gtm.console.service.UsuarioService;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Este interceptor verifica as permissões de acesso para um usuário 
 * baseado no código de acesso e senha informados na requisição
 * */
@Provider
@ServerInterceptor
public class SecurityInterceptor implements PreProcessInterceptor
{
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";
	
	@Inject
	private UsuarioService usuarioService;
	
	@Override
	public ServerResponse preProcess(HttpRequest request, ResourceMethod methodInvoked) throws Failure, WebApplicationException
	{
		
		final ServerResponse ACCESS_DENIED = new ServerResponse(MessageBundle.getString("msg.erro.recurso.acessonegado"), 401, new Headers<Object>());;
		final ServerResponse ACCESS_FORBIDDEN = new ServerResponse(MessageBundle.getString("msg.erro.recurso.indisponivel"), 403, new Headers<Object>());;
		final ServerResponse SERVER_ERROR = new ServerResponse("INTERNAL SERVER ERROR", 500, new Headers<Object>());;
		
		Method method = methodInvoked.getMethod();
		
		//Acesso permitido para qualquer perfil.
		if(method.isAnnotationPresent(PermitAll.class))	{
			return null;
		}
		//Acesso negado independente do perfil.
		if(method.isAnnotationPresent(DenyAll.class)) {
			return ACCESS_FORBIDDEN;
		}
		
		//Recuperando o header da aplicação.
		final HttpHeaders headers = request.getHttpHeaders();
		
		//Recuperando a propriedade de autoriação do header informado pela requisição.
	    final List<String> authorization = headers.getRequestHeader(AUTHORIZATION_PROPERTY);
	    
	    //Se não for autorizado, acesso negado.
	    if(authorization == null || authorization.isEmpty()) {
	    	return ACCESS_DENIED;
	    }
	    
	    //Recuperando o token de usuário e senha.
	    final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
	    
	    //Decodificando o token de usuário e senha.
	    String usernameAndPassword;
		try {
			usernameAndPassword = new String(Base64.decode(encodedUserPassword));
		} 
		catch (IOException e) {
			return SERVER_ERROR;
		}

		//Recuperando usuário e senha no token.
	    final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
	    final String username = tokenizer.nextToken();
	    final String password = tokenizer.nextToken();
	    
	    try {
		    //Verificando usuário e senha.
		    Usuario usuarioLogado = usuarioService.efetuarLogin(username, password);
		    if (usuarioLogado==null)
		    	return ACCESS_DENIED;
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	return ACCESS_DENIED;
	    }
		
	    //TODO Implementar o controle de autorização de acesso no backend.
	    //Verificando se o usuário pode acessar a funcionalidade.
		if(method.isAnnotationPresent(RolesAllowed.class)) {
			RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
			Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));			
			//Permitir se checagem for OK.
			if( ! isUserAllowed(username, password, rolesSet)) {
				return ACCESS_DENIED;
			}
		}
		
		return null;
	}

	private boolean isUserAllowed(final String username, final String password,	final Set<String> rolesSet) {
		boolean isAllowed = false;
		
		// Recuperando a ROLE do usuário.
		// Neste caso, não temos as roles implementadas ainda, 
		// Então estamos usando a APP_USER_ROLE como default.
		String userRole = "APP_USER_ROLE";
		
		// Verificar se as roles do recurso podem ser acessadas pelo usuário.
		if(rolesSet.contains(userRole))	{
			isAllowed = true;
		}
		return isAllowed;
	}
	
}
