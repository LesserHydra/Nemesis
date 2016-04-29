package com.lesserhydra.wordgen;

/**
 * Represents a symbol in a NameGenerator grammar.
 */
interface Symbol {
	
	/**
	 * Evaluates this symbol to a possible string value represented by this symbol.
	 * @return Possible string value
	 */
	public String evaluate();
	
	/**
	 * Returns the number of possible values represented by this symbol.
	 * @return The number of possible values
	 */
	public double numRepresented();
	
}
