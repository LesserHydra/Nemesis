package com.lesserhydra.wordgen;

public class NameGenerator {
	
	private final Symbol root;
	
	NameGenerator(Symbol root) {
		this.root = root;
	}
	
	public String next() {
		return root.evaluate();
	}
	
	public double numPossible() {
		return root.numRepresented();
	}
	
	public boolean match(String s) {
		return root.match(s).stream()
				.anyMatch(i -> i == s.length());
	}
	
	public static GrammarBuilder builder() {
		return new GrammarBuilder();
	}
	
	public static NameGenerator build(String ruleString) {
		return builder().rule("S", ruleString).build();
	}
	
}
