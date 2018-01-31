package com.lesserhydra.nemesis.namers;

import com.lesserhydra.nemesis.util.MapBuilder;
import com.lesserhydra.nemesis.wordgen.NameGenerator;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.EnumMap;
import java.util.Map;

public class MonsterNamers {
	
	private static NameGenerator vexNamer =
			NameGenerator.build("(v|z|k|y|x)? (i|e|y) (i|e|g)? (k|x) (e (s|z)[0.2])[0.2]");
	
	private static final Map<EntityType, MonsterNamer> namerMap =
			MapBuilder.init(() -> new EnumMap<EntityType, MonsterNamer>(EntityType.class))
			//Zombies
			.put(EntityType.ZOMBIE, new ZombieNamer())
			.put(EntityType.ZOMBIE_VILLAGER, new ZombieNamer())
			.put(EntityType.PIG_ZOMBIE, new ZombieNamer())
			.put(EntityType.HUSK, new ZombieNamer())
			//Skeletons
			.put(EntityType.SKELETON, new SkeletonNamer())
			.put(EntityType.STRAY, new SkeletonNamer())
			.put(EntityType.WITHER_SKELETON, new SkeletonNamer())
			//Spiders
			.put(EntityType.SPIDER, new SpiderNamer())
			.put(EntityType.CAVE_SPIDER, new SpiderNamer())
			//Illagers
			.put(EntityType.VINDICATOR, new VindicatorNamer())
			.put(EntityType.EVOKER, new VindicatorNamer())
			.put(EntityType.VEX, e -> StringUtils.capitalize(vexNamer.next()) + " the Vex")
			//Other
			.put(EntityType.WITCH, new WitchNamer())
			.buildImmutable();
	
	public static boolean hasNamer(EntityType type) {
		return namerMap.containsKey(type);
	}
	
	public static String generateName(LivingEntity entity) {
		MonsterNamer namer = namerMap.get(entity.getType());
		assert namer != null;
		return namer.generateName(entity);
	}
	
}
