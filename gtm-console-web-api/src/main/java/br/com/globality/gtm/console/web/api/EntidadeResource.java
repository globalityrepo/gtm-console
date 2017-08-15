package br.com.globality.gtm.console.web.api;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
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

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.bytecode.opencsv.CSVReader;
import br.com.globality.gtm.console.model.AcessoDelegado;
import br.com.globality.gtm.console.model.Entidade;
import br.com.globality.gtm.console.model.EntidadeAplicacao;
import br.com.globality.gtm.console.model.Registro;
import br.com.globality.gtm.console.model.Usuario;
import br.com.globality.gtm.console.service.CommonService;
import br.com.globality.gtm.console.service.EntidadeService;
import br.com.globality.gtm.console.service.GenericPersitenceService;
import br.com.globality.gtm.console.util.AppConstants;
import br.com.globality.gtm.console.util.AppUtil;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Endpoint REST para resolver a entidade Entidade.
 * 
 * @author Leonardo Andrade
 *
 */
@Path("/entidade")
public class EntidadeResource {	
	
	@Inject
	private EntidadeService entidadeService;
	
	@Inject
	private CommonService commonService;
	
	@Inject
	private GenericPersitenceService persistenceService;
	
	@Inject
	private Logger log;
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/{pageSize}/{currentPage}/{idUsuario}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findComPaginacao(@PathParam("pageSize") int pageSize, 
			@PathParam("currentPage") int currentPage,
			@PathParam("idUsuario") long idUsuario) {
		
		List<Entidade> result = null;		
		try {
			Usuario usuarioLogado = persistenceService.findById(Usuario.class, idUsuario);
			if (usuarioLogado==null)
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.usuario.credenciais.invalidas")).build();
			result = (List<Entidade>) entidadeService.findByFiltroComPaginacao(null, pageSize, currentPage, usuarioLogado);
			if (result!=null && !result.isEmpty()) 
				result.get(0).setPaginacaoQtdeRegConsulta(entidadeService.countRegistrosByFiltro(null, usuarioLogado));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/{pageSize}/{currentPage}/{idUsuario}/{filtro}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findAllComPaginacao(@PathParam("pageSize") int pageSize, 
			@PathParam("currentPage") int currentPage, 
			@PathParam("idUsuario") long idUsuario,
			@PathParam("filtro") String filtro) {
		
		List<Entidade> result = null;		
		try {
			Usuario usuarioLogado = persistenceService.findById(Usuario.class, idUsuario);
			if (usuarioLogado==null)
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.usuario.credenciais.invalidas")).build();
			result = (List<Entidade>) entidadeService.findByFiltroComPaginacao(filtro, pageSize, currentPage, usuarioLogado);
			if (result!=null && !result.isEmpty()) 
				result.get(0).setPaginacaoQtdeRegConsulta(entidadeService.countRegistrosByFiltro(filtro, usuarioLogado));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@POST
	@Path("/upload/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON})
	public Response uploadFile(MultipartFormDataInput input) {
		List<Registro> result = new ArrayList<Registro>();
		try {
			Map<String, List<InputPart>> formParts = input.getFormDataMap();
			List<InputPart> inPart = formParts.get("file");
			for (InputPart inputPart : inPart) {
				InputStream istream = inputPart.getBody(InputStream.class,null);
				CSVReader reader = new CSVReader(new InputStreamReader(istream), ';');
				String [] nextLine;
			    while ((nextLine = reader.readNext()) != null) {
			    	if (StringUtils.isNotBlank(nextLine[0])
			    			&& StringUtils.isNotBlank(nextLine[1])) {
				    	Registro registro = new Registro();
				    	registro.setCodigo(nextLine[0]);
				    	registro.setDescricao(nextLine[1]);
				    	registro.setHasEdicao(true);
				    	registro.setImportado(true);
				    	result.add(registro);
			    	}
			    }
			    reader.close();
			}
			if (result.isEmpty()) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.importacao.file.empty")).build();
			}
			return Response.ok(result,MediaType.APPLICATION_JSON).build();
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/")
	@POST
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response add(String json){
		
		Entidade voPrinc = null;				
		try {
			voPrinc = new ObjectMapper().readValue(json.getBytes("UTF-8"), Entidade.class);		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		
		try {
			voPrinc.setAtivo(Boolean.TRUE);
			voPrinc.setDataInclusao(new Date());
			persistenceService.add(voPrinc);
			
			// Atualizando código do registro.
			voPrinc.setCodigo(AppUtil.generateCodigo(AppConstants.PREFIXO_REGISTRO_ENTIDADE, voPrinc.getId(), 4));
			persistenceService.update(voPrinc);
			
			// Incluindo Aplicações.
			if (voPrinc.getEntidadeAplicacoes()!=null && !voPrinc.getEntidadeAplicacoes().isEmpty()) {
				for (EntidadeAplicacao voApp : voPrinc.getEntidadeAplicacoes()) {
					incluirEntidadeAplicacao(voApp, voPrinc);
				}
			}
		
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(voPrinc.getId(), MediaType.APPLICATION_JSON).build();
	}
	
	private void incluirEntidadeAplicacao(EntidadeAplicacao entidadeAppParam, Entidade entidadeParam) throws Exception {
		
		// Incluindo dados gerais da aplicação.
		EntidadeAplicacao entidadeAplicacao = new EntidadeAplicacao();
		entidadeAplicacao.setEntidade(entidadeParam);
		entidadeAplicacao.setAtivo(Boolean.TRUE);
		entidadeAplicacao.setAplicacao(entidadeAppParam.getAplicacao());
		entidadeAplicacao.setLiberarAcesso(entidadeAppParam.getLiberarAcesso());
		entidadeAplicacao.setUsuarioInclusao(entidadeParam.getUsuarioInclusao());
		entidadeAplicacao.setDataInclusao(new Date());
		persistenceService.add(entidadeAplicacao);
		
		// Incluindo registros da massa de dados.
		if (entidadeAppParam.getRegistros()!=null && !entidadeAppParam.getRegistros().isEmpty()) {
			for (Registro voRegistroAux : entidadeAppParam.getRegistros()) {
				Registro voRegistro = new Registro();
				voRegistro.setEntidadeAplicacao(entidadeAplicacao);
				voRegistro.setCodigo(voRegistroAux.getCodigo());
				voRegistro.setDescricao(voRegistroAux.getDescricao());
				voRegistro.setUsuarioInclusao(entidadeParam.getUsuarioInclusao());
				voRegistro.setDataInclusao(new Date());
				voRegistro.setAtivo(Boolean.TRUE);
				persistenceService.add(voRegistro);
			}
		}
		
		// Incluindo acessos delegados.
		if (entidadeAppParam.getAcessosDelegados()!=null && !entidadeAppParam.getAcessosDelegados().isEmpty()) {
			for (AcessoDelegado voAcessoDelegadoAux : entidadeAppParam.getAcessosDelegados()) {
				AcessoDelegado voAcessoDelegado = new AcessoDelegado();
				voAcessoDelegado.setUsuario(voAcessoDelegadoAux.getUsuario());
				voAcessoDelegado.setIdReferencia(entidadeAppParam.getId());
				voAcessoDelegado.setRecurso(commonService.findRecursoByCodigo(AppConstants.COD_RECURSO_ENTIDADE));
				voAcessoDelegado.setUsuarioInclusao(entidadeParam.getUsuarioInclusao());
				voAcessoDelegado.setDataInclusao(new Date());
				voAcessoDelegado.setAtivo(Boolean.TRUE);
				persistenceService.add(voAcessoDelegado);
			}
		}
		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response update(String json){
		
		Entidade voPrinc = null;
		Entidade param = null;				
		try {
			param = new ObjectMapper().readValue(json.getBytes("UTF-8"), Entidade.class);			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		
		try {
			voPrinc = persistenceService.findById(Entidade.class, param.getId());
			voPrinc.setDescricao(param.getDescricao());
			voPrinc.setComentario(param.getComentario());
			voPrinc.setDataAlteracao(new Date());
			voPrinc.setUsuarioAlteracao(param.getUsuarioAlteracao());
			persistenceService.update(voPrinc);
			// Tratando aplicações removidas.
			List<EntidadeAplicacao> lstEntidadeAplicacao = entidadeService.findEntidadeAplicacaoyIdEntidade(voPrinc.getId(), null, null);
			boolean achou = false;
			for (EntidadeAplicacao entidadeAplicacaoOrig : lstEntidadeAplicacao) {
				for (EntidadeAplicacao entidadeAplicacao : param.getEntidadeAplicacoes()) {
					if (entidadeAplicacaoOrig.getId().equals(entidadeAplicacao.getId())) {
						achou = true;
						break;
					}
				}
				if (!achou) {
					// Removendo entidade aplicação.
					entidadeAplicacaoOrig.setAtivo(Boolean.FALSE);
					entidadeAplicacaoOrig.setDataAlteracao(new Date());
					entidadeAplicacaoOrig.setUsuarioAlteracao(param.getUsuarioAlteracao());
					persistenceService.update(entidadeAplicacaoOrig);
					// Removendo registros associados.
					List<Registro> registros = entidadeService.findRegistroByIdEntidadeAplicacao(entidadeAplicacaoOrig.getId());
					for (Registro registro : registros) {
						registro.setAtivo(Boolean.FALSE);
						registro.setDataAlteracao(new Date());
						registro.setUsuarioAlteracao(param.getUsuarioAlteracao());
						persistenceService.update(registro);
					}					
					// Removendo acessos delegados.
					List<AcessoDelegado> acessosDelegados = commonService.findAcessosDelegadosByCodRecursoAndIdRef(AppConstants.COD_RECURSO_ENTIDADE, entidadeAplicacaoOrig.getId());
					for (AcessoDelegado acessoDelegado : acessosDelegados) {
						acessoDelegado.setAtivo(Boolean.FALSE);
						acessoDelegado.setDataAlteracao(new Date());
						acessoDelegado.setUsuarioAlteracao(param.getUsuarioAlteracao());
						persistenceService.update(acessoDelegado);
					}
				}
				achou = false;
			}
			for (EntidadeAplicacao entidadeAplicacao : param.getEntidadeAplicacoes()) {
				if (entidadeAplicacao.getId()<0L) {
					// Tratando aplicações adicionadas.
					incluirEntidadeAplicacao(entidadeAplicacao, param);
				} 	
				else {
					// Alterando configurações da entidade aplicação.
					// Tratando registros.
					EntidadeAplicacao voAux = persistenceService.findById(EntidadeAplicacao.class, entidadeAplicacao.getId());
					voAux.setLiberarAcesso(entidadeAplicacao.getLiberarAcesso());
					voAux.setDataAlteracao(new Date());
					voAux.setUsuarioAlteracao(param.getUsuarioAlteracao());
					persistenceService.update(voAux);
					if (entidadeAplicacao.getRegistros()!=null) {
						List<Registro> registrosOrig = entidadeService.findRegistroByIdEntidadeAplicacao(entidadeAplicacao.getId());
						for (Registro registroOrig : registrosOrig) {
							achou = false;
							for (Registro registro : entidadeAplicacao.getRegistros()) {
								if (registro.getId().equals(registroOrig.getId())) {
									if (registro.getHasEdicao()) {
										registroOrig.setCodigo(registro.getCodigo());
										registroOrig.setDescricao(registro.getDescricao());
										registroOrig.setDataAlteracao(new Date());
										registroOrig.setUsuarioAlteracao(param.getUsuarioAlteracao());
										persistenceService.update(registroOrig);
									}
									achou = true;
									break;
								}
							}
							if (!achou) {
								registroOrig.setAtivo(Boolean.FALSE);
								registroOrig.setDataAlteracao(new Date());
								registroOrig.setUsuarioAlteracao(param.getUsuarioAlteracao());
								persistenceService.update(registroOrig);
							}
						}
						for (Registro registro : entidadeAplicacao.getRegistros()) {
							if (registro.getHasEdicao()) {
								if (registro.getId()>0L) {
									registro.setUsuarioAlteracao(param.getUsuarioAlteracao());
									registro.setDataAlteracao(new Date());
									persistenceService.update(registro);
								}
								else {
									Registro voRegistro = new Registro();
									voRegistro.setEntidadeAplicacao(entidadeAplicacao);
									voRegistro.setCodigo(registro.getCodigo());
									voRegistro.setDescricao(registro.getDescricao());
									voRegistro.setUsuarioInclusao(param.getUsuarioAlteracao());
									voRegistro.setDataInclusao(new Date());
									voRegistro.setAtivo(Boolean.TRUE);
									persistenceService.add(voRegistro);
								}
							}
						}
					}
					// Tratando acessos delegados.
					if (entidadeAplicacao.getAcessosDelegados()!=null) {
						List<AcessoDelegado> acessosDelegadosOrig = commonService.findAcessosDelegadosByCodRecursoAndIdRef(AppConstants.COD_RECURSO_ENTIDADE, entidadeAplicacao.getId());
						for (AcessoDelegado acessoDelegadoOrig : acessosDelegadosOrig) {
							achou = false;
							for (AcessoDelegado acessoDelegado : entidadeAplicacao.getAcessosDelegados()) {
								if (acessoDelegado.getId().equals(acessoDelegadoOrig.getId())) {
									achou = true;
									break;
								}
							}
							if (!achou) {
								acessoDelegadoOrig.setAtivo(Boolean.FALSE);
								acessoDelegadoOrig.setDataAlteracao(new Date());
								acessoDelegadoOrig.setUsuarioAlteracao(param.getUsuarioAlteracao());
								persistenceService.update(acessoDelegadoOrig);
							}
						}
						for (AcessoDelegado acessoDelegado : entidadeAplicacao.getAcessosDelegados()) {
							if (acessoDelegado.getId()<0L) {
								AcessoDelegado voAcessoDelegado = new AcessoDelegado();
								voAcessoDelegado.setUsuario(acessoDelegado.getUsuario());
								voAcessoDelegado.setIdReferencia(entidadeAplicacao.getId());
								voAcessoDelegado.setRecurso(commonService.findRecursoByCodigo(AppConstants.COD_RECURSO_ENTIDADE));
								voAcessoDelegado.setUsuarioInclusao(param.getUsuarioAlteracao());
								voAcessoDelegado.setDataInclusao(new Date());
								voAcessoDelegado.setAtivo(Boolean.TRUE);
								persistenceService.add(voAcessoDelegado);
							}
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
	@Path("/{id}/{idUsuario}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response delete(@PathParam("id") long id, @PathParam("idUsuario") long idUsuario){
		
		try {			
			if (entidadeService.hasAssociacaoDeParaAByIdEntidade(id)
					|| entidadeService.hasAssociacaoDeParaBByIdEntidade(id)) {
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
			}
			else {	
				// Removendo o registro principal.
				Usuario usuarioLogado = persistenceService.findById(Usuario.class, idUsuario);
				if (usuarioLogado==null)
					return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.usuario.credenciais.invalidas")).build();
				Entidade voPrinc = persistenceService.findById(Entidade.class, id);
				voPrinc.setAtivo(Boolean.FALSE);
				voPrinc.setDataAlteracao(new Date());
				voPrinc.setUsuarioAlteracao(usuarioLogado);
				persistenceService.update(voPrinc);	
				
				// Removendo aplicações associadas.
				List<EntidadeAplicacao> lstEntidadeAplicacao = entidadeService.findEntidadeAplicacaoyIdEntidade(voPrinc.getId(), null, null);
				for (EntidadeAplicacao entidadeAplicacao : lstEntidadeAplicacao) {
					entidadeAplicacao.setDataAlteracao(new Date());
					entidadeAplicacao.setUsuarioAlteracao(usuarioLogado);
					persistenceService.update(entidadeAplicacao);		
					// Removendo registros associados.
					List<Registro> registros = entidadeService.findRegistroByIdEntidadeAplicacao(entidadeAplicacao.getId());
					for (Registro registro : registros) {
						registro.setAtivo(Boolean.FALSE);
						registro.setDataAlteracao(new Date());
						registro.setUsuarioAlteracao(usuarioLogado);
						persistenceService.update(registro);
					}					
					// Removendo acessos delegados.
					List<AcessoDelegado> acessosDelegados = commonService.findAcessosDelegadosByCodRecursoAndIdRef(AppConstants.COD_RECURSO_ENTIDADE, entidadeAplicacao.getId());
					for (AcessoDelegado acessoDelegado : acessosDelegados) {
						acessoDelegado.setAtivo(Boolean.FALSE);
						acessoDelegado.setDataAlteracao(new Date());
						acessoDelegado.setUsuarioAlteracao(usuarioLogado);
						persistenceService.update(acessoDelegado);
					}
				}
			}				
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(MediaType.APPLICATION_JSON).build();
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/aplicacoes/{idEntidade}/{idUsuario}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findEntidadeAplicacaoyIdEntidade(@PathParam("idEntidade") Long idEntidade,
			@PathParam("idUsuario") Long idUsuario) {
		
		List<EntidadeAplicacao> result = null;		
		try {
			Usuario usuarioLogado = persistenceService.findById(Usuario.class, idUsuario);
			if (usuarioLogado==null)
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.usuario.credenciais.invalidas")).build();
			result = entidadeService.findEntidadeAplicacaoyIdEntidade(idEntidade, null, usuarioLogado);
			for (EntidadeAplicacao vo : result) {
				vo.setConfigurado(true);
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
	@Path("/aplicacoes/{idEntidade}/")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findAllEntidadeAplicacaoyIdEntidade(@PathParam("idEntidade") Long idEntidade) {
		
		List<EntidadeAplicacao> result = null;		
		try {
			result = entidadeService.findEntidadeAplicacaoyIdEntidade(idEntidade, null, null);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/aplicacao/registros/{idEntidadeAplicacao}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findRegistroByEntidadeAplicacao(@PathParam("idEntidadeAplicacao") Long idEntidadeAplicacao) {
		
		List<Registro> result = null;		
		try {
			result = entidadeService.findRegistroByIdEntidadeAplicacao(idEntidadeAplicacao);
			for (Registro registro : result) {
				registro.setCodigoOriginal(registro.getCodigo());
				registro.setDescricaoOriginal(registro.getDescricao());
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
	@Path("/checkrule/delete/registro/{id}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response verificarRegraExclusaoRegistro(@PathParam("id") long id) {
		try {
			if (entidadeService.hasAssociacaoDeParaByIdRegistro(id)) {
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		return Response.ok(MediaType.APPLICATION_JSON).build();		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/checkrule/delete/entidadeaplicacao/{id}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response verificarRegraExclusaoEntidadeAplicacao(@PathParam("id") long id) {
		try {
			if (entidadeService.hasAssociacaoDeParaByIdEntidadeAplicacao(id)) {
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.possui.restricao.exclusao")).build();
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		return Response.ok(MediaType.APPLICATION_JSON).build();		
	}
	
}
