package br.com.globality.gtm.console.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import br.com.globality.gtm.console.util.annotation.RESTful;
import br.com.globality.gtm.console.util.annotation.RESTfulEntities;

/**
 * Criação de componentes padrões da aplicação
 */
@ApplicationScoped
public class Producer {

	@Produces
	public Logger getLooger(InjectionPoint injectionPoint) {
		return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}
	
	@Produces	
	@RESTfulEntities
	public Map<String, Class<?>> getRestfullEntities() {
		HashMap<String, Class<?>> restfullEntities = new HashMap<String, Class<?>>();
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(RESTful.class));
		final Set<BeanDefinition> classes = provider.findCandidateComponents("br.com.globality.gtm");
		try {
			for (BeanDefinition bean: classes) {
			    Class<?> clazz = Class.forName(bean.getBeanClassName());
			    RESTful request = clazz.getAnnotation(RESTful.class);
		    	String endpoint = request.value();
				if (StringUtils.isBlank(endpoint)) {
					endpoint = clazz.getSimpleName().toLowerCase();
				}
				restfullEntities.put(endpoint, clazz);
			}
		}
        catch (Exception e) {
        	e.printStackTrace();
        }
		return restfullEntities;
	}
	
}
