package net.comdude2.plugins.commissioned.autosell.main;

import java.util.Map;

import net.comdude2.plugins.commissioned.autosell.chests.AutoSeller;
import net.comdude2.plugins.commissioned.autosell.chests.ChestManager;
import net.comdude2.plugins.commissioned.autosell.util.YamlManager;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

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
	private Essentials ess = null;
	public Economy econ = null;
	private Listeners listeners = null;
	private ChestManager cm = null;
	private FileConfiguration worth = null;
	
	private int taskId = -1;
	
	public AutoSell(){
		this.loaded_before = true;
	}
	
	public void onEnable(){
		this.saveDefaultConfig();
		//listYamlContents();  - for debugging worth file
		if (!setupEconomy()){
			this.getLogger().severe(ChatColor.stripColor(me) + " disabled due to missing Vault API hook.");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		try{
			ess = (Essentials) this.getServer().getPluginManager().getPlugin("Essentials");
			if (ess == null){throw new Exception("Essentials not loaded");}
		}catch(Exception e){
			this.getLogger().severe(ChatColor.stripColor(me) + " disabled due to missing Essentials hook.");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		//boolean loaded = this.loadWorthFile();
		//if (!loaded){this.getLogger().severe("Failed to load essentials worth file, disabling myself.");}else{this.getLogger().info("Loaded essentials worth file into memory!");}
		this.cm = new ChestManager(this);
		if (this.listeners == null){this.listeners = new Listeners(this);}
		this.listeners.register();
		taskId = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new AutoSeller(this, cm), 600L, 6000L);
		this.getLogger().severe(ChatColor.stripColor(me) + " version: " + this.getDescription().getVersion() + " enabled!");
	}
	
	public void onDisable(){
		if (this.listeners != null){this.listeners.unregister();}
		if (taskId != -1){this.getServer().getScheduler().cancelTask(taskId);}
		if (cm != null){cm.save();}
		this.getLogger().severe(ChatColor.stripColor(me) + " version: " + this.getDescription().getVersion() + " disabled!");
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
	
	@SuppressWarnings("unused")
	private void listYamlContents(){
		YamlManager ym = new YamlManager(this, "", "worth");
		Map <String, Object> contents = ym.getYAML().getValues(true);
		for (Map.Entry<String, Object> entry : contents.entrySet()){
			this.getLogger().info("Key: '" + entry.getKey() + "' Value: '" + entry.getValue() + "'");
		}
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private boolean loadWorthFile(){
		Plugin p = this.getServer().getPluginManager().getPlugin("Essentials");
		if (p != null){
			YamlManager ym = new YamlManager(p, "", "worth");
			if (ym.exists()){
				this.worth = ym.getYAML();
				Map <String, Object> contents = ym.getYAML().getValues(true);
				for (Map.Entry<String, Object> entry : contents.entrySet()){
					if (toInteger(entry.getValue()) == null){
						worth.set(entry.getKey(), null);
					}
				}
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	private static Integer toInteger(Object o){
		try{return Integer.valueOf(o.toString());}catch(Exception e){return null;}
	}
	
	/*
	 * Get and Set
	 */
	
	public ChestManager getChestManager(){
		return this.cm;
	}
	
	public Essentials getEssentials(){
		return this.ess;
	}
	
}
