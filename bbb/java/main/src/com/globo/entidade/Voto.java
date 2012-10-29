/**
 * 
 */
package com.globo.entidade;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Marcos
 *
 */
@XmlRootElement
public class Voto {

	public Voto() {
		super();
	}

	private String participante;
	
	private Double percentual;

	public String getParticipante() {
		return participante;
	}

	public void setParticipante(String participante) {
		this.participante = participante;
	}

	public Double getPercentual() {
		return percentual;
	}

	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}

	public Voto(String participante, Double percentual) {
		super();
		this.participante = participante;
		this.percentual = percentual;
	}
	
}
