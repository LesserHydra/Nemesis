package com.lesserhydra.wordgen;

import com.lesserhydra.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents one or more concatenated symbols.
 */
public class ListSymbol implements Symbol {
	
	private final ArrayList<Symbol> symbols;
	private final double represented;
	
	ListSymbol(List<Symbol> symbols) {
		this.symbols = new ArrayList<>(symbols);
		this.represented = symbols.stream()
				.mapToDouble(Symbol::numRepresented)
				.reduce(1, (l1, l2) -> l1*l2);
	}
	
	public ListSymbol(Symbol symbol, int n) {
		//TODO: exceptions
		this.symbols = CollectionUtils.generateCollection(n, () -> symbol, ArrayList::new);
		this.represented = Math.pow(symbol.numRepresented(), n);
	}
	
	@Override
	public String evaluate() {
		return symbols.stream()
				.map(Symbol::evaluate)
				.collect(Collectors.joining());
	}
	
	@Override
	public Set<Integer> match(String string) {
		return match_impl(string, 0);
	}
	
	@Override
	public double numRepresented() {
		return represented;
	}
	
	private Set<Integer> match_impl(String string, int symbolIndex) {
		//All symbols are finished, result
		if (symbolIndex == symbols.size()) return Collections.singleton(0);
		
		//Run current symbols
		Set<Integer> results = new HashSet<>();
		for (int i : symbols.get(symbolIndex).match(string)) {
			match_impl(string.substring(i), symbolIndex + 1)
					.forEach(n -> results.add(n + i));
		}
		return results;
	}
	
}
