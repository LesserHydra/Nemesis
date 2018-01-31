package com.lesserhydra.wordgen;

import java.util.Set;

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
	 * @return A collection of potential matches, int i >= 0 where i is the number of characters matched
	 */
	Set<Integer> match(String string);
	
	/**
	 * Returns the number of possible values represented by this symbol.
	 * @return The number of possible values
	 */
	double numRepresented();
	
}
