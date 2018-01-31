package com.lesserhydra.nemesis;

import com.lesserhydra.bukkitutil.EntityUtils;
import com.lesserhydra.nemesis.namers.MonsterNamers;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class NemesisListener implements Listener {
	
	private static final UUID nemesisAttID = UUID.fromString("8ebe07b9-b10b-4561-ae0b-9e4e92cfac8c");
	private static final String nemesisAttName = "nemesisIdentifier";
	
	
	//Name mob on player kill
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onMonsterKillPlayer(PlayerDeathEvent event) {
		//Get the killer monster, if exists
		LivingEntity monster = EntityUtils.getKiller(event.getEntity());
		if (monster == null) return;
		
		//Make sure we have a generator for this entity type
		if (!MonsterNamers.hasNamer(monster.getType())) return;
		
		//Heal nemesis
		monster.setHealth(monster.getMaxHealth());
		if (monster instanceof PigZombie) ((PigZombie) monster).setAnger(32767);
		
		//If already named, stop
		if (monster.getCustomName() != null) return;
		
		//Give name and set as nemesis
		String originalName = monster.getName();
		makeNemesis(monster, MonsterNamers.generateName(monster));
		
		//Change death message
		String message = event.getDeathMessage();
		event.setDeathMessage(message.replace(originalName, monster.getCustomName()));
	}

	//Nemesis death message
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onNemesisDeath(EntityDeathEvent event) {
		if (!isNemesis(event.getEntity())) return;
		Bukkit.broadcastMessage(generateDeathMessage(event.getEntity()));
	}
	
	private String generateDeathMessage(LivingEntity entity) {
		Player killerPlayer = entity.getKiller();
		return entity.getCustomName() + (killerPlayer == null
				? " died"
				: " was slain by " + killerPlayer.getDisplayName());
	}
	
	private boolean isNemesis(LivingEntity entity) {
		if (entity.getCustomName() == null) return false;
		return entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().stream()
				.filter(mod -> mod.getUniqueId().equals(nemesisAttID))
				.anyMatch(mod -> mod.getName().equals(nemesisAttName));
	}
	
	private void makeNemesis(LivingEntity entity, String generatedName) {
		entity.setCustomName(generatedName);
		entity.setRemoveWhenFarAway(false);
		entity.setCanPickupItems(true);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(
				new AttributeModifier(nemesisAttID, nemesisAttName, 0, Operation.ADD_NUMBER));
	}

}
