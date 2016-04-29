package com.lesserhydra.wordgen;


/**
 * Represents a problem encountered while parsing a NameGenerator grammar.
 */
public class GrammarParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public GrammarParseException(String message) {
		super(message);
	}
	
}
