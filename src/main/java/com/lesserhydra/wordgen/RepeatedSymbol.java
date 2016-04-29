package com.lesserhydra.wordgen;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepeatedSymbol implements Symbol {
	
	public RepeatedSymbol(Symbol symbol, int n) {
		//TODO: exceptions
		this.symbol = symbol;
		this.n = n;
	}
	
	@Override
	public String evaluate() {
		return Stream.generate(symbol::evaluate)
				.limit(n)
				.collect(Collectors.joining());
	}

	@Override
	public double numRepresented() {
		return Math.pow(symbol.numRepresented(), n);
	}
	
	private final Symbol symbol;
	private final int n;
	
}
