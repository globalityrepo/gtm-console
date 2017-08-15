package br.com.globality.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Criação de componentes padrões da aplicação 
 */
@ApplicationScoped
public class Producer {	
	
	@Produces
	public Logger getLooger( InjectionPoint injectionPoint ){
		return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}
	
}

