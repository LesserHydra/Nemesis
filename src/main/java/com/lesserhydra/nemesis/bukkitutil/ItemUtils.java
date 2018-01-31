package com.lesserhydra.bukkitutil;

import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import com.lesserhydra.util.MapBuilder;

public class ItemUtils {

	private static final Map<Material, Double> materialAttackMap =
			MapBuilder.init(() -> new EnumMap<Material, Double>(Material.class))
			//Wood
			.put(Material.WOOD_PICKAXE,		2.0)
			.put(Material.WOOD_SPADE,		2.5)
			.put(Material.WOOD_SWORD,		4.0)
			.put(Material.WOOD_AXE,			7.0)
			//Gold
			.put(Material.GOLD_PICKAXE,		2.0)
			.put(Material.GOLD_SPADE,		2.5)
			.put(Material.GOLD_SWORD,		4.0)
			.put(Material.GOLD_AXE,			7.0)
			//Stone
			.put(Material.STONE_PICKAXE,	3.0)
			.put(Material.STONE_SPADE,		3.5)
			.put(Material.STONE_SWORD,		5.0)
			.put(Material.STONE_AXE,		9.0)
			//Iron
			.put(Material.IRON_PICKAXE,		4.0)
			.put(Material.IRON_SPADE,		4.5)
			.put(Material.IRON_SWORD,		6.0)
			.put(Material.IRON_AXE,			9.0)
			//Diamond
			.put(Material.DIAMOND_PICKAXE,	5.0)
			.put(Material.DIAMOND_SPADE,	5.5)
			.put(Material.DIAMOND_SWORD,	7.0)
			.put(Material.DIAMOND_AXE,		9.0)
			.buildImmutable();
	
	public static double getSwordDamage(ItemStack sword) {
		Material swordType = sword.getType();
		int damageLevel = sword.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
		return calculateMaterialBaseAttack(swordType) + calculateSharpnessAttack(damageLevel);
	}

	public static double calculateSharpnessAttack(int damageLevel) {
		if (damageLevel < 1) return 0;
		return 1 + 0.5 * (damageLevel - 1);
	}

	public static double calculatePowerModifier(int powerLevel) {
		if (powerLevel < 1) return 0;
		return 0.25 * (powerLevel + 1);
	}

	public static double calculateMaterialBaseAttack(Material swordType) {
		return materialAttackMap.getOrDefault(swordType, 1.0);
	}
	
}
