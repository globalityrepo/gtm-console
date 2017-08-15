package br.com.globality.gtm.console.web.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
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

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.globality.gtm.console.model.Grupo;
import br.com.globality.gtm.console.model.Usuario;
import br.com.globality.gtm.console.model.UsuarioGrupo;
import br.com.globality.gtm.console.model.compositeId.UsuarioGrupoCompositeId;
import br.com.globality.gtm.console.service.GenericPersitenceService;
import br.com.globality.gtm.console.service.UsuarioService;
import br.com.globality.gtm.console.util.AppUtil;
import br.com.globality.gtm.console.util.EmailManager;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Endpoint REST para resolver a entidade Usuario
 * 
 * @author Leonardo Andrade
 *
 */
@Path("/usuario")
public class UsuarioResource {	
	
	@Inject
	private GenericPersitenceService persistenceService;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject 
	private EmailManager emailManager;
	
	@Inject
	private Logger log;
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findAll() {
		
		List<Usuario> result = null;		
		try {
			result = persistenceService.findAll(Usuario.class, true);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
	
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/{pageSize}/{currentPage}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findAllComPaginacao(@PathParam("pageSize") int pageSize, @PathParam("currentPage") int currentPage) {
		
		List<Usuario> result = null;		
		try {
			result = (List<Usuario>) persistenceService.findByFiltroComPaginacao(Usuario.class, null, pageSize, currentPage, true);
			if (result!=null && !result.isEmpty()) 
				result.get(0).setPaginacaoQtdeRegConsulta(persistenceService.countRegistrosByFiltro(Usuario.class, null, true));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}		
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
	
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/{pageSize}/{currentPage}/{filtro}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findAllComPaginacao(@PathParam("pageSize") int pageSize, 
			@PathParam("currentPage") int currentPage, 
			@PathParam("filtro") String filtro) {
		
		List<Usuario> result = null;		
		try {
			result = (List<Usuario>) persistenceService.findByFiltroComPaginacao(Usuario.class, filtro, pageSize, currentPage, true);
			if (result!=null && !result.isEmpty()) 
				result.get(0).setPaginacaoQtdeRegConsulta(persistenceService.countRegistrosByFiltro(Usuario.class, filtro, true));
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
		
		Usuario usuario = null;
		try {
			usuario  = new ObjectMapper().readValue(json.getBytes("UTF-8"), Usuario.class);			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		
		Usuario result = null;		
		try {
			// Gerar nova senha.
			String novaSenha = AppUtil.gerarSenha(4);
			usuario.setSenha(AppUtil.encodeBase64(novaSenha));			
			// Gravar dados do novo usuário.
			usuario.setAtivo(Boolean.TRUE);
			usuario.setBloqueado(Boolean.FALSE);
			result = persistenceService.add(usuario);		
			// Incluindo grupos.
			for (Grupo grupo : usuario.getGrupos()) {
				UsuarioGrupo item = new UsuarioGrupo();
				UsuarioGrupoCompositeId id = new UsuarioGrupoCompositeId();
				id.setIdUsuario(result.getId());
				id.setIdGrupo(grupo.getId());
				item.setId(id);
				persistenceService.add(item);
			}
			// Enviar e-mail
			emailManager.sendMail(usuario.getEmail(), MessageBundle.getString("msg.cadastro.usuario.assunto"), MessageBundle.getString("msg.email.manager.aviso.cadastro.usuario.corpo", usuario.getNome(), usuario.getCodigo() , novaSenha));
			usuario.setSenha(null);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response update(String json){
		
		Usuario usuarioParam = null;
		try {
			usuarioParam  = new ObjectMapper().readValue(json.getBytes("UTF-8"), Usuario.class);
		} 
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.conversao.objeto")).build();
		} 
		
		Usuario vo = null;		
		try {
			vo = persistenceService.findById(Usuario.class, usuarioParam.getId());
			if (vo!=null) {
				// Montando o objeto VO.
				vo.setCodigo(usuarioParam.getCodigo());
				vo.setNome(usuarioParam.getNome());
				vo.setEmail(usuarioParam.getEmail());
				vo.setPerfil(usuarioParam.getPerfil());
				vo.setAdmDePara(usuarioParam.getAdmDePara());
				vo.setBloqueado(usuarioParam.getBloqueado());
				vo = persistenceService.update(vo);
				// Removendo grupos legados.
				List<UsuarioGrupo> lstAux01 = usuarioService.findUsuarioGrupoByIdUsuario(vo.getId());
				for (UsuarioGrupo item : lstAux01) {
					persistenceService.delete(UsuarioGrupo.class, item.getId());
				}
				// Incluindo grupos.
				for (Grupo grupo : usuarioParam.getGrupos()) {
					UsuarioGrupo item = new UsuarioGrupo();
					UsuarioGrupoCompositeId id = new UsuarioGrupoCompositeId();
					id.setIdUsuario(vo.getId());
					id.setIdGrupo(grupo.getId());
					item.setId(id);
					persistenceService.add(item);
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(vo, MediaType.APPLICATION_JSON).build();
	
	}
		
	@RolesAllowed("APP_USER_ROLE")
	@Path("/{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response delete(@PathParam("id") long id) {
		
		try {
			Usuario usuario = persistenceService.findById(Usuario.class, id);
			usuario.setAtivo(false);
			persistenceService.update(usuario);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}				
		
		return Response.ok(MediaType.APPLICATION_JSON).build();
	
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/grupo/adicionado/{id}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findGrupoByIdUsuario(@PathParam("id") long id) {
		
		List<UsuarioGrupo> lstUsuarioGrupo = null;
		List<Grupo> result = new ArrayList<Grupo>();
		try {
			lstUsuarioGrupo = usuarioService.findUsuarioGrupoByIdUsuario(id);
			for (UsuarioGrupo vo : lstUsuarioGrupo) {
				result.add(vo.getGrupo());
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
	@Path("/grupo/pendente/{id}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findGrupoPendenteByIdUsuario(@PathParam("id") long id) {
		
		List<Grupo> result = new ArrayList<Grupo>();
		try {
			List<Grupo> lstGrupo = persistenceService.findAll(Grupo.class);	
			List<UsuarioGrupo> lstUsuarioGrupo = usuarioService.findUsuarioGrupoByIdUsuario(id);		
			boolean estaContido = false;
			for (Grupo grupo : lstGrupo) {
				estaContido = false;
				for (UsuarioGrupo usuarioGrupo : lstUsuarioGrupo) {
					if (usuarioGrupo.getGrupo().getId().equals(grupo.getId())) {
						estaContido = true;
						break;
					}
				}
				if (!estaContido) 
					result.add(grupo);
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
	@Path("/checkrole/admcrossreference/{id}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response isUsuarioAdnCrossReference(@PathParam("id") long id) {
		boolean result = false;
		try {
			Usuario usuario = persistenceService.findById(Usuario.class, id);
			result = usuario.getAdmDePara();
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
	}
	
}
