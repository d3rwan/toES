package com.github.d3rwan.toes.tasklets;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.github.d3rwan.toes.common.Constants;
import com.github.d3rwan.toes.exceptions.ESException;

/**
 * Put mapping index
 * 
 * @author d3rwan
 * 
 */
public class PutMappingESTasklet extends AbstractESTasklet {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(PutMappingESTasklet.class);

    /** mapping */
    private Resource mapping;

    /** Test after properties set */
    @PostConstruct
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(mapping, "mapping must not be null");
        Assert.notNull(getType(), "type must not be null");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws ESException {
        String index = getIndex();
        String type = getType();
        try {
            LOGGER.info("Adding mapping {} for index {}", type, index);
            String map = IOUtils.toString(mapping.getInputStream(), Constants.CHARSET_UTF8);
            PutMappingRequestBuilder request = esClient.admin().indices().preparePutMapping(index);
            request.setSource(map).setType(type);
            PutMappingResponse response = request.execute().actionGet();
            if (!response.isAcknowledged()) {
                throw new ESException("An error occured when adding mapping " + type + " for index " + index);
            }
            LOGGER.info("Mapping {} for index {} added successfully", type, index);
            return RepeatStatus.FINISHED;
        } catch (Exception ex) {
            throw new ESException("An error occured when adding mapping " + type + " for index " + index, ex);
        }
    }

    /**
     * @param mapping the mapping to set
     */
    public void setMapping(Resource mapping) {
        this.mapping = mapping;
    }
}
