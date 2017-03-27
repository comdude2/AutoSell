package net.comdude2.plugins.commissioned.autosell.chests;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Location;

import net.comdude2.plugins.commissioned.autosell.main.AutoSell;
import net.comdude2.plugins.commissioned.autosell.util.ObjectManager;
import net.md_5.bungee.api.ChatColor;

public class ChestManager {
	
	private AutoSell as = null;
	private ConcurrentLinkedQueue <AutoChest> chests = new ConcurrentLinkedQueue <AutoChest> ();
	private ConcurrentLinkedQueue <UUID> do_not_notify = new ConcurrentLinkedQueue <UUID> ();
	
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
		//Should be more robust later
		ConcurrentLinkedQueue <UUID> do_not_notify = null;
		try{
			File f = new File(as.getDataFolder() + "/notify.obj");
			Object o = ObjectManager.readObject(f).readObject();
			if (o instanceof ConcurrentLinkedQueue <?>){
				do_not_notify = (ConcurrentLinkedQueue <UUID>)o;
				this.do_not_notify = do_not_notify;
				if (do_not_notify.size() > 0){
					as.getLogger().info("Players to not notify loaded, size was: " + do_not_notify.size());
				}else{
					as.getLogger().warning("Players to not notify size was 0, assuming this is normal and no one has opted out.");
				}
			}else{
				throw new Exception("Wrong datatype");
			}
		}catch(Exception e){this.do_not_notify = new ConcurrentLinkedQueue <UUID> ();as.getLogger().warning("Failed to load list of players to not notify, creating new...");}
		return false;
	}
	
	public boolean save(){
		boolean worked = false;
		try{
			File f = new File(as.getDataFolder() + "/chests.obj");
			ObjectManager.writeObject(f, this.chests);
			if (f.exists()){worked = true;}
		}catch(Exception e){e.printStackTrace();worked = false;}
		try{
			File f = new File(as.getDataFolder() + "/notify.obj");
			ObjectManager.writeObject(f, this.do_not_notify);
			if (!f.exists()){worked = false;}
		}catch(Exception e){e.printStackTrace();worked = false;}
		return worked;
	}
	
	public ConcurrentLinkedQueue <AutoChest> getChests(){
		return this.chests;
	}
	
	public boolean chestExists(Location l){
		for (AutoChest c : chests){
			if (c.getWorldId().equals(l.getWorld().getUID())){
				if (c.getX() == l.getX()){
					if (c.getY() == l.getY()){
						if (c.getZ() == l.getZ()){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public AutoChest getChest(Location l){
		for (AutoChest c : chests){
			if (c.getWorldId().equals(l.getWorld().getUID())){
				if (c.getX() == l.getX()){
					if (c.getY() == l.getY()){
						if (c.getZ() == l.getZ()){
							return c;
						}
					}
				}
			}
		}
		return null;
	}
	
	public void addChest(AutoChest ac){
		this.chests.add(ac);
	}
	
	public void remove(AutoChest ac){
		this.chests.remove(ac);
	}
	
	public ConcurrentLinkedQueue <UUID> getDoNotNotify(){
		return this.do_not_notify;
	}
	
	public void doNotNotify(UUID uuid){
		if (!this.do_not_notify.contains(uuid)){
			this.do_not_notify.add(uuid);
		}
	}
	
	public void notify(UUID uuid){
		if (this.do_not_notify.contains(uuid)){
			this.do_not_notify.remove(uuid);
		}
	}
	
}
