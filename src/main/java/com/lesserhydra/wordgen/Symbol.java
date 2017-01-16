package com.lesserhydra.wordgen;

/**
 * Represents a symbol in a NameGenerator grammar.
 */
interface Symbol {
	
	/**
	 * Evaluates this symbol to a possible string value represented by this symbol.
	 * @return Possible string value
	 */
	String evaluate();
	
	/**
	 * Attempts to match the beginning of the given string as a possible result of evaluate().
	 * @param string String to match
	 * @return -1 if no match, or int i >= 0 where i is the number of characters matched
	 */
	int match(String string);
	
	/**
	 * Returns the number of possible values represented by this symbol.
	 * @return The number of possible values
	 */
	double numRepresented();
	
}
