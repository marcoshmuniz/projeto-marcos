/**
 * 
 */
package com.globo.servico;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.globo.controle.ControleVotacao;
import com.globo.entidade.Voto;
import com.globo.exception.BBBValidacaoException;

/**
 * @author Marcos
 * 
 */
@Path("/votacao")
public class ServicoVotacao {
	private static final Logger LOG = Logger.getLogger(ServicoVotacao.class);
	private ControleVotacao controleVotacao;

	public ServicoVotacao() {
		controleVotacao = new ControleVotacao();
		LOG.debug("Classe ServicoVotacao instanciada.");
	}

	@POST
	@Path("/votar/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void votar(@PathParam("id") String id) {
		LOG.debug("Método votar da classe ServicoVotacao acionado.");
		controleVotacao.votar(id);
	}

	@GET
	@Path("/resultado")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Voto> resultado() throws BBBValidacaoException {
		LOG.debug("Método resultado da classe ServicoVotacao acionado.");
		return controleVotacao.recuperarPercentual();
	}
}
