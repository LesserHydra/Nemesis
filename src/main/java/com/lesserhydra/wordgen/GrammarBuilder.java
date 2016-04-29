package com.lesserhydra.wordgen;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.lesserhydra.util.ImmutablePair;
import com.lesserhydra.util.Pair;

/**
 * Aids in creation of a NameGenerator.
 */
public class GrammarBuilder {
	
	/**
	 * Adds a rule to the grammar. The rule will not be parsed until the grammar is {@link #build() built}.
	 * @param symbolString String used to identify this rule in preceding rules; may not be empty
	 * @param ruleString String representing the rule
	 * @return Itself
	 * @throws IllegalArgumentException If symbolString is null or empty, or ruleString is null
	 */
	public GrammarBuilder rule(String symbolString, String ruleString) {
		if (symbolString == null || symbolString.isEmpty()) throw new IllegalArgumentException("Symbol string may not be null or empty.");
		if (ruleString == null) throw new IllegalArgumentException("Rule string may not be null.");
		
		if (rootRule == null) rootRule = symbolString;
		ruleStrings.add(new ImmutablePair<>(symbolString, ruleString));
		return this;
	}
	
	/**
	 * Parses each rule and constructs a NameGenerator.
	 * @return The resulting NameGenerator
	 * @throws GrammarParseException If a problem occurs during parsing
	 */
	public NameGenerator build() {
		ruleStrings.descendingIterator()
				.forEachRemaining(this::buildRule);
		return new NameGenerator(ruleMap.get(rootRule));
	}
	
	private String rootRule;
	private final Deque<Pair<String, String>> ruleStrings = new LinkedList<>();
	private final Map<String, Rule> ruleMap = new HashMap<>();
	
	//TODO: Exceptions
	private void buildRule(Pair<String, String> pair) {
		String symbolString = pair.getLeft();
		String ruleString = pair.getRight();
		
		Collection<List<Symbol>> choices = Stream.of(ruleString.split("\\|", -1))
				.map(this::buildSymbols)
				.collect(Collectors.toList());
		ruleMap.put(symbolString, new Rule(choices));
	}
	
	private List<Symbol> buildSymbols(String string) {
		return Stream.of(string.split("\\s+", -1))
				.map(this::mapSymbol)
				.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
	}
	
	private Symbol mapSymbol(String symbolString) {
		if (!symbolString.endsWith("?")) return getSymbol(symbolString);
		Symbol symbol = getSymbol(symbolString.substring(0, symbolString.length() - 1));
		return new OptionalSymbol(symbol);
	}
	
	private Symbol getSymbol(String symbolString) {
		Rule rule = ruleMap.get(symbolString);
		return rule != null ? rule : new TerminalSymbol(symbolString);
	}
	
}
