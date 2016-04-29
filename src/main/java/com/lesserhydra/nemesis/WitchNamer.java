package com.lesserhydra.nemesis;

import org.bukkit.entity.LivingEntity;
import com.lesserhydra.wordgen.NameGenerator;

public class WitchNamer implements MonsterNamer {
	
	//26144 possibilities
	private static final NameGenerator nameGenerator = NameGenerator.builder()
			.rule("Name", "One Two Ia")
			.rule("One", "Mn A Th | t V? | th | Mn | V L | Wy V? | Wy Th")
			.rule("Two", "A L Three | A? u M? ThreeL | Ai ThreeL | e r? ThreeL")
			.rule("ThreeL", "Three | L")
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
	
	@Override
	public String generateName() {
		return nameGenerator.next();
	}

	@Override
	public String generateTitle(LivingEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
