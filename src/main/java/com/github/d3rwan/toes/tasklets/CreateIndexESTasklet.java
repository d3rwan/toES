package com.github.d3rwan.toes.tasklets;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

import com.github.d3rwan.toes.common.Constants;
import com.github.d3rwan.toes.exceptions.ESException;

/**
 * Create index
 * 
 * @author d3rwan
 * 
 */
public class CreateIndexESTasklet extends AbstractESTasklet {

	/** logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateIndexESTasklet.class);

	/** settings */
	private Resource settings;

	/** Test after properties set */
	@PostConstruct
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws ESException {
		String index = getIndex();
		try {
			LOGGER.info("Creating index {}", index);
			CreateIndexRequestBuilder createIndexReq = esClient.admin().indices().prepareCreate(index);
			if (settings != null) {
				String set = IOUtils.toString(settings.getInputStream(), Constants.CHARSET_UTF8);
				createIndexReq.setSettings(set);
			}
			CreateIndexResponse response = createIndexReq.execute().actionGet();
			if (!response.isAcknowledged()) {
				throw new ESException("An error occured when creating index " + index);
			}
			esClient.admin().indices().refresh(new RefreshRequest(index)).actionGet();
			LOGGER.info("Index {} created successfully", index);
			return RepeatStatus.FINISHED;
		} catch (Exception ex) {
			throw new ESException("An error occured when creating index " + index, ex);
		}
	}

	/**
	 * @param settings the settings to set
	 */
	public void setSettings(Resource settings) {
		this.settings = settings;
	}
}
