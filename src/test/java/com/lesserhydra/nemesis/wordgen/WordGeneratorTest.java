package com.lesserhydra.nemesis.wordgen;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordGeneratorTest {
	
	@Test public void numPossible() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "6|7|8")
				.build();
		//4 * 5 + 3
		assertEquals(23, (long) generator.numPossible());
		
		assertTrue(generator.match("15"));
		assertTrue(generator.match("11"));
		assertTrue(generator.match("43"));
		assertTrue(generator.match("7"));
		
		assertFalse(generator.match("1"));
		assertFalse(generator.match("5"));
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
		
		assertTrue(generator.match("A"));
		assertTrue(generator.match("AA"));
	}
	
	@Test public void rulesWithinRules() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5|C")
				.rule("C", "6|7|8")
				.build();
		//4 * 9 + 3
		assertEquals(35, (long) generator.numPossible());
		
		assertTrue(generator.match("47"));
		assertTrue(generator.match("8"));
		
		assertFalse(generator.match("1"));
	}
	
	@Test public void optionalSymbols() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A A? B | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "6|7?|8")
				.build();
		//4 * (4+1) * 5 + 4
		assertEquals(104, (long) generator.numPossible());
		
		assertTrue(generator.match(""));
		assertTrue(generator.match("7"));
		assertTrue(generator.match("11"));
		assertTrue(generator.match("111"));
	}
	
	@Test public void repeatedSymbols() {
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
	
	@Test public void combinedSymbols() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A?[0.5]{0,3}? B | C{1,2}{0,3}")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "1|2|3")
				.build();
		//(1 + 1 + 6 + 6^2 + 6^3) * 5 + (1 + [3 + 3^2] + [3 + 3^2]^2 + [3 + 3^2]^3)
		assertEquals(3185, (long) generator.numPossible());
	}
	
	@Test public void escapes() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B\\? | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "6|7|8\\{")
				.build();
		//4 * 1 + 3
		assertEquals(7, (long) generator.numPossible());
		
		assertTrue(generator.match("1B?"));
		assertTrue(generator.match("8{"));
		
		assertFalse(generator.match("1"));
		assertFalse(generator.match("11"));
	}
	
	@Test public void doubleEscapes() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B\\\\? | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "6|7|8\\{")
				.build();
		//4 * 2 + 3
		assertEquals(11, (long) generator.numPossible());
		
		assertTrue(generator.match("1B\\"));
		assertTrue(generator.match("1"));
		
		assertFalse(generator.match("11"));
	}
	
	@Test public void groupings() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A (B | C \\| C)? | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "6|7|8")
				.build();
		//4 * (5 + [3 * 1 * 3] + 1) + 3
		assertEquals(63, (long) generator.numPossible());
		
		assertTrue(generator.match("1"));
		assertTrue(generator.match("15"));
		assertTrue(generator.match("18|8"));
		assertTrue(generator.match("8"));
		
		assertFalse(generator.match("5"));
		assertFalse(generator.match("18"));
	}
	
	@Test public void match1() {
		NameGenerator generator = NameGenerator.builder()
				.rule("S", "B A{3} | A{3} | A{4} | A{3} B | B{3} | B B A{3} B B")
				.rule("A", "00|0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|a")
				.rule("B", "|a|aa|aaa|b|bb|bbb")
				.build();
		
		assertTrue(generator.match("000"));
		assertTrue(generator.match("aaa0000aaa"));
		assertTrue(generator.match("2020"));
		assertTrue(generator.match("2020ab"));
		assertTrue(generator.match("aba"));
		assertTrue(generator.match("aabbaaa"));
		assertTrue(generator.match("aba20"));
		
		assertFalse(generator.match("ab20"));
		assertFalse(generator.match("bab20"));
		assertFalse(generator.match("00"));
		assertFalse(generator.match("aabab"));
	}
	
	@Test public void match2() {
		NameGenerator generator = NameGenerator.builder()
				.rule("Full", "Name \\s? :\\s Phone")
				.rule("Name", "UChar Word{1,9}")
				.rule("Phone", "AreaCode? \\s? Digit{3} \\s? (- \\s?)? Digit{4}")
				.rule("AreaCode", "\\( Digit{3} \\) | Digit{3} \\s? -?")
				.rule("Word", "Digit|LChar|UChar|-|_")
				.rule("UChar", "A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z")
				.rule("LChar", "a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z")
				.rule("Digit", "0|1|2|3|4|5|6|7|8|9")
				.build();
		
		assertTrue(generator.match("BB: (123)555-9321"));
		assertTrue(generator.match("Bob: (123)555-9321"));
		assertTrue(generator.match("Bobbington: (123)555-9321"));
		assertTrue(generator.match("Joe: (123) 555 - 9321"));
		assertTrue(generator.match("Phil: 123-555-9321"));
		assertTrue(generator.match("Jackson: 1235559321"));
		assertTrue(generator.match("Abraham: 555-9321"));
		
		assertFalse(generator.match("bob: (123)555-9321"));
		assertFalse(generator.match("B: (123)555-9321"));
		assertFalse(generator.match("Bobbingtonn: (123)555-9321"));
		assertFalse(generator.match("Bob: 123)555-9321"));
		assertFalse(generator.match("Bob: (12)555-9321"));
		assertFalse(generator.match("Bob"));
	}
	
}
