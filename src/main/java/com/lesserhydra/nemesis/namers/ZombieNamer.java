package com.lesserhydra.nemesis.namers;

import com.lesserhydra.wordgen.NameGenerator;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.LivingEntity;

class ZombieNamer implements MonsterNamer {
	
	@Override
	public String generateName(LivingEntity entity) {
		String name = nameGenerator.next();
		String title = RatingType.getMostRelevant(entity).getRandomTitle();
		return StringUtils.capitalize(name) + " the " + title;
	}
	
	private static final NameGenerator nameGenerator = NameGenerator.builder()
			.rule("Name", "First End")
			.rule("First", "FA r | FB l | FC FV r")
			.rule("FA", "a|b|d|g|k|o|s")
			.rule("FB", "b|g|k|m|o|s")
			.rule("FC", "b|d|g|k|m|n|r|s")
			.rule("FV", "a|o")
			.rule("End", "EV? EA EV EB E | EA? EV EB E | EV EB EV? EA E1 | EB? EV EA E1 | EA? EB EV E2")
			.rule("EA", "k|r|th|ch|d|EB")
			.rule("EB", "p|g|n")
			.rule("EV", "a|o|u")
			.rule("E", "h|t|r")
			.rule("E1", "l|r")
			.rule("E2", "l|r|b|g|d")
			.build();
	
}
