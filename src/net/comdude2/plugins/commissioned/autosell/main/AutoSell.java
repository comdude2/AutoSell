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
	public static final long initial_delay = 600L;
	public static long repeating_delay = 1200L;
	
	/*
	 * Global Runtime
	 */
	
	private boolean loaded_before = false;
	
	/*
	 * Runtime
	 */
	private Essentials ess = null;
	public Economy econ = null;
	private Listeners listeners = null;
	private ChestManager cm = null;
	private FileConfiguration worth = null;
	private AutoSeller as = null;
	
	private int taskId = -1;
	
	public AutoSell(){
		
	}
	
	public void onEnable(){
		this.saveDefaultConfig();
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
		if (!this.loaded_before){this.getCommand("autosell").setExecutor(new AutoSellCommand(this));}
		AutoSell.repeating_delay = this.getConfig().getInt("delay") * 20L;
		this.cm = new ChestManager(this);
		if (this.listeners == null){this.listeners = new Listeners(this);}
		this.listeners.register();
		as = new AutoSeller(this, cm);
		taskId = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, as, AutoSell.initial_delay, AutoSell.repeating_delay);
		this.loaded_before = true;
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
	@Deprecated
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
	
	public AutoSeller getAutoSeller(){
		return this.as;
	}
	
}
