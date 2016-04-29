package com.lesserhydra.nemesis;

import java.util.EnumMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import com.lesserhydra.util.MapBuilder;

public class MonsterNamers {
	
	private static final Map<EntityType, MonsterNamer> namerMap =
			MapBuilder.init(() -> new EnumMap<EntityType, MonsterNamer>(EntityType.class))
			.put(EntityType.ZOMBIE, new ZombieNamer())
			.put(EntityType.SKELETON, new SkeletonNamer())
			.put(EntityType.SPIDER, new SpiderNamer())
			.put(EntityType.CAVE_SPIDER, new SpiderNamer())
			.put(EntityType.WITCH, new WitchNamer())
			.buildImmutable();
	
	public static String generateName(LivingEntity entity) {
		EntityType type = entity.getType();
		MonsterNamer namer = namerMap.get(entity.getType());
		if (namer == null) return null;
		
		String name = namer.generateName();
		
		String title = "";
		if (type == EntityType.ZOMBIE || type == EntityType.SKELETON) {
			title = " the " + RatingType.getMostRelevant(entity).getRandomTitle();
		}
		
		return ChatColor.ITALIC + name.substring(0, 1).toUpperCase() + name.substring(1) + title + ChatColor.RESET;
	}
	
}
