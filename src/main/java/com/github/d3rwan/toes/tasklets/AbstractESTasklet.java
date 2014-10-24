package com.github.d3rwan.toes.tasklets;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.Client;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import com.github.d3rwan.toes.exceptions.ESException;

/**
 * Abstract Tasklet for Elasticsearch
 * 
 * @author d3rwan
 * 
 */
public abstract class AbstractESTasklet implements Tasklet {

	/** environment */
	@Autowired
	public Environment environment;

	/** ES client */
	@Autowired
	public Client esClient;

	/** index */
	public String index;

	/** type */
	public String type;

	/** alias */
	public String alias;

	/** Test after properties set */
	@PostConstruct
	public void afterPropertiesSet() {
		Assert.notNull(esClient, "esClient must not be null");
		Assert.notNull(index, "indexName must not be null");
	}

	@Override
	public abstract RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws ESException;

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * @return the esClient
	 */
	public Client getEsClient() {
		return esClient;
	}

	/**
	 * @param esClient the esClient to set
	 */
	public void setEsClient(Client esClient) {
		this.esClient = esClient;
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
