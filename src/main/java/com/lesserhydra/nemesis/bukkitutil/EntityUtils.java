package com.lesserhydra.bukkitutil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityUtils {
	
	/**
	 * Gets an attacking LivingEntity from the damager Entity. Looks for the
	 * projectile source if damager is a projectile.
	 * @param damager Damager from EntityDamagedByEntityEvent
	 * @return The living entity that caused the damage event, or null
	 */
	public static LivingEntity getAttacker(Entity damager) {
		//Damager is living
		if (damager instanceof LivingEntity) return (LivingEntity) damager;
		
		//Damager is projectile
		if (damager instanceof Projectile) {
			ProjectileSource source = ((Projectile) damager).getShooter();
			if (source instanceof LivingEntity) return (LivingEntity) source;
		}
		
		//No living attacker found
		return null;
	}
	
	/**
	 * Gets a living entity's killer from last damage event.
	 * @param died The entity that died
	 * @return The living entity that caused the last damage event, or null
	 */
	public static LivingEntity getKiller(LivingEntity died) {
		if (!(died.getLastDamageCause() instanceof EntityDamageByEntityEvent)) return null;
		return getAttacker( ((EntityDamageByEntityEvent)died.getLastDamageCause()).getDamager() );
	}
	
}
