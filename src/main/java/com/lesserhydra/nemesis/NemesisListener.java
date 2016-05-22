package com.lesserhydra.nemesis;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import com.lesserhydra.bukkitutil.EntityUtils;


public class NemesisListener implements Listener {
	
	private static final UUID nemesisAttID = UUID.fromString("8ebe07b9-b10b-4561-ae0b-9e4e92cfac8c");
	private static final String nemesisAttName = "nemesisIdentifier";
	
	
	//Name mob on player kill
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onMonsterKillPlayer(PlayerDeathEvent event) {
		LivingEntity killer = EntityUtils.getKiller(event.getEntity());
		if (!(killer instanceof Zombie || killer instanceof Skeleton)) return;
		Monster monster = (Monster) killer;
		monster.setHealth(monster.getMaxHealth());
		
		if (monster.getCustomName() != null) return;

		String originalName = monster.getName();
		monster.setCustomName(MonsterNamers.generateName(monster));
		monster.setRemoveWhenFarAway(false);
		monster.setCanPickupItems(true);
		setNemesisAttribute(monster);
		
		//Change death message
		String message = event.getDeathMessage();
		event.setDeathMessage(message.replace(originalName, monster.getCustomName()));
	}

	/*@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMonsterSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() != SpawnReason.SPAWNER_EGG) return;
		if (!(event.getEntity() instanceof Monster)) return;
		Monster monster = (Monster) event.getEntity();
		monster.setCustomName(MonsterNamers.generateName(monster));
	}*/

	//Nemesis death message
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onNemesisDeath(EntityDeathEvent event) {
		if (!isNemesis(event.getEntity())) return;
		String name = event.getEntity().getCustomName(); 
		Bukkit.broadcastMessage(name + " has been defeated.");
	}
	
	private void setNemesisAttribute(LivingEntity entity) {
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(
				new AttributeModifier(nemesisAttID, nemesisAttName, 0, Operation.ADD_NUMBER));
	}

	private boolean isNemesis(LivingEntity entity) {
		if (entity.getCustomName() == null) return false;
		return entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().stream()
				.filter(mod -> mod.getUniqueId().equals(nemesisAttID))
				.anyMatch(mod -> mod.getName().equals(nemesisAttName));
	}

}
