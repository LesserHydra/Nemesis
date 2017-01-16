package com.lesserhydra.wordgen;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class WordGeneratorTest {
	
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
	}*/
	
	@Test public void escapes() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B\\? | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "1|2|3\\{")
				.build();
		//4 * 1 + 3
		assertEquals(7, (long) generator.numPossible());
	}
	
	@Test public void doubleEscapes() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A B\\\\? | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "1|2|3\\{")
				.build();
		//4 * 2 + 3
		assertEquals(11, (long) generator.numPossible());
	}
	
	@Test public void groupings() {
		NameGenerator generator = NameGenerator.builder()
				.rule("First", "A (B | C \\| C)? | C")
				.rule("A", "1|2|3|4")
				.rule("B", "1|2|3|4|5")
				.rule("C", "1|2|3")
				.build();
		//4 * (5 + [3 * 1 * 3] + 1) + 3
		assertEquals(63, (long) generator.numPossible());
	}
	
}
