package br.com.globality.gtm.console.web.api;

import javax.annotation.security.PermitAll;
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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import br.com.globality.gtm.console.model.Usuario;
import br.com.globality.gtm.console.service.GenericPersitenceService;
import br.com.globality.gtm.console.service.UsuarioService;
import br.com.globality.gtm.console.util.AppUtil;
import br.com.globality.gtm.console.util.EmailManager;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Endpoint RESTfull para a funcionalidade Esqueci Minha Senha.
 *
 */
@RequestScoped
@Path("/esquecisenha")
public class EsqueciSenhaResource {

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private GenericPersitenceService persistenceService;
	
	@Inject 
	private EmailManager emailManager;
	
	@Inject
	private Logger log;
	
	@PermitAll
	@POST
	@Path("/")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response executar(@FormParam("login") String login) throws ResourceException {
		
		Usuario usuario = null;
		try {
			// Recuperando o usuário..
			usuario = usuarioService.findUsuarioByCodigoAcesso(login);
			if (usuario==null) 
				return Response.status(Response.Status.NOT_FOUND).entity(
						MessageBundle.getString("msg.erro.codigo.invalido")).build();
			if (StringUtils.isBlank(usuario.getEmail())) 
				return Response.status(Response.Status.NOT_FOUND).entity(
						MessageBundle.getString("msg.erro.email.nao.encontrado")).build();
			// Gerar nova senha.
			String novaSenha = AppUtil.gerarSenha(4);
			usuario.setSenha(AppUtil.encodeBase64(novaSenha));
			// Enviar e-mail
			emailManager.sendMail(usuario.getEmail(), MessageBundle.getString("msg.email.manager.aviso.senha.assunto"), 
					MessageBundle.getString("msg.email.manager.aviso.senha.corpo", usuario.getNome(), novaSenha));
			// Gravar nova senha
			persistenceService.update(usuario);						
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}		
		
		return Response.ok(usuario, MediaType.APPLICATION_JSON).build();		
	
	}

}
