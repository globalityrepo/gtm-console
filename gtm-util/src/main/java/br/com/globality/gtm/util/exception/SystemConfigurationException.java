package br.com.globality.gtm.util.exception;

/**
 * Exceção pai de todas as exceções de sistema.
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 */
public class SystemConfigurationException extends SINSystemException {

	private static final long serialVersionUID = -4131198649164320418L;

	public SystemConfigurationException(String message) {
		super("ERRO: Erro de configuação: " + message);
	}
}
