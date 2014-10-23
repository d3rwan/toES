package com.github.d3rwan.toes.exceptions;

/**
 * ES Exception 
 * 
 * @author d3rwan
 * 
 */
public class ESException extends Exception {

	/** Serial UID */
	private static final long serialVersionUID = -8657499808698090227L;

	public ESException() {
		super();
	}

	public ESException(String message, Throwable cause) {
		super(message, cause);
	}

	public ESException(String message) {
		super(message);
	}

	public ESException(Throwable cause) {
		super(cause);
	}
}
