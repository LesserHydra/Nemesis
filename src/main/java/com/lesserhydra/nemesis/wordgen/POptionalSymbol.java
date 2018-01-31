package com.lesserhydra.nemesis.wordgen;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class POptionalSymbol implements Symbol {

	private final Random rand = new Random();
	private final Symbol symbol;
	private final double probability;
	
	POptionalSymbol(Symbol symbol, double probability) {
		this.symbol = symbol;
		this.probability = probability;
	}
	
	@Override
	public String evaluate() {
		return (rand.nextDouble() < probability ? symbol.evaluate() : "");
	}
	
	@Override
	public Set<Integer> match(String string) {
		Set<Integer> result = new HashSet<>(symbol.match(string));
		result.add(0);
		return result;
	}
	
	@Override
	public double numRepresented() {
		return 1 + symbol.numRepresented();
	}
	
}
