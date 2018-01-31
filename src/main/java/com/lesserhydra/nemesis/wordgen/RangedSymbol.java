package com.lesserhydra.nemesis.wordgen;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import com.lesserhydra.nemesis.util.RandomBag;

public class RangedSymbol implements Symbol {
	
	public RangedSymbol(Symbol symbol, int minimum, int maximum) {
		if (minimum > maximum) throw new GrammarParseException("Range minimum must be less than or equal to maximum.");
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
	public Set<Integer> match(String string) {
		return match_impl(string, 1);
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
	
	private Set<Integer> match_impl(String string, int symbolIndex) {
		//All symbols are finished, result
		if (symbolIndex > maximum) return Collections.singleton(0);
		
		//Run current symbol
		Set<Integer> currentMatches = symbol.match(string);
		if (currentMatches.isEmpty()) {
			if (symbolIndex > minimum) return Collections.singleton(0);
			return Collections.emptySet();
		}
		
		Set<Integer> results = new HashSet<>();
		for (int i : currentMatches) {
			match_impl(string.substring(i), symbolIndex + 1)
					.forEach(n -> results.add(n + i));
		}
		return results;
	}
	
}
