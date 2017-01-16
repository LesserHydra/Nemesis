package com.lesserhydra.wordgen;

import java.util.Random;

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
	public int match(String string) {
		int symbolMatch = symbol.match(string);
		return symbolMatch < 0 ? 0 : symbolMatch;
	}
	
	@Override
	public double numRepresented() {
		return 1 + symbol.numRepresented();
	}
	
}
