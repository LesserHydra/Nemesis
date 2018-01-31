package com.lesserhydra.wordgen;

import java.util.Collections;
import java.util.Set;

/**
 * A terminal symbol, that is, a symbol that directly represents one string.
 * @author Justin Lawen
 */
public class TerminalSymbol implements Symbol {
	
	private final String str;
	
	/**
	 * Constructs a terminal symbol representing the given string.
	 * @param str The string to represent
	 */
	public TerminalSymbol(String str) {
		this.str = str;
	}
	
	@Override
	public String evaluate() {
		return str;
	}
	
	@Override
	public Set<Integer> match(String string) {
		return string.startsWith(str) ? Collections.singleton(str.length()) : Collections.emptySet();
	}
	
	@Override
	public double numRepresented() {
		return 1;
	}

}
