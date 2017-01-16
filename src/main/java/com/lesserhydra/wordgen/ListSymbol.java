package com.lesserhydra.wordgen;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents one or more concatenated symbols.
 */
public class ListSymbol implements Symbol {
	
	private final List<Symbol> symbols;
	private final double represented;
	
	ListSymbol(List<Symbol> symbols) {
		this.symbols = symbols;
		this.represented = symbols.stream()
				.mapToDouble(Symbol::numRepresented)
				.reduce(1, (l1, l2) -> l1*l2);
	}
	
	@Override
	public String evaluate() {
		return symbols.stream()
				.map(Symbol::evaluate)
				.collect(Collectors.joining());
	}
	
	@Override
	public int match(String string) {
		int stringIndex = 0;
		for (Symbol symbol: symbols) {
			int matchIndex = symbol.match(string.substring(stringIndex));
			if (matchIndex < 0) return -1;
			stringIndex += matchIndex;
		}
		return stringIndex;
	}
	
	@Override
	public double numRepresented() {
		return represented;
	}
	
}
