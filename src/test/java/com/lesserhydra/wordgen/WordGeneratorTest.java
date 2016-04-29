package com.lesserhydra.wordgen;

import static org.junit.Assert.*;
import java.util.stream.Stream;
import org.junit.Test;

public class WordGeneratorTest {
	
	@Test public void skeleton() {
		NameGenerator generator = NameGenerator.builder()
				//88 * 10 * 33 = 29040
				.rule("Name", "One Two Three")
				//20 + 12 + 10 + 6 + 5 + 10 + 20 + 5 = 88
				.rule("One", "Ash? Th Vowel | Vowel? Th Ash | Ash? l Vowel | Vowel? l Ash | Vowel | Ash? Wynn Vowel | Vowel!u? Wynn Vowel!u | Vowel!u? Wynn Ash")
				//1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 2 = 10
				.rule("Two", "l | k | t | p | b | c | d | Eng | Th")
				//2 + 2 + 5 + 1 + 1 + 1 + 10 + 5 + 6 = 33
				.rule("Three", "Ou s | i Ou | Vowel l | ue | Ethel | Ash | Vowel Th | Vowel Eng | VowelAsh Yogh")
				.rule("VowelAsh", "Vowel | Ash")
				.rule("Vowel", "Ae|i|Ou")
				.rule("Vowel!u", "Ae|i|o")
				.rule("Ae", "a|e")
				.rule("Ao", "a|o")
				.rule("Ou", "o|u")
				.rule("Th", "Eth | Thorn")
				.rule("Eth", "th")//"\u00F0")
				.rule("Thorn", "th")//"\u00FE")
				.rule("Ash", "\u00E6")
				.rule("Ethel", "\u0153")
				.rule("Eng", "ng")//"\u014B")
				.rule("Yogh", "gh")//"\u021D")
				.rule("Wynn", "w")//"\u01BF")
				.build();
		assertEquals(29040, (long) generator.numPossible());
		
		Stream.generate(generator::next)
				.limit(5)
				.map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
				.forEachOrdered(System.out::println);
	}
	
	@Test public void spider() {
		NameGenerator generator = NameGenerator.builder()
				//50 * 42 * 38 = 79,800
				.rule("Name", "One Two Three")
				//6 + 8 + 12 + 24 = 50
				.rule("One", "S Kt | X Ee | Ee Ee? S | Ee Ee? X")
				//9 + 9 + 12 + 12 = 42
				.rule("Two", "i Vz A? | i Ph A? | A Vz i? | A Ph i?")
				//6 + 12 + 20 = 38
				.rule("Three", "S? Ee | Kt? Kt | X? Kt? i")
				.rule("S", "s|ss")
				.rule("X", "x|Kt")
				.rule("Kt", "c|k|t")
				.rule("Ee", "e|i")
				.rule("Vz", "v|z|vz")
				.rule("Ph", "f|ph|th")
				.rule("A", "a|o")
				.build();
		assertEquals(79800, (long) generator.numPossible());
		
		Stream.generate(generator::next)
				.limit(5)
				.map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
				.forEachOrdered(System.out::println);
	}
	
	@Test public void witch() {
		NameGenerator generator = NameGenerator.builder()
				//38 * 172 * 4 = 26,144
				.rule("Name", "One Two Ia")
				//12 + 3 + 1 + 3 + 4 + 9 + 6 = 38
				.rule("One", "Mn A Th | t V? | th | Mn | V L | Wy V? | Wy Th")
				//32 + 90 + 30 + 20 = 172
				.rule("Two", "A L Three | A? u M? ThreeL | Ai ThreeL | e r? ThreeL")
				// 8 + 2 = 10
				.rule("ThreeL", "Three | L")
				//2 + 2 + 2 + 2 = 8
				.rule("Three", "M|Th|W|D")
				.rule("Wy", "W|wy")
				.rule("W", "w|y")
				.rule("Mn", "M|mn")
				.rule("M", "m|n")
				.rule("V", "v|z")
				.rule("Ia", "A|ia|aa")
				.rule("Ai", "A|ai")
				.rule("A", "a|i")
				.rule("Th", "t|th")
				.rule("L", "l|ll")
				.rule("D", "d|g")
				.build();
		assertEquals(26144, (long) generator.numPossible());
		
		Stream.generate(generator::next)
				.limit(5)
				.map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
				.forEachOrdered(System.out::println);
	}
	
	@Test public void numPossible() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "1|2|3")
				.build();
		//4 * 5 + 3
		assertEquals(23, (long) generator.numPossible());
	}
	
	@Test public void blankRules() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B | C")
				.rule("A", "1|2|3|4")
				.rule("B", "")
				.rule("C", "")
				.build();
		//4 * 1 + 1
		assertEquals(5, (long) generator.numPossible());
	}
	
	@Test public void blankSymbols() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B | C")
				.rule("A", "1|2|3|4")
				.rule("B", "|1|2|3|4|5")
				.rule("C", "1|2|3|")
				.build();
		//4 * 6 + 4
		assertEquals(28, (long) generator.numPossible());
	}
	
	@Test public void extraWhitespace() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B | C  ")
				.rule("A", "1     ")
				.rule("B", "0  |1|		2 ")
				.rule("C", "1  0|      20|30|			45")
				.build();
		//1 * 3 + 4
		assertEquals(7, (long) generator.numPossible());
		assertEquals(2, generator.next().length());
	}
	
	@Test public void loops() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B | C")
				.rule("A", "B")
				.rule("B", "C")
				.rule("C", "A")
				.build();
		//1 + 1
		assertEquals(2, (long) generator.numPossible());
	}
	
	@Test public void rulesWithinRules() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5|C")
				.rule("C", "1|2|3")
				.build();
		//4 * 9 + 3
		assertEquals(35, (long) generator.numPossible());
	}
	
	@Test public void optionalSymbols() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A A? B | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "1|2?|3")
				.build();
		//4 * (4+1) * 5 + 4
		assertEquals(104, (long) generator.numPossible());
	}
	
	/*@Test public void repeatedSymbols() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A{3} B | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "1|2|3")
				.build();
		//4^3 * 5 + 3
		assertEquals(323, (long) generator.numPossible());
	}
	
	@Test public void rangedSymbols() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A{0,3} B | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "1|2|3")
				.build();
		//(1 + 4 + 4^2 + 4^3) * 5 + 3
		assertEquals(428, (long) generator.numPossible());
	}
	
	@Test public void escapes() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B\\? | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "1|2|3\\{")
				.build();
		//4 * 1 + 3
		assertEquals(7, (long) generator.numPossible());
	}*/
	
}
