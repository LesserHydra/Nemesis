package com.lesserhydra.nemesis.wordgen;

import com.lesserhydra.nemesis.util.ImmutablePair;
import com.lesserhydra.nemesis.util.Pair;
import com.lesserhydra.nemesis.util.StringUtil;
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
	private final Map<String, Symbol> ruleMap = new HashMap<>();
	
	private void parseRuleLine(Pair<String, String> pair) {
		String symbolString = pair.getLeft();
		String ruleString = pair.getRight();
		
		ruleMap.put(symbolString, buildRule(ruleString));
	}

	
	//Parse choices; handles OR'd possibilities
	private ChoiceSymbol buildRule(String ruleString) {
		Collection<Symbol> choices = new LinkedList<>();
		parseString(ruleString, '|', s -> choices.add(buildListSymbol(s)));
		return new ChoiceSymbol(choices);
	}
	
	//Parse single choice; links symbols in a list
	private ListSymbol buildListSymbol(String string) {
		List<Symbol> resultSymbols = new LinkedList<>();
		parseString(string, ' ', str -> resultSymbols.add(mapSymbol(str)));
		return new ListSymbol(resultSymbols);
	}
	
	//Parse single symbol
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
				if ((openBraces == 0 && builder.length() > 0) || workingSymbol != null) throw new GrammarParseException(makeError("Unescaped open parentheses inside of symbol", symbolString, i));
				++openBraces;
				if (openBraces == 1) continue;
			}
			
			//Close group
			else if (c == ')') {
				if (openBraces == 0) throw new GrammarParseException(makeError("Unmatched close parentheses", symbolString, i));
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
			
			else if (openBraces == 0) {
				//Optional
				if (c == '?') {
					if (workingSymbol == null) {
						workingSymbol = getSymbol(builder.toString());
						builder = new StringBuilder();
					}
					workingSymbol = new OptionalSymbol(workingSymbol);
					continue;
				}
				//Probability
				else if (c == '[') {
					if (workingSymbol == null) {
						workingSymbol = getSymbol(builder.toString());
						builder = new StringBuilder();
					}
					int end = symbolString.indexOf(']', i+1);
					if (end == -1) throw new GrammarParseException(makeError("Unclosed open square bracket", symbolString, i));
					String probString = symbolString.substring(i+1, end);
					if (!StringUtil.isFloat(probString)) throw new GrammarParseException(makeError("Invalid probability string, expected float", symbolString, i+1));
					double prob = Double.parseDouble(probString);
					if (prob < 0 || prob > 1) throw new GrammarParseException(makeError("Invalid probability [0, 1]", symbolString, i+1));
					workingSymbol = new POptionalSymbol(workingSymbol, prob);
					i = end;
					continue;
				}
				//Ranges
				else if (c == '{') {
					if (workingSymbol == null) {
						workingSymbol = getSymbol(builder.toString());
						builder = new StringBuilder();
					}
					int end = symbolString.indexOf('}', i+1);
					if (end == -1) throw new GrammarParseException(makeError("Unclosed open bracket", symbolString, i));
					//Get arg string
					String rangeString = symbolString.substring(i+1, end);
					//Get two args, second optional
					String[] rangeArgs = rangeString.split(",");
					if (rangeArgs.length > 2) throw new GrammarParseException(makeError("Too many range arguments", symbolString, i+1));
					String minRangeStr = rangeArgs[0];
					String maxRangeStr = rangeArgs.length == 2 ? rangeArgs[1] : rangeArgs[0];
					//Expect args to be positive integers
					if (!StringUtil.isInteger(minRangeStr)) throw new GrammarParseException(makeError("Invalid range minimum, expected positive int", symbolString, i+1));
					if (!StringUtil.isInteger(maxRangeStr)) throw new GrammarParseException(makeError("Invalid range maximum, expected positive int", symbolString, i+1));
					//Parse ints
                  	int minRange = Integer.parseInt(minRangeStr);
					int maxRange = Integer.parseInt(maxRangeStr);
					if (minRange < 0) throw new GrammarParseException(makeError("Invalid range minimum, expected positive int", symbolString, i+1));
					if (maxRange < minRange) throw new GrammarParseException(makeError("Invalid range maximum, must be greater or equal to minimum", symbolString, i+1));
					//Build symbol
					workingSymbol = new RangedSymbol(workingSymbol, minRange, maxRange);
					//Update index
					i = end;
					continue;
				}
			}
			
			if (workingSymbol != null) throw new GrammarParseException(makeError("Non-special character after symbol finish", symbolString, i));
			builder.append(c);
		}
		
		//Construct symbol if not already done, return result
		return workingSymbol == null ? getSymbol(builder.toString()) : workingSymbol;
	}
	
	private Symbol getSymbol(String symbolString) {
		return ruleMap.getOrDefault(symbolString, new TerminalSymbol(symbolString));
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
				if (openBraces == 0) throw new GrammarParseException(makeError("Unmatched close parentheses", string, i));
				--openBraces;
			}
			
			builder.append(c);
		}
		
		if (openBraces != 0) throw new GrammarParseException(makeError("Unmatched open parentheses", string, string.length()));
		consumer.accept(builder.toString());
	}
	
	private static String makeError(String message, String line, int index) {
		return message + '\n'
				+ line + '\n'
				+ StringUtils.repeat(" ", index) + "^";
	}
	
}
