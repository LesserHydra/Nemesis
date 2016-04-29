package com.lesserhydra.nemesis;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import com.lesserhydra.bukkitutil.ItemUtils;

class MonsterRater {

	public static double rateAttribute(LivingEntity monster, Attribute attType, double maxMultiplier) {
		AttributeInstance attInst = monster.getAttribute(attType);
		double baseValue = attInst.getBaseValue();
		return (attInst.getValue() - baseValue)/(baseValue*maxMultiplier - baseValue);
	}

	public static double rateSword(LivingEntity monster, ItemStack sword, double maxAttack) {
		if (sword == null) return 0;
		double swordDamage = ItemUtils.getSwordDamage(sword) - 1;
		
		AttributeInstance attackAtt = monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
		for (AttributeModifier mod : attackAtt.getModifiers()) {
			if (mod.getOperation() != Operation.ADD_SCALAR) continue;
			swordDamage += swordDamage * mod.getAmount();
		}
		for (AttributeModifier mod : attackAtt.getModifiers()) {
			if (mod.getOperation() != Operation.MULTIPLY_SCALAR_1) continue;
			swordDamage *= 1 + mod.getAmount();
		}
		
		return swordDamage/maxAttack;
	}
	
	public static double rateBow(LivingEntity monster, ItemStack bow) {
		if (bow == null || bow.getType() != Material.BOW) return 0;
		return ItemUtils.calculatePowerModifier(bow.getEnchantmentLevel(Enchantment.ARROW_DAMAGE));
	}
}
