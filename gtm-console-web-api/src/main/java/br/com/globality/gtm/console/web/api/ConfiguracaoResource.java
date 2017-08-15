package br.com.globality.gtm.console.web.api;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.globality.gtm.console.model.ConfiguracaoSistema;
import br.com.globality.gtm.console.service.CommonService;
import br.com.globality.gtm.console.service.GenericPersitenceService;
import br.com.globality.gtm.console.util.AppUtil;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Endpoint REST para resolver a entidade ConfiguracaoSistema
 * 
 * @author Leonardo Andrade
 *
 */
@Path("/configuracao")
public class ConfiguracaoResource {	
	
	@Inject
	private CommonService commonService;
	
	@Inject
	private GenericPersitenceService persistenceService;
	
	@Inject
	private Logger log;
	
	@PermitAll
	@GET
	@Path("/nonauth/")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findNonAuth() {
		// Metodo que requer autenticação.	
	    ConfiguracaoSistema result = null;		
		try {
			result = commonService.findConfiguracaoSistema();
			AppUtil.changeAppLocale(result.getLocale());	
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		return Response.ok(result,MediaType.APPLICATION_JSON).build();		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response find() {
		ConfiguracaoSistema result = null;		
		try {
			result = commonService.findConfiguracaoSistema();
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response update(String json){
		ConfiguracaoSistema entityObject = null;
		try {
			entityObject  = new ObjectMapper().readValue(json.getBytes("UTF-8"), ConfiguracaoSistema.class);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		ConfiguracaoSistema result = null;		
		try {
			result = persistenceService.update(entityObject);
			AppUtil.changeAppLocale(result.getLocale());			
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}
	
}
