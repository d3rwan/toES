package com.github.d3rwan.toes.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.d3rwan.toes.models.ESDocument;

public abstract class AbstractESItemProcessor<T> implements ItemProcessor<T, ESDocument> {

	/** environment */
	@Autowired
	protected Environment environment;

	/** mapper */
	protected ObjectMapper mapper = new ObjectMapper();

	@Override
	public abstract ESDocument process(T item) throws Exception;

}
