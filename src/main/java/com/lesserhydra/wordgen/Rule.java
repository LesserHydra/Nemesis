package com.lesserhydra.wordgen;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import com.lesserhydra.util.RandomBag;

/**
 * A non-terminal symbol.
 */
class Rule implements Symbol {
	
	public Rule(Collection<List<Symbol>> choices) {
		choices.forEach(c -> possibilities.add(calculateOutcomes(c), c));
		this.represented = choices.stream()
				.mapToDouble(Rule::calculateOutcomes)
				.sum();
	}

	@Override
	public String evaluate() {
		return possibilities.next().stream()
				.map(Symbol::evaluate)
				.collect(Collectors.joining());
	}

	@Override
	public double numRepresented() {
		return represented;
	}
	
	private final RandomBag<List<Symbol>> possibilities = new RandomBag<>();
	private final double represented;
	
	private static double calculateOutcomes(List<Symbol> symbols) {
		return symbols.stream()
			.mapToDouble(Symbol::numRepresented)
			.reduce(1, (l1, l2) -> l1*l2);
	}
	
}
