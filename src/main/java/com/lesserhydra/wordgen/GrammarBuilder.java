package com.lesserhydra.wordgen;

import com.lesserhydra.util.ImmutablePair;
import com.lesserhydra.util.Pair;
import com.lesserhydra.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
				.forEachRemaining(this::parseRuleLine);
		return new NameGenerator(ruleMap.get(rootRule));
	}
	
	private String rootRule;
	private final Deque<Pair<String, String>> ruleStrings = new LinkedList<>();
	private final Map<String, Rule> ruleMap = new HashMap<>();
	
	private void parseRuleLine(Pair<String, String> pair) {
		String symbolString = pair.getLeft();
		String ruleString = pair.getRight();
		
		ruleMap.put(symbolString, buildRule(ruleString));
	}

	
	private Rule buildRule(String ruleString) {
		Collection<List<Symbol>> choices = new LinkedList<>();
		parseString(ruleString, '|', s -> choices.add(buildSymbols(s)));
		return new Rule(choices);
	}
	
	private List<Symbol> buildSymbols(String string) {
		List<Symbol> result = new LinkedList<>();
		parseString(string, ' ', str -> result.add(mapSymbol(str)));
		return result;
	}
	
	private Symbol mapSymbol(String symbolString) {
		symbolString = symbolString.trim();
		
		StringBuilder builder = new StringBuilder();
		int openBraces = 0;
		boolean escape = false;
		
		Symbol workingSymbol = null;
		
		for (int i = 0; i < symbolString.length(); ++i) {
			char c = symbolString.charAt(i);
			
			//Escaped characters
			if (escape) {
				escape = false;
				if (openBraces == 0 && c == 's') c = ' '; //Space
			}
			
			//Open group
			else if (c == '(') {
				if ((openBraces == 0 && builder.length() > 0) || workingSymbol != null) throw makeParseError("Unescaped open parentheses inside of symbol", symbolString, i);
				++openBraces;
				if (openBraces == 1) continue;
			}
			
			//Close group
			else if (c == ')') {
				if (openBraces == 0) throw makeParseError("Unmatched close parentheses", symbolString, i);
				--openBraces;
				if (openBraces == 0) {
					workingSymbol = buildRule(builder.toString());
					builder = new StringBuilder();
					continue;
				}
			}
			
			//Set escape
			else if (c == '\\') {
				escape = true;
				if (openBraces == 0) continue;
			}
			
			//Optional
			else if (openBraces == 0) {
				if (c == '?') {
					if (workingSymbol == null) {
						workingSymbol = getSymbol(builder.toString());
						builder = new StringBuilder();
					}
					workingSymbol = new OptionalSymbol(workingSymbol);
					continue;
				}
				else if (c == '[') {
					if (workingSymbol == null) {
						workingSymbol = getSymbol(builder.toString());
						builder = new StringBuilder();
					}
					int end = symbolString.indexOf(']', i+1);
					if (end == -1) throw makeParseError("Unclosed open square bracket", symbolString, i);
					String probString = symbolString.substring(i+1, end);
					if (!StringUtil.isFloat(probString)) throw makeParseError("Invalid probability string, expected float", symbolString, i+1);
					double prob = Double.parseDouble(probString);
					if (prob < 0 || prob > 1) throw makeParseError("Invalid probability [0, 1]", symbolString, i+1);
					workingSymbol = new POptionalSymbol(workingSymbol, prob);
					i = end;
					continue;
				}
			}
			
			if (workingSymbol != null) throw makeParseError("Non-special character after symbol finish", symbolString, i);
			builder.append(c);
		}
		
		//Construct symbol if not already done, return result
		return workingSymbol == null ? getSymbol(builder.toString()) : workingSymbol;
	}
	
	private Symbol getSymbol(String symbolString) {
		Rule rule = ruleMap.get(symbolString);
		return rule != null ? rule : new TerminalSymbol(symbolString);
	}
	
	private static void parseString(String string, char seperator, Consumer<String> consumer) {
		StringBuilder builder = new StringBuilder();
		
		int openBraces = 0;
		boolean escape = false;
		
		for (int i = 0; i < string.length(); ++i) {
			char c = string.charAt(i);
			
			if (escape) escape = false;
			else if (c == seperator && openBraces == 0) {
				consumer.accept(builder.toString());
				builder = new StringBuilder();
				continue;
			}
			else if (c == '\\') escape = true;
			else if (c == '(') ++openBraces;
			else if (c == ')') {
				if (openBraces == 0) throw makeParseError("Unmatched close parentheses", string, i);
				--openBraces;
			}
			
			builder.append(c);
		}
		
		if (openBraces != 0) throw makeParseError("Unmatched open parentheses", string, string.length());
		consumer.accept(builder.toString());
	}
	
	private static GrammarParseException makeParseError(String message, String line, int index) {
		String errorMessage = message + '\n'
				+ line + '\n'
				+ StringUtils.repeat(" ", index) + "^";
		return new GrammarParseException(errorMessage);
	}
	
}
