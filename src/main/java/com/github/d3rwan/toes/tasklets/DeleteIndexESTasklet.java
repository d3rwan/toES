package com.github.d3rwan.toes.tasklets;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.indices.IndexMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import com.github.d3rwan.toes.exceptions.ESException;

/**
 * Delete index
 * 
 * @author d3rwan
 * 
 */
public class DeleteIndexESTasklet extends AbstractESTasklet {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteIndexESTasklet.class);

    /** Test after properties set */
    @PostConstruct
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws ESException {
        String index = getIndex();
        try {
            LOGGER.info("Deleting index {}", index);
            DeleteIndexResponse response = esClient.admin().indices().prepareDelete(index).execute().actionGet();
            if (!response.isAcknowledged()) {
                throw new ESException("An error occured when deleting index " + index);
            }
            LOGGER.info("Index {} deleted successfully", index);
            return RepeatStatus.FINISHED;
        } catch (IndexMissingException ex) {
            LOGGER.info("Index {} is already missing", index);
            return RepeatStatus.FINISHED;
        } catch (Exception ex) {
            throw new ESException("An error occured when deleting index " + index, ex);
        }
    }
}
