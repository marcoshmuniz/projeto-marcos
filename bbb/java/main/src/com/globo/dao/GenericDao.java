/**
 * 
 */
package com.globo.dao;

import static me.prettyprint.hector.api.factory.HFactory.createCounterColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HCounterColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.CounterQuery;
import me.prettyprint.hector.api.query.QueryResult;

import org.apache.log4j.Logger;

import com.globo.servico.ServicoVotacao;

/**
 * @author Marcos
 * 
 */

public class GenericDao {
	private static final Logger LOG = Logger.getLogger(ServicoVotacao.class);
	private static GenericDao instance;
	private static String KEYSPACE_NAME;
	protected Keyspace keyspace;
	private Cluster cluster;
	
	public static GenericDao getInstance() {
		if (instance == null) {
			ResourceBundle bundle = ResourceBundle.getBundle("conf");
			String keyspaceName = null;
			String clusterName = null;
			String clusterLocation = null;
			boolean criarSchema = false;
	    	clusterName = "local";
			clusterLocation = "localhost:9160";
			
			String ambiente = bundle.getString("ambiente");
			if (ambiente.equals("teste")) {
				keyspaceName = bundle.getString("keyspace_teste");
			}else{
				keyspaceName = bundle.getString("keyspace_dev");
			}
			String recriarBanco = bundle.getString("recriarBanco");
			if (recriarBanco.equals("true")) {
				criarSchema = true;
			}
	 
			instance = new GenericDao(keyspaceName, clusterName, clusterLocation,criarSchema);			
		}
		return instance;
	}
	
	private GenericDao(String keyspaceName, String clusterName, String clusterLocation, boolean criarKeyspace) {
		LOG.debug("Criando o objeto GenericDao");
		KEYSPACE_NAME = keyspaceName;
		cluster = HFactory.getOrCreateCluster(clusterName, clusterLocation);
		keyspace = HFactory.createKeyspace(KEYSPACE_NAME, cluster);
		KeyspaceDefinition keyspaceDef = cluster
				.describeKeyspace(KEYSPACE_NAME);
		// Cria o esquema caso ele não exista.
		if (keyspaceDef == null && criarKeyspace) {
			createKeyspace(KEYSPACE_NAME);
		}else if(criarKeyspace){
			createKeyspace(KEYSPACE_NAME);
		}

	}

	protected void createKeyspace(String keyspaceName) {
		LOG.debug("Criando o keyspace "+keyspaceName);
		List<ColumnFamilyDefinition> definicoes = new ArrayList<>();		
		ColumnFamilyDefinition countDef = HFactory.createColumnFamilyDefinition(
				keyspaceName, "contagem", ComparatorType.BYTESTYPE);
		countDef.setDefaultValidationClass(ComparatorType.COUNTERTYPE
				.getClassName());
		definicoes.add(countDef);
		KeyspaceDefinition novoKeyspace = HFactory.createKeyspaceDefinition(
				keyspaceName, ThriftKsDef.DEF_STRATEGY_CLASS, 1,
				definicoes);
		cluster.addKeyspace(novoKeyspace, true);
		LOG.debug("keyspace "+keyspaceName+" foi criado");
	}
	
	protected void dropKeyspace(String keyspace) {
		LOG.debug("Apagando o keyspace "+keyspace);
		cluster.dropKeyspace(keyspace);
		LOG.debug("keyspace "+keyspace+" foi apagado");
	}

	/**
	 * @param participante
	 * @return o número de votos de um participante.
	 */
	public Integer recuperarNumeroVotos(String participante) {
		LOG.debug("Recuperando o número de votos.");
		StringSerializer stringSerializer = StringSerializer.get();
		CounterQuery<String, String> query = HFactory.createCounterColumnQuery(keyspace, stringSerializer, stringSerializer);
		query.setColumnFamily("contagem").setKey(participante).setName("votos");
		QueryResult<HCounterColumn<String>> resultado = query.execute();
		if (resultado == null || resultado.get() == null) {
			return 0;
		}
		Long numeroVotos = resultado.get().getValue();
		return numeroVotos.intValue();
	}

	public void inserirVoto(String rowKey) {
		LOG.debug("Inserindo um voto para:"+rowKey);
		Mutator<String> mutator = HFactory.createMutator(keyspace,
				StringSerializer.get());
		mutator.insertCounter(rowKey, "contagem",
				createCounterColumn("votos", 1));
	}
}
