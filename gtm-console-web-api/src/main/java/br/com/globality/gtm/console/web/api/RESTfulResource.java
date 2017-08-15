package br.com.globality.gtm.console.web.api;

import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.globality.gtm.console.model.AbstractEntity;
import br.com.globality.gtm.console.service.GenericPersitenceService;
import br.com.globality.gtm.console.service.exception.DAOException;
import br.com.globality.gtm.console.util.MessageBundle;
import br.com.globality.gtm.console.util.annotation.RESTfulEntities;

/**
 * Endpoint REST para buscar entidades
 * 
 * @author Leonardo Andrade
 *
 */
@Path("/restfulresource")
public class RESTfulResource {	
	
	@Inject @RESTfulEntities
	private Map<String, Class<?>> supportedEntities;
	
	@Inject
	private GenericPersitenceService persistenceService;
	
	@Inject
	private Logger log;
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/{entity}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findAll(@PathParam("entity") String entity) {
		
		Class<?> clazz = supportedEntities.get(entity);
		
		if( clazz == null )
			return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.entidade.naoexiste", entity)).build();
		
		List<?> result = null;		
		try {
			result = persistenceService.findAll(clazz);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@SuppressWarnings("unchecked")
	@GET
	@Path("/{entity}/{pageSize}/{currentPage}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findAllComPaginacao(@PathParam("entity") String entity, 
			@PathParam("pageSize") int pageSize, 
			@PathParam("currentPage") int currentPage) {
		
		Class<?> clazz = supportedEntities.get(entity);
		
		if( clazz == null )
			return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.entidade.naoexiste", entity)).build();
		
		List<AbstractEntity> result = null;		
		try {
			result = (List<AbstractEntity>) persistenceService.findByFiltroComPaginacao(clazz, null, pageSize, currentPage);
			if (result!=null && !result.isEmpty()) 
				result.get(0).setPaginacaoQtdeRegConsulta(persistenceService.countRegistrosByFiltro(clazz, null));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@SuppressWarnings("unchecked")
	@GET
	@Path("/{entity}/{pageSize}/{currentPage}/{filtro}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findAllComPaginacao(@PathParam("entity") String entity, 
			@PathParam("pageSize") int pageSize, 
			@PathParam("currentPage") int currentPage, 
			@PathParam("filtro") String filtro) {
		
		Class<?> clazz = supportedEntities.get(entity);
		
		if( clazz == null )
			return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.entidade.naoexiste", entity)).build();
		
		List<AbstractEntity> result = null;		
		try {
			result = (List<AbstractEntity>) persistenceService.findByFiltroComPaginacao(clazz, filtro, pageSize, currentPage);
			if (result!=null && !result.isEmpty()) 
				result.get(0).setPaginacaoQtdeRegConsulta(persistenceService.countRegistrosByFiltro(clazz, filtro));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/{entity}")
	@POST
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response add(@PathParam("entity") String entity, String json){
		
		Class<?> clazz = supportedEntities.get(entity);
		
		if( clazz == null )
			return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.entidade.naoexiste", entity)).build();
		
		Object entityObject = null;
		try {
			entityObject  = new ObjectMapper().readValue(json.getBytes("UTF-8"), clazz);			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		
		Object result = null;		
		try {
			result = persistenceService.add(entityObject);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/{entity}")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response update(@PathParam("entity") String entity, String json){
		
		Class<?> clazz = supportedEntities.get(entity);
		
		if( clazz == null )
			return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.entidade.naoexiste", entity)).build();
		
		Object entityObject = null;
		try {
			entityObject  = new ObjectMapper().readValue(json.getBytes("UTF-8"), clazz);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
				
		Object result = null;		
		try {
			result = persistenceService.update(entityObject);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/{entity}/{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response delete(@PathParam("entity") String entity, @PathParam("id") String id) {
				
 		Class<?> clazz = supportedEntities.get(entity);
		
		if( clazz == null )
			return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.entidade.naoexiste", entity)).build();
		
		try {
			persistenceService.delete(clazz, Long.valueOf(id));
		}
		catch (EJBTransactionRolledbackException e) {
		    Throwable t = e.getCause();
		    while ((t != null) && !(t instanceof ConstraintViolationException)) {
		        t = t.getCause();
		    }
		    if (t instanceof ConstraintViolationException) {
		    	log.error(t.getMessage(), t);
		    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
		    }
		    else {
		    	log.error(e.getMessage(), e);
		    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		    }
		}
		catch (NumberFormatException e1) {
			try {
				persistenceService.delete(clazz, id);
			}
			catch (EJBTransactionRolledbackException e) {
			    Throwable t = e.getCause();
			    while ((t != null) && !(t instanceof ConstraintViolationException)) {
			        t = t.getCause();
			    }
			    if (t instanceof ConstraintViolationException) {
			    	log.error(t.getMessage(), t);
			    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
			    }
			    else {
			    	log.error(e.getMessage(), e);
			    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
			    }
			}
			catch (DAOException e2) {
				try {
					persistenceService.delete(clazz, id + " ");
				}
				catch (EJBTransactionRolledbackException e) {
				    Throwable t = e.getCause();
				    while ((t != null) && !(t instanceof ConstraintViolationException)) {
				        t = t.getCause();
				    }
				    if (t instanceof ConstraintViolationException) {
				    	log.error(t.getMessage(), t);
				    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
				    }
				    else {
				    	log.error(e.getMessage(), e);
				    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
				    }
				}
				catch (Exception e) {
					log.error(e.getMessage(), e);
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
				}
			}
			catch (Exception e) {
				log.error(e.getMessage(), e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
			}
		}
		catch (DAOException e1) {
			try {
				persistenceService.delete(clazz, id);
			}
			catch (EJBTransactionRolledbackException e) {
			    Throwable t = e.getCause();
			    while ((t != null) && !(t instanceof ConstraintViolationException)) {
			        t = t.getCause();
			    }
			    if (t instanceof ConstraintViolationException) {
			    	log.error(t.getMessage(), t);
			    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
			    }
			    else {
			    	log.error(e.getMessage(), e);
			    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
			    }
			}
			catch (DAOException e2) {
				try {
					persistenceService.delete(clazz, id + " ");
				}
				catch (EJBTransactionRolledbackException e) {
				    Throwable t = e.getCause();
				    while ((t != null) && !(t instanceof ConstraintViolationException)) {
				        t = t.getCause();
				    }
				    if (t instanceof ConstraintViolationException) {
				    	log.error(t.getMessage(), t);
				    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
				    }
				    else {
				    	log.error(e.getMessage(), e);
				    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
				    }
				}
				catch (Exception e) {
					log.error(e.getMessage(), e);
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
				}
			}
			catch (Exception e) {
				log.error(e.getMessage(), e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
				
		return Response.ok(MediaType.APPLICATION_JSON).build();
	}
		
}
