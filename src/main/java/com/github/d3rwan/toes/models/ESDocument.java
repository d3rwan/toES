package com.github.d3rwan.toes.models;

import java.io.Serializable;

/**
 * Document to index
 * 
 * @author d3rwan
 * 
 */
public class ESDocument implements Serializable {

    /** Serial UID */
    private static final long serialVersionUID = 3541943018711382146L;

    /** index */
    private String index;

    /** index */
    private String type;

    /** index */
    private String id;

    /** index */
    private String source;

    /** index */
    private Long version;

    /**
     * Default constructor
     */
    public ESDocument() {
        super();
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}
