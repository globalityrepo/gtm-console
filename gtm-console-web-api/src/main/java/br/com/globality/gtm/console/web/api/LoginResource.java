package br.com.globality.gtm.console.web.api;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.resource.ResourceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.globality.gtm.console.model.Usuario;
import br.com.globality.gtm.console.service.UsuarioService;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Endpoint RESTfull para o Controle de Acesso.
 *
 */
@RequestScoped
@Path("/login")
public class LoginResource {

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private Logger log;
	
	@PermitAll
	@POST
	@Path("/")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response autenticarUsuario(String json) throws ResourceException {
		Usuario param = null;
		Usuario usuarioLogado = null;
		try {
			param = new ObjectMapper().readValue(json.getBytes(), Usuario.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.parametro.invaldo")).build();
		}
		try {
			usuarioLogado = usuarioService.efetuarLogin(param.getCodigo(), param.getSenha());
			if (usuarioLogado == null)
				return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.login.autenticacao.invalida")).build();
			else if (usuarioLogado.getBloqueado())
				return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.login.autenticacao.usuario.bloqueado")).build();
			return Response.ok(usuarioLogado, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
					
		}
	}
	
}
