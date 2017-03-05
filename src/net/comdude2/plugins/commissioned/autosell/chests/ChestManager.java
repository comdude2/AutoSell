package net.comdude2.plugins.commissioned.autosell.chests;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Location;

import net.comdude2.plugins.commissioned.autosell.main.AutoSell;
import net.comdude2.plugins.commissioned.autosell.util.ObjectManager;
import net.md_5.bungee.api.ChatColor;

public class ChestManager {
	
	private AutoSell as = null;
	private ConcurrentLinkedQueue <AutoChest> chests = new ConcurrentLinkedQueue <AutoChest> ();
	
	public ChestManager(AutoSell as){
		this.as = as;
		load();
	}
	
	@SuppressWarnings("unchecked")
	public boolean load(){
		ConcurrentLinkedQueue <AutoChest> loadedChests = null;
		try{
			File f = new File(as.getDataFolder() + "/chests.obj");
			if (f.exists()){
				Object o = ObjectManager.readObject(f).readObject();
				if (o instanceof ConcurrentLinkedQueue <?>){
					loadedChests = (ConcurrentLinkedQueue <AutoChest>) o;
					if (loadedChests.size() > 0){
						chests = loadedChests;
						as.getLogger().info(ChatColor.stripColor(AutoSell.me) + "Chests loaded, count: " + chests.size());
					}else{
						as.getLogger().warning(ChatColor.stripColor(AutoSell.me) + "Chests size was 0, assuming that that's normal and that you've placed no signs.");
						chests = new ConcurrentLinkedQueue <AutoChest> ();
					}
				}else{
					try{f.renameTo(new File(as.getDataFolder() + "/corruptedChests.obj"));}catch(Exception e){}
					as.getLogger().severe(ChatColor.stripColor(AutoSell.me) + "Failed to load AutoSell chests.");
					as.getLogger().severe(ChatColor.stripColor(AutoSell.me) + "Chests renamed to 'corruptedChests.obj', creating new chests list.");
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			as.getLogger().severe(ChatColor.stripColor(AutoSell.me) + "Failed to load AutoSell chests.");
			as.getLogger().severe(ChatColor.stripColor(AutoSell.me) + "Chests renamed to 'corruptedChests.obj', creating new chests list.");
			return false;
		}
		return false;
	}
	
	public boolean save(){
		try{
			File f = new File(as.getDataFolder() + "/chests.obj");
			ObjectManager.writeObject(f, this.chests);
			if (f.exists()){return true;}
		}catch(Exception e){e.printStackTrace();}
		return false;
	}
	
	public ConcurrentLinkedQueue <AutoChest> getChests(){
		return this.chests;
	}
	
	public boolean chestExists(Location l){
		for (AutoChest c : chests){
			if (c.getChestLocation(as).getWorld().getUID() == l.getWorld().getUID()){
				if (c.getChestLocation(as).getBlockX() == l.getBlockX()){
					if (c.getChestLocation(as).getBlockY() == l.getBlockY()){
						if (c.getChestLocation(as).getBlockZ() == l.getBlockZ()){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public void addChest(AutoChest ac){
		this.chests.add(ac);
	}
	
	public void remove(AutoChest ac){
		this.chests.remove(ac);
	}
	
}
