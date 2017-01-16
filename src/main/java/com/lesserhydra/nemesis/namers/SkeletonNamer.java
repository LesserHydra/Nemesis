package com.lesserhydra.nemesis.namers;

import com.lesserhydra.wordgen.NameGenerator;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.LivingEntity;

class SkeletonNamer implements MonsterNamer {
	
	@Override
	public String generateName(LivingEntity entity) {
		String name = nameGenerator.next();
		String title = RatingType.getMostRelevant(entity).getRandomTitle();
		return StringUtils.capitalize(name) + " the " + title;
	}
	
	private static final NameGenerator nameGenerator = NameGenerator.builder()
			//88 * 10 * 33 = 29040
			.rule("Name", "One Two Three")
			//20 + 12 + 10 + 6 + 5 + 10 + 20 + 5 = 88
			.rule("One", "Ash? Th Vowel | Vowel? Th Ash | Ash? l Vowel | Vowel? l Ash | Vowel | Ash? Wynn Vowel | Aeio? Wynn Aeio | Aeio? Wynn Ash")
			//1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 2 = 10
			.rule("Two", "l | k | t | p | b | c | d | Eng | Th")
			//2 + 2 + 5 + 1 + 1 + 1 + 10 + 5 + 6 = 33
			.rule("Three", "Ou s | i Ou | Vowel l | ue | Ethel | Ash | Vowel Th | Vowel Eng | VowelAsh Yogh")
			.rule("VowelAsh", "Vowel | Ash")
			.rule("Vowel", "Ae|i|Ou")
			.rule("Aeio", "Ae|i|o")
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
	
}
