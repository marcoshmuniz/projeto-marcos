/**
 * 
 */
package com.globo.exception;

/**
 * @author Marcos
 *Classe para erros de valida��o da aplica��o.
 */
public class BBBValidacaoException extends Exception {

	public BBBValidacaoException(String msg) {
		super(msg);
	}
	
	public BBBValidacaoException() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
