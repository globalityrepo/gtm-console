package br.com.globality.gtm.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;

import br.com.globality.gtm.model.Aplicacao;
import br.com.globality.gtm.service.AplicacaoService;
import br.com.globality.gtm.service.GenericPersitenceService;

/**
 * Endpoint RESTfull para o Aplicacao.
 *
 */
@Path("/teste")
@Produces(MediaType.APPLICATION_JSON)
public class AplicacaoResource {
	
	private static final String FORM_FILE_INPUT_NAME = "uploadFile";

	@Inject
	private AplicacaoService aplicacaoService;

	@Inject
	private GenericPersitenceService persistenceService;
	
	@Inject
	private Logger log;

	@Path("/")
	@GET
	public Response find() {
		
		List<?> result = persistenceService.findAll(Aplicacao.class);
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
	@Path("/{id}")
	@GET
	public Response find(@PathParam("id") Long id) {
		
		Object result = persistenceService.findById(Aplicacao.class, id);
		
		if(result == null)
			return Response.status(Response.Status.NOT_FOUND).entity("Resgistro não encontrado.").build();
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
	@Path("/")
	@POST
	public Response add(String json){
		
		Object entityObject = null;
		try {
			entityObject  = new ObjectMapper().readValue(json.getBytes(), Aplicacao.class);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Não foi possível converter o parametro para um objeto válido").build();
		} 
		
		Object result = persistenceService.add(entityObject);
		
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
		
	}
	
	@Path("/")
	@PUT
	public Response update(String json){
		
		Object entityObject = null;
		try {
			entityObject  = new ObjectMapper().readValue(json.getBytes(), Aplicacao.class);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Não foi possível converter o parametro para um objeto válido").build();
		} 
		
		Object result = persistenceService.update(entityObject);
		
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
		
	}
	
	
	@Path("/{id}")
	@DELETE
	public Response delete(@PathParam("id") Long id) {
		
		persistenceService.delete(Aplicacao.class, id);
		
		return Response.ok(MediaType.APPLICATION_JSON).build();
		
	}
	
}
