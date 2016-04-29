package com.lesserhydra.nemesis;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

enum RatingType {
	
	NORMAL ("Normal", "Weak", "Blind", "Short-Sighted", "Lame", "Deaf", "One-legged", "Boring", "Slow", "Diseased",
			"Skinny", "Sickly", "Ugly", "Terrible-At-Killing", "Slothful", "Warted", "Unnoteworthy", "Six-Fingered", "Dyslexic", "Old",
			"Haggard", "Pale", "Failure", "Three-Toed", "Scholar", "Refugee", "Disgraced", "Dead", "Minion", "Questionable",
			"Sleepy", "Tired", "Pitiful", "Short", "Hunchbacked", "Ignorant", "Thoughtless", "Hopeful", "Quaint", "Polite",
			"Undeserving", "Tireless", "Sneaky", "Tall", "Pitiless", "Cruel", "Green", "Newbie", "Castaway", "Four-Fingered",
			"Three-Fingered", "Four-Toed", "Six-Toed", "Secret Agent", "Fickle", "Giddy", "Dizzy", "Shaken", "Flightless", "Shivering",
			"Quivering", "Frightened", "Startled", "Chicken", "Coward", "Fearful", "Maniac", "Griefer", "Totally-Deadly", "Forgetful",
			"Unappealing", "Unpleasant", "Annoying", "Tiresome", "Dull", "Uninspired", "Humdrum", "Mundane", "Uninspiring", "Unexciting",
			"Unimaginative", "Unremarkable", "Typical", "Fragile", "Sick", "Feeble", "Frail", "Ailing", "Infirm", "Ill",
			"Lazy", "Slacker", "Sluggish", "Lethargic", "Floundering", "Flailing", "Stubby", "Revolting", "Vile", "Gloating", //100
			"Stinky", "Unshowered", "\"Special\"", "Incongruent", "Crooked", "Itchy", "Obsolete", "Wandering", "Odd", "Insulting",
			"Demeaning", "Clean", "Hungry", "Quack", "Awful", "Leper", "Boastful", "Outcast", "Misunderstood", "Alergic",
			"Talkative", "Babbling", "Icky", "Gabbing", "Snitch", "Driveling", "Bubbling", "Gossip", "Bumbling", "Inept",
			"Clumsy", "Blundering", "Graceless", "Maladroit", "Clutz", "Brash", "Diplomat", "Undiplomatic", "Sensitive", "Ticklish",
			"Irritable", "High-Strung", "Emotional", "Touchy", "Pathetic", "Temperamental", "Ardent", "Nervous", "Frantic", "Neurotic",
			"Egghead", "Punching Bag", "Ugly Mug") {
		@Override
		public double rate(LivingEntity monster) {
			return 0.5; //Amount that other catagories have to beat
		}
	},
	
	//Fire resistant
	FIREPROOF ("Fireproof", "Magmatic", "Flame-Retardant", "Fire-Resistant", "Incombustible", "Noncandescent", "Noncombustible", "Nonflammable", "Noninflammable", "Soaked",
			"Soggy", "Drenched", "Wet", "Sodden", "Dripping", "Waterlogged") {
		@Override
		public double rate(LivingEntity monster) {
			for (PotionEffect effect : monster.getActivePotionEffects()) {
				if (effect.getType() == PotionEffectType.FIRE_RESISTANCE && effect.getDuration() > 9600) return 0.5;
			}
			return 0;
		}
	},
	
	//Dual-wielding
	TWO_HANDED ("Ambidextrous", "Versatile", "Adaptable", "Showoff", "Braggadocio") {
		@Override
		public double rate(LivingEntity monster) {
			ItemStack offHand = monster.getEquipment().getItemInOffHand();
			return (offHand != null && offHand.getType() != Material.AIR) ? 0.5 : 0;
		}
	},

	//UNSTOPPABLE,
	
	HEALTHY ("Healthy", "Hearty", "Stout", "Vast", "Heavy", "Youthful", "Fit", "Hardy") {/*, "", "",
			"", "", "", "", "", "", "", "", "", "") {*/
		@Override
		public double rate(LivingEntity monster) {
			return MonsterRater.rateAttribute(monster, Attribute.GENERIC_MAX_HEALTH, 2);
		}
	},
	
	SPEEDY ("Speedy", "Swift", "Quick", "Rapid", "Fast", "Hurried", "Agile", "Nimble", "Fleet", "Spry",
			"Scout", "Light-Footed", "Slick", "Unrelenting", "Hyper", "Wild", "High-Strung", "Hyperactive", "\"Dashing\"", "Animated",
			"Zippy", "Zingy", "Energetic", "Lively", "Peppy", "Vivacious", "Effervescent", "Ebullient", "Exhilarated", "Spontaneous",
			"Impulsive") {
		@Override
		public double rate(LivingEntity monster) {
			return MonsterRater.rateAttribute(monster, Attribute.GENERIC_MOVEMENT_SPEED, 1.3);
		}
	},
	
	STRONG ("Strong", "Muscular", "Brawny", "Powerful", "Beefy", "Bruiser", "Pugilist", "Gladiator", "Boxer", "Slugger",
			"Heavy", "Brawler", "Bully", "Brute", "Thug", "Hooligan", "Ruffian", "Warrior", "Aggressor", "Disputant",
			"Inimical", "Nasty", "Rough", "Ornery", "Mean") {
		@Override
		public double rate(LivingEntity monster) {
			return MonsterRater.rateAttribute(monster, Attribute.GENERIC_ATTACK_DAMAGE, 2);
		}
	},
	
	SWORDSMAN ("Knight", "Butcher", "Killer", "Assassin", "Reaper", "Deadly", "Lethal", "Horrible", "Terror", "Horned",
			"Terrible", "Unmerciful", "Merciless", "Unsparing", "Ruthless", "Barbarian", "Brutal", "Callous", "Cruel", "Cutthroat",
			"Ferocious", "Fierce", "Malevolent", "Violent", "Sadistic", "Vicious", "Mercenary", "Commando", "Legionnaire", "Cruel",
			"Vengeful", "Spiteful", "Murderous", "Malignant", "Sinister", "Wicked", "Vile", "Ignoble", "Dignified", "Noble") {
		@Override
		public double rate(LivingEntity monster) {
			ItemStack weapon = monster.getEquipment().getItemInMainHand();
			return MonsterRater.rateSword(monster, weapon, 10);
		}
	},
	
	ARCHER ("Deadeye", "Sniper", "Fareyed", "Hawkeyed", "Eagle-Eyed", "Archer", "Bowman", "Precise", "Accurate", "Deadly",
			"Strong-Bowed", "Lethal", "Sharpshooter", "Straightshot", "Marksman", "Crackshot", "Expert", "Skilled", "Trained", "Adept",
			"Deft", "Experienced", "Sharp", "Apt", "Able", "Practiced", "Adroit", "Artist", "Ace", "Artiste",
			"Connoisseur", "Doyen", "Guru", "Phenomenon", "Pro", "Whiz", "Hotshot", "Proficient", "Virtuoso", "Old Pro",
			"Prodigy", "Slinger", "Marshal", "Freedom Fighter", "Mercenary", "Commando", "Merc", "Condottiere", "Freelancer", "Guerrilla",
			"Noble", "Dignified") {
		@Override
		public double rate(LivingEntity monster) {
			ItemStack weapon = monster.getEquipment().getItemInMainHand();
			return MonsterRater.rateBow(monster, weapon);
		}
	},
	
	TANK ("Unstoppable", "Horror", "Brute", "Tanker") {/*,
			"", "", "", "", "", "", "", "", "", "") {*/
		@Override
		public double rate(LivingEntity monster) {
			// TODO Analyze armor
			return 0;
		}
	},
	
	NEMESIS ("Nemesis", "Overpowered", "Adversary", "Cursed", "Scourge", "Tormenter", "Persecutor", "Antagonizer", "Oppressor", "Tyrant",
			"Legendary", "Eternal", "Abominable", "Abhorrent", "Accursed", "Monstrous") {
		@Override
		public double rate(LivingEntity monster) {
			//TODO: Combination of health, speed, strength, weapon, and armor
			return 0;
		}
	};
	
	private static final Random rand = new Random();
	
	private final List<String> possibleTitles;
	
	private RatingType(String... possibleTitles) {
		this.possibleTitles = Arrays.asList(possibleTitles);
	}
	
	public static RatingType getMostRelevant(LivingEntity monster) {
		RatingType best = null;
		double bestRating = -1;
		for (RatingType type : values()) {
			double rating = type.rate(monster);
			if (rating < bestRating) continue;
			best = type;
			bestRating = rating;
		}
		return best;
	}
	
	public abstract double rate(LivingEntity monster);
	
	public String getRandomTitle() {
		return possibleTitles.get(rand.nextInt(possibleTitles.size()));
	}
	
}
