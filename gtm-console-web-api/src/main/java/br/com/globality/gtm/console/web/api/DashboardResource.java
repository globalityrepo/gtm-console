package br.com.globality.gtm.console.web.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import br.com.globality.gtm.console.model.EventoInstanciaConteudo;
import br.com.globality.gtm.console.model.EventoInstanciaDashboard;
import br.com.globality.gtm.console.model.EventoNivel;
import br.com.globality.gtm.console.model.Grupo;
import br.com.globality.gtm.console.model.Transacao;
import br.com.globality.gtm.console.service.GenericPersitenceService;
import br.com.globality.gtm.console.service.TransacaoService;
import br.com.globality.gtm.console.util.AppConstants;
import br.com.globality.gtm.console.util.MessageBundle;
import br.com.globality.gtm.console.web.api.util.DashboardTreeItem;
import br.com.globality.gtm.console.web.api.util.DashboardUtil;
import br.com.globality.gtm.console.web.api.util.EventoInstanciaStatusEnum;
import br.com.globality.gtm.console.web.api.util.TransacaoInstanciaStatusEnum;

/**
 * Endpoint REST de serviços específicos do dashboard.
 * 
 * @author Leonardo Andrade
 *
 */
@Path("/dashboard")
public class DashboardResource {	
		
	@Inject
	private GenericPersitenceService persistenceService;
	
	@Inject
	private TransacaoService transacaoService;
	
	@Inject
	private Logger log;
	
	private final String TEMP_FILE_PATH = System.getProperty("java.io.tmpdir");
	
	@RolesAllowed("APP_USER_ROLE")
	@Path("/")
	@POST
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response find(String json){
		
		SimpleDateFormat dfIn   = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dfOut  = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		// Tratando a data inicial.
		JSONObject jObject = null;
		Date dataInicial   = null;
		Date dataFinal     = null;
		Long qtdeRegistros = 0l;
		
		List<Grupo> 	  grupos		= new ArrayList<Grupo>();
		List<Transacao>   transacoes 	= new ArrayList<Transacao>();
		List<EventoNivel> eventosNivel  = new ArrayList<EventoNivel>();
		
		try {
			jObject = new JSONObject(json);
			qtdeRegistros = jObject.getLong("qtdeRegistrosConsulta");
			
			String dataInicialStr  = jObject.getString("dataInicial");			
			if (StringUtils.isNotBlank(dataInicialStr))
				dataInicial = dfIn.parse(dataInicialStr);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		// Tratando a data final.
		try {
			String dataFinalStr  = jObject.getString("dataFinal");			
			if (StringUtils.isNotBlank(dataFinalStr))
				dataFinal = dfIn.parse(dataFinalStr);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
				
		try {
			// Recuperando filtro de grupos.
			JSONArray gruposArray = jObject.getJSONArray("grupos");
			for (int i = 0; i < gruposArray.length(); ++i) {
				Grupo grupo = new Grupo();
				JSONObject objJson = gruposArray.getJSONObject(i);
				grupo.setId(objJson.getLong("id"));
				grupos.add(grupo);
			}
			// Recuperando filtro de transações.
			JSONArray transacoesArray = jObject.getJSONArray("transacoes");
			for (int i = 0; i < transacoesArray.length(); ++i) {
				Transacao transacao = new Transacao();
				JSONObject objJson = transacoesArray.getJSONObject(i);
				transacao.setId(objJson.getLong("id"));
				try {
					transacao.setIdTransacaoInstancia(objJson.getString("idTransacaoInstancia"));
				}
				catch (Exception e) {
					log.error(e.getMessage());
				}
				try {
					transacao.setIdParametro(objJson.getLong("idParametro"));
				}
				catch (Exception e) {
					log.error(e.getMessage());
				}
				try {
					transacao.setValorParametro(objJson.getString("valorParametro"));
				}
				catch (Exception e) {
					log.error(e.getMessage());
				}
				transacoes.add(transacao);
			}			
			// Recuperando filtro de níveis de evento.
			JSONArray niveisEventoArray = jObject.getJSONArray("eventoNiveis");
			for (int i = 0; i < niveisEventoArray.length(); ++i) {
				EventoNivel eventoNivel = new EventoNivel();
				JSONObject objJson = niveisEventoArray.getJSONObject(i);
				eventoNivel.setId(objJson.getString("id"));
				eventosNivel.add(eventoNivel);
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
		}
		
		List<DashboardTreeItem> result = null;
		List<EventoInstanciaDashboard> vos = null;
		try {
			if (!transacoes.isEmpty() && !eventosNivel.isEmpty()) {
				vos = transacaoService.findEventoInstanciaByFiltro(
						transacaoService.findTransacaoInstanciaByFiltro(dataInicial, dataFinal, grupos, transacoes, eventosNivel, qtdeRegistros));
				if (vos!=null && !vos.isEmpty()) {
					result = new ArrayList<DashboardTreeItem>();
					String idTransacaoInstanciaAnterior = "";
					int indexParent = 0;
					int indexChild  = 0;
					for (EventoInstanciaDashboard vo : vos) {
						if (!vo.getIdTransacaoInstancia().equals(idTransacaoInstanciaAnterior)) {
							indexParent++;
							DashboardTreeItem item = new DashboardTreeItem();
							item.setId(Long.valueOf(indexParent));
							item.setDescricao((StringUtils.isNotBlank(vo.getCodigoGrupo()) ? vo.getCodigoGrupo() + " - " : "") + vo.getIdTransacaoInstancia());
							item.setData(vo.getDataTransacaoInstancia());
							item.setDataFormatada(dfOut.format(vo.getDataTransacaoInstancia()));
							item.setHasConteudo(false);
							item.setFilhos(new ArrayList<DashboardTreeItem>());
							result.add(item);
							idTransacaoInstanciaAnterior = vo.getIdTransacaoInstancia();
							indexChild  = 0;
						}
						indexChild++;
						result.get(indexParent-1).getFilhos().add(new DashboardTreeItem());
						result.get(indexParent-1).getFilhos().get(indexChild-1).setId(Long.valueOf(String.valueOf(indexParent)+String.valueOf(indexChild))*1000);
						result.get(indexParent-1).getFilhos().get(indexChild-1).setIdPai(Long.valueOf(indexParent));
						result.get(indexParent-1).getFilhos().get(indexChild-1).setDescricao(vo.getCodigoEventoTipo() + " - " + vo.getDescricao());
						result.get(indexParent-1).getFilhos().get(indexChild-1).setData(vo.getData());
						result.get(indexParent-1).getFilhos().get(indexChild-1).setDataFormatada(dfOut.format(vo.getData()));
						result.get(indexParent-1).getFilhos().get(indexChild-1).setItem(vo);
						result.get(indexParent-1).getFilhos().get(indexChild-1).setHasConteudo(vo.getPossuiConteudo());
					}
					// Definindo status dos itens da árvore.
					for (DashboardTreeItem item : result) {
						TransacaoInstanciaStatusEnum statusTransacaoEnum = DashboardUtil.getTransacaoInstanciaStatus(item.getFilhos());
						if (statusTransacaoEnum == TransacaoInstanciaStatusEnum.ERROR) {
							item.setStatus(AppConstants.ID_STATUS_TRANSACAO_INSTANCIA_ERROR);
							item.setMensagem(MessageBundle.getString("msg.dashboard.transacao.erro"));
						}
						else if (statusTransacaoEnum == TransacaoInstanciaStatusEnum.COMPLETED) {
							item.setStatus(AppConstants.ID_STATUS_TRANSACAO_INSTANCIA_COMPLETED);
							item.setMensagem(MessageBundle.getString("msg.dashboard.transacao.completa"));
						}
						else {
							item.setStatus(AppConstants.ID_STATUS_TRANSACAO_INSTANCIA_NOT_COMPLETED);
							item.setMensagem(MessageBundle.getString("msg.dashboard.transacao.incompleta"));
						}
						for (DashboardTreeItem filho : item.getFilhos()) {
							EventoInstanciaStatusEnum statusEventoEnum = DashboardUtil.getEventoInstanciaStatus(filho.getItem(), statusTransacaoEnum);
							if (statusEventoEnum == EventoInstanciaStatusEnum.ERROR) {
								filho.setStatus(AppConstants.ID_STATUS_EVENTO_INSTANCIA_ERROR);
								filho.setMensagem(MessageBundle.getString("msg.dashboard.evento.erro"));
							}
							else if (statusEventoEnum == EventoInstanciaStatusEnum.COMPLETED) {
								filho.setStatus(AppConstants.ID_STATUS_EVENTO_INSTANCIA_COMPLETED);
								filho.setMensagem(MessageBundle.getString("msg.dashboard.evento.completo"));
							}
							else {
								filho.setStatus(AppConstants.ID_STATUS_EVENTO_INSTANCIA_NOT_COMPLETED);
								filho.setMensagem(MessageBundle.getString("msg.dashboard.evento.incompleto"));
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
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}	
	
	@RolesAllowed("APP_USER_ROLE")
	@GET
	@Path("/download/conteudo/{idEventoInstancia}")
	@Produces("text/plain")
	public Response downloadInstanceEventContentFile(@PathParam("idEventoInstancia") String idEventoInstancia) {
		BufferedWriter bw = null;
		FileWriter fw = null;		
		EventoInstanciaConteudo result = null;	
		try {
			result = persistenceService.findById(EventoInstanciaConteudo.class, idEventoInstancia);
			if (result!=null) {
				String fileName = new Date().getTime() + ".txt";
				fw = new FileWriter(TEMP_FILE_PATH + fileName);
				bw = new BufferedWriter(fw);
				bw.write(result.getConteudo());
				File file = FileUtils.getFile(TEMP_FILE_PATH + fileName);
				if (file!=null) {
					ResponseBuilder response = Response.ok((Object) file);
					response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
					return response.build();
				}
				else {
					return Response.status(Response.Status.NOT_FOUND).entity(MessageBundle.getString("msg.arquivo.nao.encontrado")).build();
				}
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
				if (fw!=null) {
					fw.close();
				}
			}
			catch (IOException e) {
				log.error(e.getMessage(), e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MessageBundle.getString("msg.erro.inesperado")).build();
			}
		}				
	}
	
}
