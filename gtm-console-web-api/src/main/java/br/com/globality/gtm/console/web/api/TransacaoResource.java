package br.com.globality.gtm.console.web.api;

import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.globality.gtm.console.model.EventoNivel;
import br.com.globality.gtm.console.model.Grupo;
import br.com.globality.gtm.console.model.Transacao;
import br.com.globality.gtm.console.model.TransacaoGrupo;
import br.com.globality.gtm.console.model.TransacaoParametro;
import br.com.globality.gtm.console.model.TransacaoPasso;
import br.com.globality.gtm.console.model.TransacaoPassoAcao;
import br.com.globality.gtm.console.model.TransacaoPassoEventoNivel;
import br.com.globality.gtm.console.model.compositeId.TransacaoPassoAcaoCompositeId;
import br.com.globality.gtm.console.model.compositeId.TransacaoPassoEventoNivelCompositeId;
import br.com.globality.gtm.console.service.GenericPersitenceService;
import br.com.globality.gtm.console.service.TransacaoService;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Endpoint REST para resolver a entidade Transação.
 * 
 * @author Leonardo Andrade
 *
 */
@Path("/transacao")
public class TransacaoResource {	
	
	@Inject
	private TransacaoService transacaoService;
	
	@Inject
	private GenericPersitenceService persistenceService;
	
	@Inject
	private Logger log;
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/transacaopasso/{idTransacao}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findTransacaoPassoByIdTransacao(@PathParam("idTransacao") Long idTransacao) {
		
		List<TransacaoPasso> result = null;			
		try {
			result = transacaoService.findTransacaoPassoByIdTransacao(idTransacao);
			for (TransacaoPasso transacaoPasso : result) {
				// Populando Níveis de Evento Associados.
				transacaoPasso.setEventoNiveis(persistenceService.findAll(EventoNivel.class));	
				List<TransacaoPassoEventoNivel> lstTransacaoPassoEventoNivel = transacaoService.findTransacaoPassoEventoNivelByIdTransacaoPasso(transacaoPasso.getId());
				for (EventoNivel evt : transacaoPasso.getEventoNiveis()) {
					evt.setConteudo("Y");
					for (TransacaoPassoEventoNivel item : lstTransacaoPassoEventoNivel) {
						if (evt.getId().equalsIgnoreCase(item.getEventoNivel().getId())) {
							evt.setConteudo(item.getConteudo());
							evt.setSelecionado(true);
						}
					}
				}
				// Populado Ações Associadas.
				long i=1;
				transacaoPasso.setAcoes(transacaoService.findTransacaoPassoAcaoByIdTransacaoPasso(transacaoPasso.getId()));
				// Implementação para possibilitar o controle de CRUD devido ao composite id da entidade não permitir.
				for (TransacaoPassoAcao acao : transacaoPasso.getAcoes()) {
					acao.setIdFake(i++);
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
	
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/transacaoparametro/{idTransacao}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findTransacaoParametroByIdTransacao(@PathParam("idTransacao") Long idTransacao) {
		
		List<TransacaoParametro> result = null;		
		
		try {
			result = transacaoService.findTransacaoParametroByIdTransacao(idTransacao);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
	
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/")
	@POST
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response add(String json){
		
		Transacao voPrinc 	= null;				
		try {
			voPrinc  = new ObjectMapper().readValue(json.getBytes("UTF-8"), Transacao.class);			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		
		try {
			persistenceService.add(voPrinc);
			// Incluindo Grupos de Transação.
			if (voPrinc.getGrupos()!=null && !voPrinc.getGrupos().isEmpty()) {
				for (TransacaoGrupo voAux : voPrinc.getGrupos()) {
					TransacaoGrupo transacaoGrupo = new TransacaoGrupo();
					transacaoGrupo.setGrupo(voAux.getGrupo());
					transacaoGrupo.setTransacao(voPrinc);
					persistenceService.add(transacaoGrupo);
				}
			}
			// Incluindo parâmetros.
			if (voPrinc.getParametros()!=null && !voPrinc.getParametros().isEmpty()) {
				for (TransacaoParametro voAux : voPrinc.getParametros()) {
					TransacaoParametro transacaoParametro = new TransacaoParametro();
					montarTransacaoParametroVO(voAux, transacaoParametro);
					transacaoParametro.setTransacao(voPrinc);
					persistenceService.add(transacaoParametro);
				}
			}
			// Incluindo passos.
			if (voPrinc.getPassos()!=null && !voPrinc.getPassos().isEmpty()) {
				for (TransacaoPasso voAux : voPrinc.getPassos()) {
					TransacaoPasso transacaoPasso = new TransacaoPasso();
					montarTransacaoPassoVO(voAux, transacaoPasso);
					transacaoPasso.setTransacao(voPrinc);
					persistenceService.add(transacaoPasso);
					// Incluindo níveis de eventos.
					for (EventoNivel voChildAux : voAux.getEventoNiveis()) {
						if (voChildAux.getSelecionado()) {
							TransacaoPassoEventoNivel transacaoPassoEventoNivel = new TransacaoPassoEventoNivel();
							transacaoPassoEventoNivel.setConteudo(voChildAux.getConteudo());
							TransacaoPassoEventoNivelCompositeId id = new TransacaoPassoEventoNivelCompositeId();
							id.setIdEventoNivel(voChildAux.getId());
							id.setIdTransacaoPasso(transacaoPasso.getId());
							transacaoPassoEventoNivel.setId(id);
							persistenceService.add(transacaoPassoEventoNivel);
						}
					}
					// Incluindo ações.
					if (voAux.getAcoes()!=null && !voAux.getAcoes().isEmpty()) {
						for (TransacaoPassoAcao acao : voAux.getAcoes()) {
							TransacaoPassoAcaoCompositeId id = new TransacaoPassoAcaoCompositeId();
							id.setIdEventoTipo(acao.getEventoTipo().getId());
							id.setIdTransacaoPasso(transacaoPasso.getId());
							acao.setId(id);
							persistenceService.add(acao);
						}
					}
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(voPrinc.getId(), MediaType.APPLICATION_JSON).build();
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response update(String json){
		
		Transacao voPrinc 	= null;				
		try {
			voPrinc  = new ObjectMapper().readValue(json.getBytes("UTF-8"), Transacao.class);			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		
		try {
			persistenceService.update(voPrinc);
			// Tratando grupos removidos.
			List<TransacaoGrupo> lstOrigGrupos = transacaoService.findGrupoByIdTransacao(voPrinc.getId());
			boolean achou = false;
			for (TransacaoGrupo transacaoGrupoOrig : lstOrigGrupos) {
				for (TransacaoGrupo transacaoGrupo : voPrinc.getGrupos()) {
					if (transacaoGrupoOrig.getId().equals(transacaoGrupo.getId())) {
						achou = true;
						break;
					}
				}
				if (!achou) {
					persistenceService.delete(TransacaoGrupo.class, transacaoGrupoOrig.getId());
				}
				achou = false;
			}
			// Tratando grupos adicionados.
			for (TransacaoGrupo voAux : voPrinc.getGrupos()) {
				if (voAux.getId()<0L) {
					TransacaoGrupo transacaoGrupo = new TransacaoGrupo();
					transacaoGrupo.setGrupo(voAux.getGrupo());
					transacaoGrupo.setTransacao(voPrinc);
					persistenceService.add(transacaoGrupo);
				}
			}
			// Atualizando parâmetros.	
			List<TransacaoParametro> lstOrigParams = transacaoService.findTransacaoParametroByIdTransacao(voPrinc.getId());
			achou = false;
			// Tratando parâmetros removidos e editados.
			for (TransacaoParametro voOriginal : lstOrigParams) {
				for (TransacaoParametro voAux : voPrinc.getParametros()) {
					if (voAux.getId().equals(voOriginal.getId())) {
						// Efetuando o update do registro.
						montarTransacaoParametroVO(voAux, voOriginal);
						persistenceService.update(voOriginal);
						// Definindo flag de controle.
						achou = true;
						break;
					}
				}
				if (!achou) {
					persistenceService.delete(TransacaoParametro.class, voOriginal.getId());
				}
				achou = false;
			}
			// Tratando parâmetros adicionados.
			for (TransacaoParametro voAux : voPrinc.getParametros()) {
				if (voAux.getId()<0L) {
					TransacaoParametro transacaoParametro = new TransacaoParametro();
					montarTransacaoParametroVO(voAux, transacaoParametro);
					transacaoParametro.setTransacao(voPrinc);
					persistenceService.add(transacaoParametro);
				}
			}			
			// Atualizando passos.	
			List<TransacaoPasso> lstOrigPassos = transacaoService.findTransacaoPassoByIdTransacao(voPrinc.getId());
			achou = false;
			// Tratando passos removidos e editados.
			for (TransacaoPasso voOriginal : lstOrigPassos) {
				List<TransacaoPassoEventoNivel> lstOrigEventosNivel = transacaoService.findTransacaoPassoEventoNivelByIdTransacaoPasso(voOriginal.getId());
				List<TransacaoPassoAcao> lstOrigAcoes = transacaoService.findTransacaoPassoAcaoByIdTransacaoPasso(voOriginal.getId());
				for (TransacaoPasso voAux : voPrinc.getPassos()) {
					if (voAux.getId().equals(voOriginal.getId())) {
						// Efetuando o update do registro.
						montarTransacaoPassoVO(voAux, voOriginal);
						persistenceService.update(voOriginal);
						// Tratando niveis de eventos associados.
						if (voAux.getEventoNiveis()!=null) {
							boolean achouChild = false;
							for (TransacaoPassoEventoNivel transacaoPassoEventoNivelOrig : lstOrigEventosNivel) {
								for (EventoNivel eventoNivel : voAux.getEventoNiveis()) {
									if (eventoNivel.getId().equalsIgnoreCase(transacaoPassoEventoNivelOrig.getEventoNivel().getId())
											&& eventoNivel.getSelecionado()) {
										// Efetuando update de alterações.
										transacaoPassoEventoNivelOrig.setConteudo(eventoNivel.getConteudo());
										persistenceService.update(transacaoPassoEventoNivelOrig);
										achouChild = true;
										break;
									}
								}
								if (!achouChild) {
									persistenceService.delete(TransacaoPassoEventoNivel.class, transacaoPassoEventoNivelOrig.getId());
								}
								achouChild = false;
							}
							// Trantando níveis de evento adicionados.
							for (EventoNivel eventoNivel : voAux.getEventoNiveis()) {
								for (TransacaoPassoEventoNivel transacaoPassoEventoNivelOrig : lstOrigEventosNivel) {
									if (eventoNivel.getId().equalsIgnoreCase(transacaoPassoEventoNivelOrig.getEventoNivel().getId())) {
										achouChild = true;
										break;
									}
								}
								if (!achouChild && eventoNivel.getSelecionado()) {
									TransacaoPassoEventoNivel voChildAux = new TransacaoPassoEventoNivel();
									voChildAux.setConteudo(eventoNivel.getConteudo());
									TransacaoPassoEventoNivelCompositeId id = new TransacaoPassoEventoNivelCompositeId();
									id.setIdEventoNivel(eventoNivel.getId());
									id.setIdTransacaoPasso(voOriginal.getId());
									voChildAux.setId(id);
									persistenceService.add(voChildAux);
								}
								achouChild = false;
							}
						}
						// Tratando ações associadas.
						if (voAux.getAcoes()!=null) {
							boolean achouChild = false;
							for (TransacaoPassoAcao transacaoPassoAcaoOrig : lstOrigAcoes) {
								for (TransacaoPassoAcao transacaoPassoAcao : voAux.getAcoes()) {
									if (transacaoPassoAcao.getId().equals(transacaoPassoAcaoOrig.getId())) {
										// Efetuando update de alterações.
										BeanUtils.copyProperties(transacaoPassoAcao, transacaoPassoAcaoOrig);
										persistenceService.update(transacaoPassoAcaoOrig);
										achouChild = true;
										break;
									}
								}
								// Efetuando exclusão.
								if (!achouChild) {
									persistenceService.delete(TransacaoPassoAcao.class, transacaoPassoAcaoOrig.getId());
								}
								achouChild = false;
							}
							// Trantando ações adicionadas.
							for (TransacaoPassoAcao transacaoPassoAcao : voAux.getAcoes()) {
								if (transacaoPassoAcao.getIdFake()<0L) {
									TransacaoPassoAcao voChildAux = new TransacaoPassoAcao();
									BeanUtils.copyProperties(transacaoPassoAcao, voChildAux);
									TransacaoPassoAcaoCompositeId id = new TransacaoPassoAcaoCompositeId();
									id.setIdEventoTipo(transacaoPassoAcao.getEventoTipo().getId());
									id.setIdTransacaoPasso(voOriginal.getId());
									voChildAux.setId(id);
									persistenceService.add(voChildAux);
								}
							}
						}
						// Definindo flag de controle.
						achou = true;
						break;
					}
				}
				if (!achou) {
					// Removendo niveis de eventos associados a passo.
					for (TransacaoPassoEventoNivel transacaoPassoEventoNivel : lstOrigEventosNivel) {
						persistenceService.delete(TransacaoPassoEventoNivel.class, transacaoPassoEventoNivel.getId());
					}
					// Removendo ações associadas ao passo.
					for (TransacaoPassoAcao transacaoPassoAcao : lstOrigAcoes) {
						persistenceService.delete(TransacaoPassoAcao.class, transacaoPassoAcao.getId());
					}
					// Removendo o registro principal do passo.
					persistenceService.delete(TransacaoPasso.class, voOriginal.getId());
				}
				achou = false;
			}
			// Tratando passos adicionados.
			for (TransacaoPasso voAux : voPrinc.getPassos()) {
				if (voAux.getId()<0L) {
					TransacaoPasso transacaoPasso = new TransacaoPasso();
					montarTransacaoPassoVO(voAux, transacaoPasso);
					transacaoPasso.setTransacao(voPrinc);
					persistenceService.add(transacaoPasso);
					// Incluindo níveis de eventos.
					for (EventoNivel eventoNivel : voAux.getEventoNiveis()) {
						if (eventoNivel.getSelecionado()) {
							TransacaoPassoEventoNivel transacaoPassoEventoNivel = new TransacaoPassoEventoNivel();
							transacaoPassoEventoNivel.setConteudo(eventoNivel.getConteudo());
							TransacaoPassoEventoNivelCompositeId id = new TransacaoPassoEventoNivelCompositeId();
							id.setIdEventoNivel(eventoNivel.getId());
							id.setIdTransacaoPasso(transacaoPasso.getId());
							transacaoPassoEventoNivel.setId(id);
							persistenceService.add(transacaoPassoEventoNivel);
						}
					}
					// Incluindo ações.
					if (voAux.getAcoes()!=null && !voAux.getAcoes().isEmpty()) {
						for (TransacaoPassoAcao acao : voAux.getAcoes()) {
							TransacaoPassoAcaoCompositeId id = new TransacaoPassoAcaoCompositeId();
							id.setIdEventoTipo(acao.getEventoTipo().getId());
							id.setIdTransacaoPasso(transacaoPasso.getId());
							acao.setId(id);
							persistenceService.add(acao);
						}
					}
				}
			}			
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
		
		return Response.ok(voPrinc.getId(), MediaType.APPLICATION_JSON).build();
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response delete(@PathParam("id") long id){
		
		try {			
			if (!isExclusaoTransacaoPermitida(id)) {
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
			}
			else {				
				// Removendo grupos associados.
				List<TransacaoGrupo> lstOrigGrupos = transacaoService.findGrupoByIdTransacao(id);
				for (TransacaoGrupo transacaoGrupo : lstOrigGrupos) {
					persistenceService.delete(TransacaoGrupo.class, transacaoGrupo.getId());
				}				
				// Removendo parâmetros associados.	
				List<TransacaoParametro> lstOrigParams = transacaoService.findTransacaoParametroByIdTransacao(id);
				for (TransacaoParametro voOriginal : lstOrigParams) {
					persistenceService.delete(TransacaoParametro.class, voOriginal.getId());
				}				
				// Removendo passos associados.	
				List<TransacaoPasso> lstOrigPassos = transacaoService.findTransacaoPassoByIdTransacao(id);
				for (TransacaoPasso voOriginal : lstOrigPassos) {
					if (isExclusaoTransacaoPassoPermitida(voOriginal.getId())) {
						List<TransacaoPassoEventoNivel> lstOrigEventosNivel = transacaoService.findTransacaoPassoEventoNivelByIdTransacaoPasso(voOriginal.getId());
						List<TransacaoPassoAcao> lstOrigAcoes = transacaoService.findTransacaoPassoAcaoByIdTransacaoPasso(voOriginal.getId());
						// Removendo niveis de eventos associados.
						for (TransacaoPassoEventoNivel transacaoPassoEventoNivelOrig : lstOrigEventosNivel) {
							persistenceService.delete(TransacaoPassoEventoNivel.class, transacaoPassoEventoNivelOrig.getId());
						}
						// Removendo ações associadas.
						for (TransacaoPassoAcao transacaoPassoAcaoOrig : lstOrigAcoes) {
							persistenceService.delete(TransacaoPassoAcao.class, transacaoPassoAcaoOrig.getId());
						}
						// Removendo o registro principal do passo.
						persistenceService.delete(TransacaoPasso.class, voOriginal.getId());
					}
				}
				// Removendo a transação principal.
				persistenceService.delete(Transacao.class, id);				
			}				
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
		
		return Response.ok(MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Método para transferir valores de atributos 
	 * entre duas instâncias da classe TransacaoParametro.
	 * 
	 * @param TransacaoParametro src - Objeto de origem dos dados.
	 * @Param TransacaoParametro dest - Objeto de destino dos dados.
	 */
	private void montarTransacaoParametroVO(TransacaoParametro src, TransacaoParametro dest) {
		dest.setAtivo(src.getAtivo());
		dest.setCaminho(src.getCaminho());
		dest.setMomento(src.getMomento());
		dest.setNamespace(src.getNamespace());
		dest.setNome(src.getNome());
		dest.setPrefixo(src.getPrefixo());
		dest.setEventoTipo(src.getEventoTipo());		
	}
	
	/**
	 * Método para transferir valores de atributos 
	 * entre duas instâncias da classe TransacaoPasso.
	 * 
	 * @param TransacaoPasso src - Objeto de origem dos dados.
	 * @Param TransacaoPasso dest - Objeto de destino dos dados.
	 */
	private void montarTransacaoPassoVO(TransacaoPasso src, TransacaoPasso dest) {
		dest.setCodigo(src.getCodigo());
		dest.setDescricao(src.getDescricao());
		dest.setGravarNaBase(src.getGravarNaBase());
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@POST
	@Path("/filtro/transacaogrupo/")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findTransacaoByGrupos(String json) {
		
		List<Transacao> result = null;
		try {
			List<Grupo> grupos = new ArrayList<Grupo>();
			JSONObject jObject = new JSONObject(json);
			JSONArray gruposArray = jObject.getJSONArray("grupos");
			if (gruposArray.length()>0) {
				for (int i = 0; i < gruposArray.length(); ++i) {
					Grupo grupo = new Grupo();
					JSONObject objJson = gruposArray.getJSONObject(i);
					grupo.setId(objJson.getLong("id"));
					grupos.add(grupo);
			    }
				result = transacaoService.findTransacaoByGrupos(grupos);
				for (Transacao transacao : result) {
					transacao.setParametros(transacaoService.findTransacaoParametroByIdTransacao(transacao.getId()));
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
	
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/grupo/adicionado/{id}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findGrupoByIdTransacao(@PathParam("id") long id) {
		
		List<TransacaoGrupo> result = null;
		try {
			result = transacaoService.findGrupoByIdTransacao(id);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
	
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/checkrule/delete/{id}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response verificarRegraExclusaoTransacao(@PathParam("id") long id) {
		try {
			if (!isExclusaoTransacaoPermitida(id)) {
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		return Response.ok(MediaType.APPLICATION_JSON).build();		
	}
	
	private Boolean isExclusaoTransacaoPermitida(Long id) throws Exception {
		try {
			if (transacaoService.hasTransacaoPassoInstanciaByIdTransacao(id)
					|| transacaoService.hasTransacaoInstanciaByIdTransacao(id)
					|| transacaoService.hasTransacaoPassoAcaoTodoByIdTransacao(id)
					|| transacaoService.hasTransacaoPassoAcaoPendenteByIdTransacao(id)
					|| transacaoService.hasTransacaoPassoAcaoPendenteEventoByIdTransacao(id)
					|| transacaoService.hasTransacaoParametroValorByIdTransacao(id)) {
				return Boolean.FALSE;
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			throw(e);
		}
		return Boolean.TRUE;
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/transacaogrupo/checkrule/delete/{id}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response verificarRegraExclusaoTransacaoGrupo(@PathParam("id") long id) {
		try {
			if (!isExclusaoTransacaoGrupoPermitida(id)) {
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		return Response.ok(MediaType.APPLICATION_JSON).build();		
	}
	
	private Boolean isExclusaoTransacaoGrupoPermitida(Long id) throws Exception {
		try {
			if (transacaoService.hasTransacaoInstanciaByIdTransacaoGrupo(id)) {
				return Boolean.FALSE;
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			throw(e);
		}
		return Boolean.TRUE;
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/transacaopasso/checkrule/delete/{id}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response verificarRegraExclusaoTransacaoPasso(@PathParam("id") long id) {
		try {
			if (!isExclusaoTransacaoPassoPermitida(id)) {
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		return Response.ok(MediaType.APPLICATION_JSON).build();		
	}
	
	private Boolean isExclusaoTransacaoPassoPermitida(Long id) throws Exception {
		try {
			if (transacaoService.hasTransacaoPassoInstanciaByIdTransacaoPasso(id)
					|| transacaoService.hasTransacaoPassoAcaoTodoByIdTransacaoPasso(id)
					|| transacaoService.hasTransacaoPassoAcaoPendenteByIdTransacaoPasso(id)
					|| transacaoService.hasTransacaoPassoAcaoPendenteEventoByIdTransacaoPasso(id)) {
				return Boolean.FALSE;
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			throw(e);
		}
		return Boolean.TRUE;
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/transacaopassoacao/checkrule/delete/{idTransacaoPasso}/{idEventoTipo}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response verificarRegraExclusaoTransacaoPassoAcao(@PathParam("idTransacaoPasso") long idTransacaoPasso, @PathParam("idEventoTipo") String idEventoTipo) {
		try {
			if (!isExclusaoTransacaoPassoAcaoPermitida(idTransacaoPasso, idEventoTipo)) {
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		return Response.ok(MediaType.APPLICATION_JSON).build();		
	}
	
	private Boolean isExclusaoTransacaoPassoAcaoPermitida(Long idTransacaoPasso, String idEventoTipo) throws Exception {
		try {
			if (transacaoService.hasTransacaoPassoAcaoTodoByIdTransacaoPassoAcao(idTransacaoPasso, idEventoTipo)
					|| transacaoService.hasTransacaoPassoAcaoPendenteByIdTransacaoPassoAcao(idTransacaoPasso, idEventoTipo)
					|| transacaoService.hasTransacaoPassoAcaoPendenteEventoByIdTransacaoPassoAcao(idTransacaoPasso, idEventoTipo)) {
				return Boolean.FALSE;
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			throw(e);
		}
		return Boolean.TRUE;
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/transacaoparametro/checkrule/delete/{id}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response verificarRegraExclusaoTransacaoParametro(@PathParam("id") long id) {
		try {
			if (!isExclusaoTransacaoParametroPermitida(id)) {
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		return Response.ok(MediaType.APPLICATION_JSON).build();		
	}
	
	private Boolean isExclusaoTransacaoParametroPermitida(Long id) throws Exception {
		try {
			if (transacaoService.hasTransacaoParametroValorByIdTransacaoParametro(id)) {
				return Boolean.FALSE;
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			throw(e);
		}
		return Boolean.TRUE;
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/checkrule/duplicidade/{codigo}/{idRef}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response verificarRegraDuplicidadeCodigoTransacao(@PathParam("codigo") String codigo, @PathParam("idRef") long idRef) {
		try {
			if (transacaoService.hasTransacaoByCodigo(codigo, idRef)) {
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.duplicidade.transacao.codigo")).build();
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		return Response.ok(MediaType.APPLICATION_JSON).build();		
	}
	
}
