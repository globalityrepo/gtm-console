package br.com.globality.gtm.rest;

import javax.inject.Inject;
import javax.resource.ResourceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;

import br.com.globality.gtm.model.Usuario;
import br.com.globality.gtm.service.UsuarioService;

/**
 * Endpoint RESTfull para o Controle de Acesso.
 *
 */
@Path("/controleacesso")
@Produces(MediaType.APPLICATION_JSON)
public class ControleAcessoResource {
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private Logger log;
	
	@POST
	@Path("/login")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response autenticarUsuario(String json) throws ResourceException {
		Usuario param  = null;
		Usuario result = null;
		try {
			param = new ObjectMapper().readValue(json.getBytes(), Usuario.class);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Não foi possível converter o parametro para um objeto válido").build();
		} 
		try {
			result = usuarioService.efetuarLogin(param.getCodigo(), param.getSenha());
			if(result == null)
				return Response.status(Response.Status.NOT_FOUND).entity("Usuário ou senha inválidos.").build();
			result.setSenha(null);
			return Response.ok(result,MediaType.APPLICATION_JSON).build();
		}
		catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Ocorreu um erro inesperado. Tente novamente.").build();
		}
	}
	
}
