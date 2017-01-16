package com.lesserhydra.wordgen;

import java.util.Random;

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
	public double numRepresented() {
		return 1 + symbol.numRepresented();
	}
	
}
