package br.com.globality.gtm.console.web.api;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.resource.ResourceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import br.com.globality.gtm.console.model.Usuario;
import br.com.globality.gtm.console.service.GenericPersitenceService;
import br.com.globality.gtm.console.service.UsuarioService;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Endpoint RESTfull para a Troca de Senha.
 *
 */
@RequestScoped
@Path("/trocasenha")
public class TrocaSenhaResource {

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private GenericPersitenceService persistenceService;
	
	@Inject
	private Logger log;
	
	@RolesAllowed("APP_USER_ROLE")
	@POST
	@Path("/")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response executar(@FormParam("login") String login, 
			@FormParam("senhaAtual") String senhaAtual, 
			@FormParam("senhaNova") String senhaNova) throws ResourceException {
		
		Usuario usuario = null;
		try {
			// Verificando informações de autenticação do usuário.
			usuario = usuarioService.efetuarLogin(login, senhaAtual);
			if (usuario==null) 
				return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.erro.senha.atual.invalida")).build();
			// Efetuando o update da senha na base.
			usuario.setSenha(senhaNova);
			persistenceService.update(usuario);			
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(usuario, MediaType.APPLICATION_JSON).build();
	
	}

}
