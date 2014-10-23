package com.github.d3rwan.toes.tasklets;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.Assert;

import com.github.d3rwan.toes.exceptions.ESException;

/**
 * Delete alias
 * 
 * @author d3rwan
 * 
 */
public class DeleteAliasESTasklet extends AbstractESTasklet {

	/** logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAliasESTasklet.class);

	/** Test after properties set */
	@PostConstruct
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(getAlias(), "alias must not be null");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws ESException {
		String alias = getAlias();
		String index = getIndex();
		try {
			LOGGER.info("Deleting alias {} -> {}", alias, index);
			IndicesAliasesRequestBuilder iarb = esClient.admin().indices().prepareAliases();
			iarb.removeAlias(index, alias);
			IndicesAliasesResponse response = iarb.execute().actionGet();
			if (!response.isAcknowledged()) {
				throw new ESException("An error occured when deleting alias " + alias + " -> " + index);
			}
			LOGGER.info("Alias {} -> {} deleted successfully", alias, index);
			return RepeatStatus.FINISHED;
		} catch (Exception ex) {
			throw new ESException("An error occured when deleting alias " + alias + " -> " + index, ex.getCause());
		}
	}
}
