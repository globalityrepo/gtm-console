package br.com.globality.gtm.console.web.api.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * Criação de componentes padrões da aplicação 
 */
@ApplicationScoped
public class Producer {	
	
	@PersistenceUnit(unitName="br.com.globality.gtm")
    private EntityManagerFactory factory;
	
	@Produces
	public EntityManager getEntityManager(){
		return factory.createEntityManager();
	}
	// cleaup method
	public void closeEntityManager(@Disposes EntityManager manager) {
        if (manager.isOpen()) {
            manager.close();
        }
    }
	
}

