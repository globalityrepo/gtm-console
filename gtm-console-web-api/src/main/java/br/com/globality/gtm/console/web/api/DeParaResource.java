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
import br.com.globality.gtm.console.model.EntidadeAplicacao;
import br.com.globality.gtm.console.model.EntidadeAplicacaoDePara;
import br.com.globality.gtm.console.model.Registro;
import br.com.globality.gtm.console.model.RegistroDePara;
import br.com.globality.gtm.console.model.Usuario;
import br.com.globality.gtm.console.service.CommonService;
import br.com.globality.gtm.console.service.DeParaService;
import br.com.globality.gtm.console.service.EntidadeService;
import br.com.globality.gtm.console.service.GenericPersitenceService;
import br.com.globality.gtm.console.util.AppConstants;
import br.com.globality.gtm.console.util.AppUtil;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Endpoint REST para resolver a funcionalidade de De Para.
 * 
 * @author Leonardo Andrade
 *
 */
@Path("/depara")
public class DeParaResource {	
	
	@Inject
	private DeParaService deParaService;
	
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
		
		List<EntidadeAplicacaoDePara> result = null;		
		try {
			Usuario usuarioLogado = persistenceService.findById(Usuario.class, idUsuario);
			if (usuarioLogado==null)
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.usuario.credenciais.invalidas")).build();
			result = (List<EntidadeAplicacaoDePara>) deParaService.findByFiltroComPaginacao(null, pageSize, currentPage, usuarioLogado);
			if (result!=null && !result.isEmpty()) 
				result.get(0).setPaginacaoQtdeRegConsulta(deParaService.countRegistrosByFiltro(null, usuarioLogado));
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
		
		List<EntidadeAplicacaoDePara> result = null;		
		try {
			Usuario usuarioLogado = persistenceService.findById(Usuario.class, idUsuario);
			if (usuarioLogado==null)
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.usuario.credenciais.invalidas")).build();
			result = (List<EntidadeAplicacaoDePara>) deParaService.findByFiltroComPaginacao(filtro, pageSize, currentPage, usuarioLogado);
			if (result!=null && !result.isEmpty()) 
				result.get(0).setPaginacaoQtdeRegConsulta(deParaService.countRegistrosByFiltro(null, usuarioLogado));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@POST
	@Path("/upload/{idEntidadeAplicacaoA}/{idEntidadeAplicacaoB}/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON})
	public Response uploadFileValidate(MultipartFormDataInput input,
			@PathParam("idEntidadeAplicacaoA") long idEntidadeAplicacaoA, 
			@PathParam("idEntidadeAplicacaoB") long idEntidadeAplicacaoB) {
		List<RegistroDePara> result = new ArrayList<RegistroDePara>();
		try {
			Map<String, List<InputPart>> formParts = input.getFormDataMap();
			List<InputPart> inPart = formParts.get("file");
			for (InputPart inputPart : inPart) {
				InputStream istream = inputPart.getBody(InputStream.class,null);
				CSVReader reader = new CSVReader(new InputStreamReader(istream), ';');
				String [] nextLine;
			    String paramA = "";
			    String paramB = "";
				while ((nextLine = reader.readNext()) != null) {
					if (StringUtils.isNotBlank(nextLine[0])
			    			&& StringUtils.isNotBlank(nextLine[1])) {
						paramA = nextLine[0];
				    	paramB = nextLine[1];
				    	RegistroDePara registroDePara = new RegistroDePara();
					    registroDePara.setRegistroDe(deParaService.findRegistroEntidadeAplicacaoByCodigo(idEntidadeAplicacaoA, paramA));
				    	registroDePara.setRegistroPara(deParaService.findRegistroEntidadeAplicacaoByCodigo(idEntidadeAplicacaoB, paramB));
				    	registroDePara.setImportado(true);
				    	if (registroDePara.getRegistroDe()!=null && registroDePara.getRegistroPara()!=null) {
				    		result.add(registroDePara);
				    	}
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
	@GET
	@Path("/aplicacoes/{idEntidade}/exceto/{idEntidadeAplicacaoA}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findEntidadeAplicacaoBByIdEntidadeExcetoIdAplicacaoA(@PathParam("idEntidade") Long idEntidade, @PathParam("idEntidadeAplicacaoA") Long idEntidadeAplicacaoA) {
		
		List<EntidadeAplicacao> result = null;		
		try {
			result = entidadeService.findEntidadeAplicacaoyIdEntidade(idEntidade, idEntidadeAplicacaoA, null);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/aplicacao/a/registros/{idEntidadeAplicacao}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findRegistroByIdEntidadeAplicacaoA(@PathParam("idEntidadeAplicacao") Long idEntidadeAplicacao) {
		
		List<Registro> result = null;		
		try {
			result = entidadeService.findRegistroByIdEntidadeAplicacao(idEntidadeAplicacao);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/aplicacao/b/registros/{idEntidadeAplicacao}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findRegistroAByIdEntidadeAplicacaoB(@PathParam("idEntidadeAplicacao") Long idEntidadeAplicacao) {
		
		List<Registro> result = null;		
		try {
			result = entidadeService.findRegistroByIdEntidadeAplicacao(idEntidadeAplicacao);
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
		
		EntidadeAplicacaoDePara voPrinc = null;				
		try {
			voPrinc = new ObjectMapper().readValue(json.getBytes("UTF-8"), EntidadeAplicacaoDePara.class);		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		
		try {
			voPrinc.setId(null);
			voPrinc.setAtivo(Boolean.TRUE);
			voPrinc.setDataInclusao(new Date());
			persistenceService.add(voPrinc);
			
			// Atualizando código do registro.
			voPrinc.setCodigo(AppUtil.generateCodigo(AppConstants.PREFIXO_REGISTRO_DEPARA, voPrinc.getId(), 4));
			persistenceService.update(voPrinc);
			
			// Incluindo Registros.
			if (voPrinc.getRegistrosDePara()!=null && !voPrinc.getRegistrosDePara().isEmpty()) {
				for (RegistroDePara obj : voPrinc.getRegistrosDePara()) {
					RegistroDePara voRegistro = new RegistroDePara();
					voRegistro.setEntidadeAplicacaoDePara(voPrinc);
					voRegistro.setRegistroDe(obj.getRegistroDe());
					voRegistro.setRegistroPara(obj.getRegistroPara());
					voRegistro.setUsuarioInclusao(voPrinc.getUsuarioInclusao());
					voRegistro.setDataInclusao(new Date());
					voRegistro.setAtivo(Boolean.TRUE);
					persistenceService.add(voRegistro);
				}
			}
			
			// Incluindo acessos delegados.
			if (voPrinc.getAcessosDelegados()!=null && !voPrinc.getAcessosDelegados().isEmpty()) {
				for (AcessoDelegado voAcessoDelegadoAux : voPrinc.getAcessosDelegados()) {
					AcessoDelegado voAcessoDelegado = new AcessoDelegado();
					voAcessoDelegado.setUsuario(voAcessoDelegadoAux.getUsuario());
					voAcessoDelegado.setIdReferencia(voPrinc.getId());
					voAcessoDelegado.setRecurso(commonService.findRecursoByCodigo(AppConstants.COD_RECURSO_DEPARA));
					voAcessoDelegado.setUsuarioInclusao(voPrinc.getUsuarioInclusao());
					voAcessoDelegado.setDataInclusao(new Date());
					voAcessoDelegado.setAtivo(Boolean.TRUE);
					persistenceService.add(voAcessoDelegado);
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
		
		EntidadeAplicacaoDePara voPrinc = null;
		EntidadeAplicacaoDePara param = null;				
		try {
			param = new ObjectMapper().readValue(json.getBytes("UTF-8"), EntidadeAplicacaoDePara.class);			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		
		try {
			voPrinc = persistenceService.findById(EntidadeAplicacaoDePara.class, param.getId());
			voPrinc.setEntidadeAplicacaoDe(param.getEntidadeAplicacaoDe());
			voPrinc.setEntidadeAplicacaoPara(param.getEntidadeAplicacaoPara());
			voPrinc.setLiberarAcesso(param.getLiberarAcesso());
			voPrinc.setUsuarioAlteracao(param.getUsuarioAlteracao());
			voPrinc.setDataAlteracao(new Date());
			persistenceService.update(voPrinc);
			
			// Tratando registros removidos.
			List<RegistroDePara> lstRegistrosDeParaOrig = deParaService.findRegistroDeParaIdEntidadeAplicacaoDePara(voPrinc.getId());
			boolean achou = false;
			for (RegistroDePara registroDeParaOrig : lstRegistrosDeParaOrig) {
				for (RegistroDePara registroDePara : param.getRegistrosDePara()) {
					if (registroDeParaOrig.getId().equals(registroDePara.getId())) {
						achou = true;
						break;
					}
				}
				if (!achou) {
					// Removendo registro de De-Para.
					registroDeParaOrig.setAtivo(Boolean.FALSE);
					registroDeParaOrig.setDataAlteracao(new Date());
					registroDeParaOrig.setUsuarioAlteracao(param.getUsuarioAlteracao());
					persistenceService.update(registroDeParaOrig);
				}
				achou = false;
			}
			
			// Incluindo novos registros de De-Para.
			for (RegistroDePara registroDePara : param.getRegistrosDePara()) {
				if (registroDePara.getId()<0L) {
					RegistroDePara voRegistro = new RegistroDePara();
					voRegistro.setEntidadeAplicacaoDePara(voPrinc);
					voRegistro.setRegistroDe(registroDePara.getRegistroDe());
					voRegistro.setRegistroPara(registroDePara.getRegistroPara());
					voRegistro.setUsuarioInclusao(voPrinc.getUsuarioInclusao());
					voRegistro.setDataInclusao(new Date());
					voRegistro.setAtivo(Boolean.TRUE);
					persistenceService.add(voRegistro);
				} 	
			}
					
			// Tratando acessos delegados.
			if (param.getAcessosDelegados()!=null) {
				List<AcessoDelegado> acessosDelegadosOrig = commonService.findAcessosDelegadosByCodRecursoAndIdRef(AppConstants.COD_RECURSO_DEPARA, voPrinc.getId());
				for (AcessoDelegado acessoDelegadoOrig : acessosDelegadosOrig) {
					achou = false;
					for (AcessoDelegado acessoDelegado : param.getAcessosDelegados()) {
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
				for (AcessoDelegado acessoDelegado : param.getAcessosDelegados()) {
					if (acessoDelegado.getId()<0L) {
						AcessoDelegado voAcessoDelegado = new AcessoDelegado();
						voAcessoDelegado.setUsuario(acessoDelegado.getUsuario());
						voAcessoDelegado.setIdReferencia(voPrinc.getId());
						voAcessoDelegado.setRecurso(commonService.findRecursoByCodigo(AppConstants.COD_RECURSO_DEPARA));
						voAcessoDelegado.setUsuarioInclusao(param.getUsuarioAlteracao());
						voAcessoDelegado.setDataInclusao(new Date());
						voAcessoDelegado.setAtivo(Boolean.TRUE);
						persistenceService.add(voAcessoDelegado);
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
			// Validando usuario logado.
			Usuario usuarioLogado = persistenceService.findById(Usuario.class, idUsuario);
			if (usuarioLogado==null)
				return Response.status(Response.Status.FORBIDDEN).entity(MessageBundle.getString("msg.erro.usuario.credenciais.invalidas")).build();
			
			// Removendo o registro principal.
			EntidadeAplicacaoDePara voPrinc = persistenceService.findById(EntidadeAplicacaoDePara.class, id);
			voPrinc.setAtivo(Boolean.FALSE);
			voPrinc.setDataAlteracao(new Date());
			voPrinc.setUsuarioAlteracao(usuarioLogado);
			persistenceService.update(voPrinc);	
			
			// Removendo registros de De-Para associados.
			List<RegistroDePara> lstRegistrosDePara = deParaService.findRegistroDeParaIdEntidadeAplicacaoDePara(voPrinc.getId());
			for (RegistroDePara registroDePara : lstRegistrosDePara) {
				registroDePara.setAtivo(Boolean.FALSE);
				registroDePara.setDataAlteracao(new Date());
				registroDePara.setUsuarioAlteracao(usuarioLogado);
				persistenceService.update(registroDePara);		
			}
			
			// Removendo acessos delegados.
			List<AcessoDelegado> acessosDelegados = commonService.findAcessosDelegadosByCodRecursoAndIdRef(AppConstants.COD_RECURSO_DEPARA, voPrinc.getId());
			for (AcessoDelegado acessoDelegado : acessosDelegados) {
				acessoDelegado.setAtivo(Boolean.FALSE);
				acessoDelegado.setDataAlteracao(new Date());
				acessoDelegado.setUsuarioAlteracao(usuarioLogado);
				persistenceService.update(acessoDelegado);
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
	@Path("/checkrule/associacao/entidadeaplicacao/{idEntidadeAplicacaoA}/{idEntidadeAplicacaoB}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response verificarRegraExclusaoRegistro(@PathParam("idEntidadeAplicacaoA") long idEntidadeAplicacaoA,
			@PathParam("idEntidadeAplicacaoB") long idEntidadeAplicacaoB) {
		try {
			if (entidadeService.hasAssociacaoDeParaByIdEntidadeAplicacaoAAndIdEntidadeAplicacaoB(idEntidadeAplicacaoA, idEntidadeAplicacaoB)) {
				return Response.status(Response.Status.FORBIDDEN).build();
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
	@Path("/registros/{idEntidadeAplicacaoDePara}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findRegistrosByIdEntidadeAplicacaoDePara(@PathParam("idEntidadeAplicacaoDePara") Long idEntidadeAplicacaoDePara) {
		
		List<RegistroDePara> result = null;		
		try {
			result = deParaService.findRegistrosByIdEntidadeAplicacaoDePara(idEntidadeAplicacaoDePara);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}
	
}
