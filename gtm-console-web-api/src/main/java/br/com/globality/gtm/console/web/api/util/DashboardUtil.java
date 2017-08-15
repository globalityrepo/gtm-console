package br.com.globality.gtm.console.web.api.util;

import java.util.List;

import br.com.globality.gtm.console.model.EventoInstanciaDashboard;

public class DashboardUtil {

	public static TransacaoInstanciaStatusEnum getTransacaoInstanciaStatus(List<DashboardTreeItem> eventos) {
		
		boolean endOk = false;
		boolean existResubmit = false;
		boolean resubmitSuccess = false;
		boolean error = false;
		
		// Verifica se ocorreu erro na instância da transação
        for (DashboardTreeItem evento : eventos) {
        	if (isEventoEncerradoComErro(evento.getItem().getCodigoEventoTipo())) {
        		error = true;
        		break;
        	}
        }
        
        // Senão ocorreu erro
        if (!error) {
            // Verifica se a instancia da transação terminou com sucesso
        	for (DashboardTreeItem evento : eventos) {
            	if (isEventoEncerradoComSucesso(evento.getItem().getCodigoEventoTipo())) {
            		endOk = true;
            		break;
            	}
            }
            // Verifica se existe evento de resubmit
        	for (DashboardTreeItem evento : eventos) {
            	if (isEventoResubmit(evento.getItem().getCodigoEventoTipo())) {
            		existResubmit = true;
            		break;
            	}
            }
        	// Se existe resubmit
        	resubmitSuccess = (existResubmit && endOk);
        }
        
        if (error) {
            return TransacaoInstanciaStatusEnum.ERROR;
        } 
        else {
            if (endOk || (existResubmit && resubmitSuccess)) {
                return TransacaoInstanciaStatusEnum.COMPLETED;
            } 
            else if ((existResubmit && !resubmitSuccess)) {
                return TransacaoInstanciaStatusEnum.NOT_COMPLETED;
            } 
            else {
                return TransacaoInstanciaStatusEnum.NOT_COMPLETED;
            }
        }
        
    }
	
	public static EventoInstanciaStatusEnum getEventoInstanciaStatus(EventoInstanciaDashboard evento, TransacaoInstanciaStatusEnum statusTransacaoInstancia) {
		if (isEventoEncerradoComErro(evento.getCodigoEventoTipo())) {
            if (statusTransacaoInstancia == TransacaoInstanciaStatusEnum.ERROR 
            		|| statusTransacaoInstancia == TransacaoInstanciaStatusEnum.COMPLETED) {
                return EventoInstanciaStatusEnum.ERROR;
            } 
            else {
            	return EventoInstanciaStatusEnum.NOT_COMPLETED;
            }
        } else if (statusTransacaoInstancia == TransacaoInstanciaStatusEnum.ERROR 
        		|| statusTransacaoInstancia == TransacaoInstanciaStatusEnum.COMPLETED) {
    		return EventoInstanciaStatusEnum.COMPLETED;
        } 
    	else {
            return EventoInstanciaStatusEnum.NOT_COMPLETED;
        }
    }
	
	private static boolean isEventoEncerradoComErro(String codigoEventoTipo) {
	    try {
	    	Long codigoEvento = Long.parseLong(codigoEventoTipo);
	    	return (codigoEvento >= 2000 && codigoEvento <= 2999) 
	    			|| (codigoEvento >= 4000 && codigoEvento <= 4999) 
	    			|| (codigoEvento >= 6000 && codigoEvento <= 6999);
	    }
	    catch(NumberFormatException e) {
	    	return false;
	    }
    }
	
	private static boolean isEventoEncerradoComSucesso(String codigoEventoTipo) {
	    return (codigoEventoTipo.equals("9998") || codigoEventoTipo.equals("9999"));
    }
	
	private static boolean isEventoResubmit(String codigoEventoTipo) {
	    return (codigoEventoTipo.equals("0002"));
    }
	
}
