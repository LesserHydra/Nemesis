package com.lesserhydra.wordgen;

import com.lesserhydra.util.RandomBag;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a collection of symbols where one is chosen at random during evaluation.
 */
class ChoiceSymbol implements Symbol {
	
	private final RandomBag<Symbol> possibilities = new RandomBag<>();
	private final double represented;
	
	ChoiceSymbol(Collection<Symbol> choices) {
		choices.forEach(c -> possibilities.add(c.numRepresented(), c));
		this.represented = choices.stream()
				.mapToDouble(Symbol::numRepresented)
				.sum();
	}

	@Override
	public String evaluate() {
		return possibilities.next().evaluate();
	}
	
	@Override
	public Set<Integer> match(String string) {
		return possibilities.stream()
				.flatMap(symbol -> symbol.match(string).stream())
				.collect(Collectors.toSet());
	}
	
	@Override
	public double numRepresented() {
		return represented;
	}
	
}
