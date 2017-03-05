package net.comdude2.plugins.commissioned.autosell.main;

import net.comdude2.plugins.commissioned.autosell.chests.ChestManager;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoSell extends JavaPlugin{
	
	/*
	 * Static
	 */
	public static final String me = ChatColor.WHITE + "[" + ChatColor.GOLD + "AutoSell" + ChatColor.WHITE + "]: ";
	
	/*
	 * Global Runtime
	 */
	
	@SuppressWarnings("unused")
	private boolean loaded_before = false;
	
	/*
	 * Runtime
	 */
	public Economy econ = null;
	private Listeners listeners = null;
	private ChestManager cm = null;
	
	public AutoSell(){
		this.loaded_before = true;
	}
	
	public void onEnable(){
		this.saveDefaultConfig();
		if (!setupEconomy()){
			this.getLogger().severe(me + " disabled due to missing Vault API hook.");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		if (this.listeners == null){this.listeners = new Listeners(this);}
		this.listeners.register();
		this.getLogger().severe(me + " version: " + this.getDescription().getVersion() + " enabled!");
	}
	
	public void onDisable(){
		if (this.listeners != null){this.listeners.unregister();}
		this.getLogger().severe(me + " version: " + this.getDescription().getVersion() + " disabled!");
	}
	
	public boolean setupEconomy(){
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
	}
	
	/*
	 * Get and Set
	 */
	
	public ChestManager getChestManager(){
		return this.cm;
	}
	
}
