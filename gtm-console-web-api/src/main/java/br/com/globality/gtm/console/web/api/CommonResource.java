package br.com.globality.gtm.console.web.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import br.com.globality.gtm.console.model.AcessoDelegado;
import br.com.globality.gtm.console.service.CommonService;
import br.com.globality.gtm.console.util.MessageBundle;

/**
 * Endpoint REST serviços comuns as funcionalidades do sistema.
 * 
 * @author Leonardo Andrade
 *
 */
@Path("/common")
public class CommonResource {	
	
	@Inject
	private Logger log;
	
	@Inject
	private CommonService commonService;
	
	@Context
	private ServletContext context;
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/download/external/{filename}")
	@Produces("text/plain")
	public Response downloadFileFromExternalFolder(@PathParam("filename") String filename) {
		BufferedWriter bw = null;
		try {
			File file = FileUtils.getFile(context.getRealPath("/external/") + "\\" + filename);
			if (file!=null) {
				ResponseBuilder response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				return response.build();
			}
			else {
				return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.arquivo.nao.encontrado")).build();
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		finally {
			try {
				if (bw!=null) {
					bw.close();
				}
			}
			catch (IOException e) {
				log.error(e.getMessage(), e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
			}
		}		
	}
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/acessosdelegados/{codigoRecurso}/{idRef}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response findAcessosDelegadosByIdRecursoAndIdRef(@PathParam("codigoRecurso") String codigoRecurso, @PathParam("idRef") Long idRef) {
		
		List<AcessoDelegado> result = null;		
		try {
			result = commonService.findAcessosDelegadosByCodRecursoAndIdRef(codigoRecurso, idRef);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		return Response.ok(result,MediaType.APPLICATION_JSON).build();
		
	}	
	
}
