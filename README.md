   toES
=====================

Insert data into ElasticSearch, based on springboot / springbatch

#### How it works ?

Index CSV files into Elasticsearch ? See [https://github.com/d3rwan/CSVtoES](https://github.com/d3rwan/CSVtoES)

Index Database table using Hibernate into Elasticsearch ? See [https://github.com/d3rwan/DBtoES](https://github.com/d3rwan/DBtoES)


#### Document ES

|   Name                     | Usage                               |
|:---------------------------|:------------------------------------|
| ESItemWriter               | Document to index in ES             |

> **Usage:**

    ESDocument document = new ESDocument();
    document.setIndex("myindex");
    document.setType("mytype");
    document.setId("1");
    document.setSource("{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}");


#### ItemProcessor

|   Name                     | Usage                               |
|:---------------------------|:------------------------------------|
| AbstractESItemProcessor<T> | Transform T object into ESDocument  |

> **Usage:**

    public class PersonItemProcessor extends AbstractESItemProcessor<Person> {
    
       @Override
       public ESDocument process(Person item) throws Exception {
           ESDocument document = new ESDocument();
           document.setIndex("myindex");
           document.setType("mytype");
           document.setId(item.getId());
           document.setSource(mapper.writeValueAsString(item));
           return document;
       }
    }


#### ItemWriter

|   Name                     | Usage                               |
|:---------------------------|:------------------------------------|
| ESItemWriter<ESDocument>   | Index ESDocument using bulk API     |

> **Usage:**

    ESItemWriter<ESDocumentwriter = new ESItemWriter<ESDocument>(esClient);


#### Tasklets

|   Name                     | Usage                               |
|:---------------------------|:------------------------------------|
| AddAliasESTasklet          | Add an alias ES                     |
| CreateIndexESTasklet       | Create an index ES                  |
| DeleteAliasESTasklet       | Delete an alias                     |
| DeleteIndexESTasklet       | Delete an index                     |
| PutMappingESTasklet        | Put mapping for an index and a type |

> **Usage:**

    AddAliasESTasklet addAlias = new AddAliasESTasklet();
    addAlias.setEsClient(esClient);
    addAlias.setIndex(environment.getProperty("myindex"));
    addAlias.setAlias(environment.getProperty("myalias"));
    
    CreateIndexESTasklet createIndex = new CreateIndexESTasklet();
    createIndex.setEsClient(esClient);
    createIndex.setIndex("myindex");
    createIndex.setSettings(new ClassPathResource("settings.json"));
    
    DeleteAliasESTasklet deleteAlias = new DeleteAliasESTasklet();
    deleteAlias.setEsClient(esClient);
    deleteAlias.setIndex(environment.getProperty("myindex"));
    deleteAlias.setAlias(environment.getProperty("myalias"));
    
    DeleteIndexESTasklet deleteIndexIfAlreadyExist = new DeleteIndexESTasklet();
    deleteIndexIfAlreadyExist.setEsClient(esClient);
    deleteIndexIfAlreadyExist.setIndex("myindex");
    
    PutMappingESTasklet putMapping = new PutMappingESTasklet();
    putMapping.setEsClient(esClient);
    putMapping.setIndex(environment.getProperty("myindex");
    putMapping.setType(environment.getProperty("mytype");
    putMapping.setMapping(new ClassPathResource("mapping.json"));
