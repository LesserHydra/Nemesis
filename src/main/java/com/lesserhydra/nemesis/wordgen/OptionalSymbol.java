package com.lesserhydra.nemesis.wordgen;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OptionalSymbol implements Symbol {

	private final Random rand = new Random();
	private final Symbol symbol;
	
	public OptionalSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public String evaluate() {
		return (rand.nextDouble() * numRepresented() < 1 ? "" : symbol.evaluate());
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
