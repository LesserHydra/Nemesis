package com.lesserhydra.nemesis;

import org.bukkit.plugin.java.JavaPlugin;

public class NemesisPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new NemesisListener(), this);
	}

}
