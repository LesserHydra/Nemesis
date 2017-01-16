package com.lesserhydra.wordgen;

import com.lesserhydra.util.RandomBag;

import java.util.Collection;

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
	public int match(String string) {
		return possibilities.stream()
				.mapToInt(symbol -> symbol.match(string))
				.max()
				.orElse(-1);
	}
	
	@Override
	public double numRepresented() {
		return represented;
	}
	
}
