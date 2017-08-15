package br.com.globality.gtm.console.web.api;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.resource.ResourceException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import br.com.globality.gtm.console.model.PerfilRecursoPermissao;
import br.com.globality.gtm.console.model.Recurso;
import br.com.globality.gtm.console.service.UsuarioService;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Endpoint RESTfull para o controle de autorizações de acesso.
 *
 */
@RequestScoped
@Path("/autorizacao")
public class AutorizacaoResource {

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private Logger log;

	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/perfil/{idPerfil}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response listarPermissoesByPerfil(@PathParam("idPerfil") Long idPerfil) throws ResourceException {
		try {
			List<Recurso> recursosPermitidos = usuarioService.findRecursosPermitidosByIdPerfil(idPerfil);
			if (recursosPermitidos == null || recursosPermitidos.isEmpty())
				return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.login.permissoes.acesso.empty")).build();
			return Response.ok(recursosPermitidos, MediaType.APPLICATION_JSON).build();
		} 
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/perfil/{idPerfil}/recurso/{codRecurso}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response listarPermissoesByPerfilRecurso(@PathParam("idPerfil") Long idPerfil, @PathParam("codRecurso") String codRecurso) throws ResourceException {
		try {
			List<PerfilRecursoPermissao> permissoes = usuarioService.findPermissoesByIdPerfilAndCodRecurso(idPerfil, codRecurso);
			if (permissoes == null || permissoes.isEmpty())
				return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.recurso.permissoes.acesso.empty")).build();
			return Response.ok(permissoes, MediaType.APPLICATION_JSON).build();
		} 
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
	}
	
}
