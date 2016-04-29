package com.lesserhydra.nemesis;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import com.lesserhydra.bukkitutil.EntityUtils;


public class NemesisListener implements Listener {
	//Name mob on player kill
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMonsterKillPlayer(PlayerDeathEvent event) {
		LivingEntity killer = EntityUtils.getKiller(event.getEntity());
		if (!(killer instanceof Zombie || killer instanceof Skeleton)) return;
		Monster monster = (Monster) killer;
		if (monster.getCustomName() != null) return;

		String originalName = monster.getName();
		monster.setCustomName(MonsterNamers.generateName(monster));
		monster.setRemoveWhenFarAway(false);
		monster.setCanPickupItems(true);
		monster.setHealth(monster.getMaxHealth());
		
		//Change death message
		String message = event.getDeathMessage();
		event.setDeathMessage(message.replace(originalName, monster.getCustomName()));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMonsterSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() != SpawnReason.SPAWNER_EGG) return;
		if (!(event.getEntity() instanceof Monster)) return;
		Monster monster = (Monster) event.getEntity();
		monster.setCustomName(MonsterNamers.generateName(monster));
	}

	//Nemesis death message
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onNemesisDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Monster)) return;
		Monster nemesis = (Monster) event.getEntity();
		String name = nemesis.getCustomName(); 
		if (name == null) return;
		Bukkit.broadcastMessage(name + " has been defeated.");
	}

}
