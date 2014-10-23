package com.github.d3rwan.toes.models;

import com.github.d3rwan.toes.exceptions.ESException;

/**
 * Interface for transform in ESDocument
 * 
 * @author d3rwan
 * 
 */
public interface ToESDocument {

	/** transform in ESDocument */
	ESDocument toESDocument() throws ESException;

}
