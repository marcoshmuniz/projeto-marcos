/**
 * 
 */
package com.globo.controle;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.globo.dao.GenericDao;
import com.globo.entidade.Voto;
import com.globo.exception.BBBValidacaoException;
import com.globo.servico.ServicoVotacao;

/**
 * @author Marcos
 * 
 */
public class ControleVotacao {
	private static final Logger LOG = Logger.getLogger(ServicoVotacao.class);

	private GenericDao dao;

	public ControleVotacao() {
		LOG.debug("Criando um objeto de ControleVotacao.");
		dao = GenericDao.getInstance();
	}

	/**
	 * 
	 * @param participante
	 */
	public void votar(String participante) {
		LOG.debug("Votando no participante: " + participante);
		dao.inserirVoto(participante);
	}

	public List<Voto> recuperarPercentual() throws BBBValidacaoException {
		LOG.debug("Recuperando o percentural...");
		String participante1 = "P1";
		String participante2 = "P2";

		Integer numVotosP1 = dao.recuperarNumeroVotos(participante1);
		Integer numVotosP2 = dao.recuperarNumeroVotos(participante2);
		Integer total = numVotosP1 + numVotosP2;
		double percentual1 = calcularPercentual(numVotosP1, total);
		double percentual2 = calcularPercentual(numVotosP2, total);

		List<Voto> votos = new ArrayList<Voto>();

		votos.add(new Voto("P1", percentual1));
		votos.add(new Voto("P2", percentual2));

		return votos;
	}

	/**
	 * Calcula o percentual do parcial informado em relação ao total.
	 * 
	 * @param parcial
	 * @param total
	 * @return
	 * @throws BBBValidacaoException
	 *             caso o parcial seja maior que o total.
	 */
	protected Double calcularPercentual(Integer parcial, Integer total)
			throws BBBValidacaoException {
		if (parcial > total) {
			throw new BBBValidacaoException(
					"O parcial da votação não pode ser maior que o total.");
		}
		if (parcial < 0 || total < 0) {
			throw new BBBValidacaoException(
					"O parcial e o total devem ser positivos.");
		}
		// Parcial e total são zero nesse caso
		if (total == 0) {
			return 0d;
		}

		return ((double) (parcial * 100) / total);
	}
}
