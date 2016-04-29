package com.lesserhydra.nemesis;

import org.bukkit.entity.LivingEntity;
import com.lesserhydra.wordgen.NameGenerator;

public class SpiderNamer implements MonsterNamer {
	
	@Override
	public String generateName() {
		return nameGenerator.next();
	}

	@Override
	public String generateTitle(LivingEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//79800 possibilities
	private static final NameGenerator nameGenerator = NameGenerator.builder()
			.rule("Name", "One Two Three")
			.rule("One", "S Kt | X Ee | Ee Ee? S | Ee Ee? X")
			.rule("Two", "i Vz A? | i Ph A? | A Vz i? | A Ph i?")
			.rule("Three", "S? Ee | Kt? Kt | X? Kt? i")
			.rule("S", "s|ss")
			.rule("X", "x|Kt")
			.rule("Kt", "c|k|t")
			.rule("Ee", "e|i")
			.rule("Vz", "v|z|vz")
			.rule("Ph", "f|ph|th")
			.rule("A", "a|o")
			.build();
	
}
