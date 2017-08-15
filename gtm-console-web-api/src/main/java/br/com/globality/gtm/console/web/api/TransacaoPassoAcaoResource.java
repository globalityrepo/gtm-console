package br.com.globality.gtm.console.web.api;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.globality.gtm.console.model.TransacaoPassoAcao;
import br.com.globality.gtm.console.model.compositeId.TransacaoPassoAcaoCompositeId;
import br.com.globality.gtm.console.service.GenericPersitenceService;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Endpoint REST para resolver a entidade TransacaoPassoAcao.
 * 
 * @author Leonardo Andrade
 *
 */
@Path("/transacaopassoacao")
public class TransacaoPassoAcaoResource {	
	
	@Inject
	private GenericPersitenceService persistenceService;
	
	@Inject
	private Logger log;
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response updateByCompositeId(String json) {
		TransacaoPassoAcao param = null;
		try {
			param  = new ObjectMapper().readValue(json.getBytes("UTF-8"), TransacaoPassoAcao.class);
		} 
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		try {
			if (!param.getId().getIdEventoTipo().equalsIgnoreCase(param.getEventoTipo().getId())
					|| param.getId().getIdTransacaoPasso().longValue()!=param.getTransacaoPasso().getId().longValue()) {
				TransacaoPassoAcaoCompositeId idRef = new TransacaoPassoAcaoCompositeId();
				idRef.setIdEventoTipo(param.getEventoTipo().getId());
				idRef.setIdTransacaoPasso(param.getTransacaoPasso().getId());
				TransacaoPassoAcao voRef = persistenceService.findById(TransacaoPassoAcao.class, idRef);
				if (voRef!=null) {
					return Response.status(Response.Status.BAD_REQUEST).entity(MessageBundle.getString("msg.erro.chave.duplicada.detalhada", "Passo e Tipo de Evento")).build();
				}
				// Implementação em decorrencia da chave composta não permitir alteração da chave.
				// Efetuando remoção do registro atual.
				persistenceService.delete(TransacaoPassoAcao.class, param.getId());
				// Efetuando inclusão do novo registro.
				TransacaoPassoAcao vo = new TransacaoPassoAcao();
				vo.setDestinatario(param.getDestinatario());
				vo.setFilaDestino(param.getFilaDestino());
				vo.setIntervalo(param.getIntervalo());
				vo.setNamespace(param.getNamespace());
				vo.setPrefixoNameSpace(param.getPrefixoNameSpace());
				vo.setQtdeTentativas(param.getQtdeTentativas());
				vo.setReenvio(param.getReenvio());
				vo.setXpath(param.getXpath());
				TransacaoPassoAcaoCompositeId id = new TransacaoPassoAcaoCompositeId();
				id.setIdEventoTipo(param.getEventoTipo().getId());
				id.setIdTransacaoPasso(param.getTransacaoPasso().getId());
				vo.setId(id);
				persistenceService.add(vo);
			}
			else {
				persistenceService.update(param);
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}				
		return Response.ok(MediaType.APPLICATION_JSON).build();
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/delete")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response deleteByCompositeId(String json) {
		TransacaoPassoAcao param = null;
		try {
			param  = new ObjectMapper().readValue(json.getBytes("UTF-8"), TransacaoPassoAcao.class);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		try {
			persistenceService.delete(TransacaoPassoAcao.class, param.getId());
		}
		catch (EJBTransactionRolledbackException e) {
		    Throwable t = e.getCause();
		    while ((t != null) && !(t instanceof ConstraintViolationException)) {
		        t = t.getCause();
		    }
		    if (t instanceof ConstraintViolationException) {
		    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
		    }
		    else {
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
