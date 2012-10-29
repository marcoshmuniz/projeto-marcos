package com.globo.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import me.prettyprint.hector.api.exceptions.HectorException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Marcos
 * 
 */
public class TesteGenericDao {
	private GenericDao dao = GenericDao.getInstance();
	ResourceBundle bundle = ResourceBundle.getBundle("conf");

	@Before
	public void iniciarTeste() {
		String keyspace = bundle.getString("keyspace_teste");
		try {
			dao.dropKeyspace(keyspace);
		} catch (HectorException e) {
			// Sem problemas, o keyspace pode não existir.
		}
		dao.createKeyspace(keyspace);
	}

	@After
	public void terminarTeste() {
		String keyspace = bundle.getString("keyspace_teste");
		dao.dropKeyspace(keyspace);
	}

	@Test
	public void testarRecuperarNumeroVotos() {
		// Inicialmente não deve existir nenhum voto pq o keyspace foi criado.
		assertEquals(new Integer(0), dao.recuperarNumeroVotos("P1"));
		assertEquals(new Integer(0), dao.recuperarNumeroVotos("P2"));
		Map<String, Integer> votos = new HashMap<String, Integer>();
		votos.put("P1", 10);
		votos.put("P2", 8);
		// insere os registros.
		inserirVotos(votos);
		assertEquals(new Integer(10), dao.recuperarNumeroVotos("P1"));
		assertEquals(new Integer(8), dao.recuperarNumeroVotos("P2"));
	}

	/**
	 * Insere votos para a realização dos testes.
	 * 
	 * @param votosParticipantes
	 *            - número de votos para cada participantes que devem receber
	 *            votos.
	 */
	private void inserirVotos(Map<String, Integer> votos) {
		for (String chave : votos.keySet()) {
			for (int i = 0; i < votos.get(chave); i++) {
				dao.inserirVoto(chave);
			}
		}
	}

	@Test
	public void testarInserirVotos() {
		assertEquals(new Integer(0), dao.recuperarNumeroVotos("1"));
		dao.inserirVoto("1");
		assertEquals(new Integer(1), dao.recuperarNumeroVotos("1"));
	}

	@Test
	public void testarCriarKeyspace() {
		String nomeKeyspace = "teste";
		try {
			dao.createKeyspace(nomeKeyspace);
		} catch (HectorException e) {
			fail("Erro não esperado.");
		}
		try {
			dao.createKeyspace(nomeKeyspace);
			fail("Criação do workspace não deveria ocorrer.");
		} catch (HectorException e) {
			// Ok, erro esperado.
		}
		dao.dropKeyspace(nomeKeyspace);
	}

	@Test
	public void testarDropKeyspace() {
		String nomeKeyspace = "teste";
		try {
			dao.dropKeyspace(nomeKeyspace);
			fail("drop do workspace não deveria ocorrer.");
		} catch (HectorException e) {
			// Ok, erro esperado.
		}
		dao.createKeyspace(nomeKeyspace);
		try {
			dao.dropKeyspace(nomeKeyspace);
		} catch (HectorException e) {
			fail("Erro não esperado.");
		}
	}
}
