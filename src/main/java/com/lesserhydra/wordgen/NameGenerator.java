package com.lesserhydra.wordgen;

public class NameGenerator {
	
	NameGenerator(Rule root) {
		this.root = root;
	}
	
	public String next() {
		return root.evaluate();
	}
	
	public double numPossible() {
		return root.numRepresented();
	}
	
	public static GrammarBuilder builder() {
		return new GrammarBuilder();
	}
	
	public static NameGenerator build(String ruleString) {
		return builder().rule("S", ruleString).build();
	}
	
	private final Rule root;
	
}
