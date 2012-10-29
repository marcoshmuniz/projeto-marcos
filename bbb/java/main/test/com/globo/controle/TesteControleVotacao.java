package com.globo.controle;

import static junit.framework.Assert.fail;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.globo.exception.BBBValidacaoException;

public class TesteControleVotacao {

	private ControleVotacao servicoVotacao;

	@Before
	public void init() {
		servicoVotacao = new ControleVotacao();
	}

	@Test
	public void testarCalcularPercentualInvalido() {
		try {
			servicoVotacao.calcularPercentual(110, 100);
			fail("A exceção BBBValidacaoException deveria ter sido lançada.");
		} catch (BBBValidacaoException e) {
			// Ok ;) exceção esperada
		}
		try {
			servicoVotacao.calcularPercentual(-1, 100);
			fail("A exceção BBBValidacaoException deveria ter sido lançada.");
		} catch (BBBValidacaoException e) {
			// Ok ;) exceção esperada
		}
		
	}

	@Test
	public void testarCalcularPercentualValido() {
		try {
			Double percentual = servicoVotacao.calcularPercentual(10, 100);
			Assert.assertEquals(10.0d, percentual);
			percentual = servicoVotacao.calcularPercentual(93, 1000);
			Assert.assertEquals(9.3d, percentual);
		} catch (Exception e) {
			fail("A exceção não deveria ocorrer. " + e.getMessage());
		}
		try {
			Double percentual = servicoVotacao.calcularPercentual(0, 0);
			Assert.assertEquals(0d, percentual);
		} catch (BBBValidacaoException e) {
			fail("A exceção não deveria ocorrer. " + e.getMessage());
		}
		try {
			Double percentual = servicoVotacao.calcularPercentual(0, 100);
			Assert.assertEquals(0d, percentual);
		} catch (BBBValidacaoException e) {
			fail("A exceção não deveria ocorrer. " + e.getMessage());
		}
	}
}
