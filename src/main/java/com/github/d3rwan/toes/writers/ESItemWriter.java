package com.github.d3rwan.toes.writers;

import java.util.List;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import com.github.d3rwan.toes.exceptions.ESException;
import com.github.d3rwan.toes.models.ESDocument;
import com.github.d3rwan.toes.models.ToESDocument;

/**
 * Writer for Elasticsearch
 * 
 * @author d3rwan
 *
 */
public class ESItemWriter<T> implements ItemWriter<T> {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ESItemWriter.class);

    /** environment */
    @Autowired
    private Environment environment;

    /** ES client */
    @Autowired
    private Client esClient;

    /** timeout */
    private String timeout;

    /**
     * Default constructor
     */
    public ESItemWriter() {
        super();
    }

    /**
     * Constructor
     * @param esClient ES client
     */
    public ESItemWriter(Client esClient) {
        super();
        setEsClient(esClient);
    }

    /**
     * Constructor
     * @param esClient ES client
     * @param timeout timeout
     */
    public ESItemWriter(Client esClient, String timeout) {
        super();
        setEsClient(esClient);
        setTimeout(timeout);
    }

    /** Test after properties set */
    @PostConstruct
    public void afterPropertiesSet() {
        Assert.notNull(esClient, "esClient must not be null");
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        try {
            BulkRequestBuilder bulkRequest = esClient.prepareBulk();
            for (Object item : items) {
                ESDocument document;
                if (item instanceof ESDocument) {
                    document = (ESDocument) item;
                } else if (item instanceof ToESDocument) {
                    document = ((ToESDocument) item).toESDocument();
                } else {
                    throw new ESException("Object to index must be instance of"
                            + " ESDocument or implement interface ToESDocument");
                }
                IndexRequestBuilder request = esClient.prepareIndex(
                        document.getIndex(),
                        document.getType(),
                        document.getId());
                request.setSource(document.getSource());
                if (document.getVersion() != null) {
                    request.setVersion(document.getVersion());
                }
                bulkRequest.add(request);
            }
            BulkResponse response;
            if (timeout != null) {
                response = bulkRequest.execute().actionGet(timeout);
            } else {
                response = bulkRequest.execute().actionGet();
            }
            if (response.hasFailures()) {
                throw new ESException("An error occured during bulk request : "
                        + response.buildFailureMessage());
            }
            LOGGER.info("{} documents indexed in {} ms", response.getItems().length, response.getTookInMillis());
        } catch (Exception ex) {
            throw new ESException("An error occured during bulk request", ex.getCause());
        }
    }

    /**
     * @param esClient the esClient to set
     */
    public void setEsClient(Client esClient) {
        this.esClient = esClient;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}