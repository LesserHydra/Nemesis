package com.lesserhydra.nemesis;

import org.bukkit.entity.LivingEntity;

interface MonsterNamer {
	
	public String generateName();
	
	public String generateTitle(LivingEntity entity);
	
}
