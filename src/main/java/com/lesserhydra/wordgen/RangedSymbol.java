package com.lesserhydra.wordgen;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import com.lesserhydra.util.RandomBag;

public class RangedSymbol implements Symbol {
	
	public RangedSymbol(Symbol symbol, int minimum, int maximum) {
		if (minimum >= maximum) throw new GrammarParseException("Range minimum must be less than maximum.");
		this.symbol = symbol;
		this.minimum = minimum;
		this.maximum = maximum;
		initBag();
	}
	
	@Override
	public String evaluate() {
		return Stream.generate(symbol::evaluate)
				.limit(bag.next())
				.collect(Collectors.joining());
	}

	@Override
	public double numRepresented() {
		double a = symbol.numRepresented();
		
		//Special case where the enclosed symbol is deterministic
		if (a == 1) return a * (maximum - minimum + 1);
		//Geometric series
		return (Math.pow(a, minimum) - Math.pow(a, maximum + 1))/(1 - a);
	}
	
	private final RandomBag<Long> bag = new RandomBag<>();
	private final Symbol symbol;
	private final int minimum;
	private final int maximum;
	
	private void initBag() {
		double a = symbol.numRepresented();
		IntStream.rangeClosed(minimum, maximum)
				.forEach(i -> bag.add(Math.pow(a, i), (long) i));
	}
	
}
