package br.com.globality.gtm.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.globality.gtm.model.util.ModelUtil;
import br.com.globality.gtm.service.GenericPersitenceService;
import br.com.globality.gtm.util.annotation.RESTful;

/**
 * Endpoint REST para buscar entidades
 * 
 * @author Leonardo Andrade
 *
 */
@Path("/")
public class RESTfulResource {	
	
	@Inject
	private GenericPersitenceService persistenceService;
	
	@Path("/{entity}")
	@GET
	public Response find(@PathParam("entity") String entity) {
		
		Class<?> clazz = ModelUtil.getSuppportedClass(entity);
		
		if( clazz == null )
			return Response.status(Response.Status.NOT_FOUND).entity("Entidade " + entity + " não existe.").build();
		
		List<?> result = persistenceService.findAll(clazz);
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
	}
	
	@Path("/{entity}/{id}")
	@GET
	public Response find(@PathParam("entity") String entity, @PathParam("id") Long id) {
		
		Class<?> clazz = ModelUtil.getSuppportedClass(entity);
		
		if( clazz == null )
			return Response.status(Response.Status.NOT_FOUND).entity("Entidade " + entity + " não existe.").build();
		
		Object result = persistenceService.findById(clazz, id);
		
		if(result == null)
			return Response.status(Response.Status.NOT_FOUND).entity("Entidade " + entity + " com o id " + id + " não existe.").build();
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
	}
	
	@Path("/{entity}")
	@POST
	public Response add(@PathParam("entity") String entity, String json){
		
		Class<?> clazz = ModelUtil.getSuppportedClass(entity);
		
		if( clazz == null )
			return Response.status(Response.Status.NOT_FOUND).entity("Entidade " + entity + " não existe.").build();
		
		Object entityObject = null;
		try {
			entityObject  = new ObjectMapper().readValue(json.getBytes(), clazz);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Não foi possível converter o parametro para um objeto válido").build();
		} 
		
		Object result = persistenceService.add(entityObject);
		
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}
	
	@Path("/{entity}")
	@PUT
	public Response update(@PathParam("entity") String entity, String json){
		
		Class<?> clazz = ModelUtil.getSuppportedClass(entity);
		
		if( clazz == null )
			return Response.status(Response.Status.NOT_FOUND).entity("Entidade " + entity + " não existe.").build();
		
		Object entityObject = null;
		try {
			entityObject  = new ObjectMapper().readValue(json.getBytes(), clazz);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Não foi possível converter o parametro para um objeto válido").build();
		} 
		
		Object result = persistenceService.update(entityObject);
		
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}
	
	
	@Path("/{entity}/{id}")
	@DELETE
	public Response delete(@PathParam("entity") String entity, @PathParam("id") Long id) {
		
		Class<?> clazz = ModelUtil.getSuppportedClass(entity);
		
		if( clazz == null )
			return Response.status(Response.Status.NOT_FOUND).entity("Entidade " + entity + " não existe.").build();
		
		persistenceService.delete(clazz, id);
		
		return Response.ok(MediaType.APPLICATION_JSON).build();
	}
	
}
